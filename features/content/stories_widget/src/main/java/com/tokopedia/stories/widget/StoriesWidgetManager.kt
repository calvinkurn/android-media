package com.tokopedia.stories.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.widget.di.DaggerStoriesWidgetComponent
import com.tokopedia.stories.widget.di.StoriesWidgetComponent
import com.tokopedia.stories.widget.domain.StoriesEntrySource
import com.tokopedia.stories.widget.tracking.DefaultTrackingManager
import com.tokopedia.stories.widget.tracking.TrackingManager
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesWidgetManager private constructor(
    private val entryPoint: StoriesEntrySource,
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val options: Options
) {

    private val component = createComponent(context)
    private val viewModelFactory = component.storiesViewModelFactory()

    private val viewToObserverMap = mutableMapOf<StoriesWidgetLayout, StoriesWidgetMeta>()

    private var showCoachMarkJob: Job? = null

    private val storiesViewListener = object : StoriesWidgetLayout.Listener {
        override fun onClickedWhenHasStories(view: StoriesWidgetLayout, state: StoriesWidgetState) {
            component.router().route(view.context, state.appLink)
            options.trackingManager.clickEntryPoints(entryPoint)
        }

        override fun onImpressed(view: StoriesWidgetLayout, state: StoriesWidgetState) {
            options.trackingManager.impressEntryPoints(entryPoint)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val viewModelProvider by lazy {
        ViewModelProvider(
            viewModelStoreOwner,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    return viewModelFactory.create(entryPoint) as T
                }
            }
        )
    }

    private val coachMark = StoriesWidgetCoachMark(context) {
        getViewModel().onIntent(StoriesWidgetIntent.HasSeenCoachMark)
        mShopIdCoachMarked = null
    }

    private var mShopIdCoachMarked: String? = null

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        hideCoachMark()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        getViewModel().onIntent(StoriesWidgetIntent.GetLatestStoriesStatus)
                    }
                    else -> {}
                }
            }
        })

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val viewModel = getViewModel()

                launch {
                    viewModel.stories.collectLatest {
                        viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark)
                    }
                }

                launch {
                    viewModel.uiMessage.collect { message ->
                        if (message == null) return@collect

                        when (message) {
                            is StoriesWidgetMessage.ShowCoachMark -> {
                                mShopIdCoachMarked = message.shopId
                                showCoachMarkOnId(message.shopId)
                            }
                        }

                        viewModel.clearMessage(message.id)
                    }
                }
            }
        }

        options.scrollingParent?.let { observeScrollingView(it) }
    }

    fun manage(storiesView: StoriesWidgetLayout, shopId: String) {
        val meta = storiesView.getMeta() ?: StoriesWidgetMeta.Empty
        meta.attachListener?.let {
            storiesView.removeOnAttachStateChangeListener(it)
        }

        val listener = object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                storiesView.onAttached(shopId)
            }

            override fun onViewDetachedFromWindow(view: View) {
                storiesView.onDetached()
            }
        }
        storiesView.setMeta(meta.copy(attachListener = listener))
        storiesView.addOnAttachStateChangeListener(listener)

        if (storiesView.isAttachedToWindow) listener.onViewAttachedToWindow(storiesView)
    }

    fun updateStories(shopIds: List<String>) {
        getViewModel().onIntent(
            StoriesWidgetIntent.GetStoriesStatus(shopIds)
        )
    }

    fun hideCoachMark() {
        showCoachMarkJob?.cancel()
        coachMark.hide()
    }

    private fun requestShowCoachMark() {
        getViewModel().onIntent(StoriesWidgetIntent.ShowCoachMark)
    }

    private fun getViewModel(): StoriesWidgetViewModel {
        return viewModelProvider.get()
    }

    private fun createComponent(context: Context): StoriesWidgetComponent {
        return DaggerStoriesWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun showCoachMarkOnId(shopId: String) {
        getViewByShopId(shopId)
            ?.let(::showCoachMarkOnView)
    }

    private fun showCoachMarkOnView(view: StoriesWidgetLayout) {
        if (showCoachMarkJob?.isActive == true) return
        showCoachMarkJob = lifecycleOwner.lifecycleScope.launch {
            delay(1000)
            coachMark.show(
                view,
                "Ada update menarik dari toko ini"
            )
        }
    }

    private fun observeScrollingView(view: View) {
        val vto = view.viewTreeObserver
        vto.addOnScrollChangedListener {
            val scrollBounds = Rect()
            view.getHitRect(scrollBounds)

            val shopId = mShopIdCoachMarked ?: return@addOnScrollChangedListener
            val storiesLayout = getViewByShopId(shopId) ?: return@addOnScrollChangedListener
            if (storiesLayout.getLocalVisibleRect(scrollBounds)) {
                requestShowCoachMark()
            } else {
                hideCoachMark()
            }
        }
    }

    private fun StoriesWidgetLayout.onAttached(shopId: String) {
        val observer = getOrCreateObserver()
        observer.observe(shopId)
        assign(shopId, observer)

        setListener(storiesViewListener)

        if (shopId == mShopIdCoachMarked) requestShowCoachMark()
    }

    private fun StoriesWidgetLayout.onDetached() {
        coachMark.hide(this)
        setListener(null)
    }

    private fun StoriesWidgetLayout.getObserver(): StoriesWidgetObserver? {
        return viewToObserverMap[this]?.observer
    }

    private fun StoriesWidgetLayout.createObserver(): StoriesWidgetObserver {
        return StoriesWidgetObserver(
            getViewModel(),
            lifecycleOwner,
            this,
            options.animStrategy
        )
    }

    private fun StoriesWidgetLayout.getOrCreateObserver(): StoriesWidgetObserver {
        return getObserver() ?: createObserver()
    }

    private fun StoriesWidgetLayout.assign(shopId: String, observer: StoriesWidgetObserver) {
        val meta = getOrCreateMeta().copy(
            shopId = shopId,
            observer = observer
        )
        viewToObserverMap[this] = meta
    }

    private fun getViewByShopId(shopId: String): StoriesWidgetLayout? {
        val meta = viewToObserverMap.entries.firstOrNull {
            it.value.shopId == shopId
        } ?: return null
        return meta.key
    }

    private fun StoriesWidgetLayout.getMeta(): StoriesWidgetMeta? {
        return viewToObserverMap[this]
    }

    private fun StoriesWidgetLayout.getOrCreateMeta(): StoriesWidgetMeta {
        return getMeta() ?: StoriesWidgetMeta.Empty
    }

    private fun StoriesWidgetLayout.setMeta(meta: StoriesWidgetMeta) {
        viewToObserverMap[this] = meta
    }

    companion object {

        fun create(
            entryPoint: StoriesEntrySource,
            fragment: Fragment,
            builderOptions: Builder.() -> Unit
        ): StoriesWidgetManager {
            val builder = Builder(entryPoint, fragment)
            builder.builderOptions()
            return builder.build()
        }

        fun create(
            entryPoint: StoriesEntrySource,
            activity: AppCompatActivity,
            builderOptions: Builder.() -> Unit
        ): StoriesWidgetManager {
            val builder = Builder(entryPoint, activity)
            builder.builderOptions()
            return builder.build()
        }
    }

    class Builder private constructor(
        private val entryPoint: StoriesEntrySource,
        private val context: Context,
        private val lifecycleOwner: LifecycleOwner,
        private val viewModelStoreOwner: ViewModelStoreOwner
    ) {

        constructor(entryPoint: StoriesEntrySource, fragment: Fragment) : this(
            entryPoint,
            fragment.requireContext(),
            fragment.viewLifecycleOwner,
            fragment
        )
        constructor(entryPoint: StoriesEntrySource, activity: AppCompatActivity) : this(
            entryPoint,
            activity,
            activity,
            activity
        )

        private var mScrollingParent: View? = null
        private var mAnimationStrategy: AnimationStrategy = NoAnimateAnimationStrategy()
        private val trackingQueue : TrackingQueue get() = TrackingQueue(context)
        private val userSession get() =  UserSession(context.applicationContext)
        private val defaultTrackingManager get() =  DefaultTrackingManager(entryPoint, trackingQueue, userSession)
        private var mTrackingManager: TrackingManager = defaultTrackingManager

        fun setScrollingParent(view: View?) = builder {
            this.mScrollingParent = view
        }

        fun setAnimationStrategy(animStrategy: AnimationStrategy) = builder {
            mAnimationStrategy = animStrategy
        }

        fun setTrackingManager(trackingManager: TrackingManager) = builder {
            mTrackingManager = trackingManager
        }

        fun build(): StoriesWidgetManager {
            return StoriesWidgetManager(
                entryPoint,
                context,
                lifecycleOwner,
                viewModelStoreOwner,
                createOptions()
            )
        }

        private fun createOptions(): Options {
            return Options(
                mScrollingParent,
                mAnimationStrategy,
                mTrackingManager
            )
        }

        private fun builder(onBuild: () -> Unit): Builder {
            onBuild()
            return this
        }
    }

    class Options internal constructor(
        val scrollingParent: View?,
        val animStrategy: AnimationStrategy,
        val trackingManager: TrackingManager
    )

    internal data class StoriesWidgetMeta(
        val shopId: String,
        val observer: StoriesWidgetObserver?,
        val attachListener: OnAttachStateChangeListener?
    ) {
        companion object {
            val Empty: StoriesWidgetMeta
                get() = StoriesWidgetMeta(
                    "",
                    null,
                    null
                )
        }
    }
}
