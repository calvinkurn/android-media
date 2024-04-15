package com.tokopedia.stories.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewTreeObserver
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
import com.tokopedia.stories.widget.di.DaggerStoriesWidgetComponent
import com.tokopedia.stories.widget.di.StoriesWidgetComponent
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.stories.widget.tracking.DefaultTrackerBuilder
import com.tokopedia.stories.widget.tracking.DefaultTrackerSender
import com.tokopedia.stories.widget.tracking.StoriesWidgetTracker
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesWidgetManager private constructor(
    private val entryPoint: StoriesEntryPoint,
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
            val tracker = options.trackerBuilder.onClickedEntryPoint(state)
            options.trackerSender.sendTracker(tracker)
        }

        override fun onImpressed(view: StoriesWidgetLayout, state: StoriesWidgetState) {
            val tracker = options.trackerBuilder.onImpressedEntryPoint(state)
            options.trackerSender.sendTracker(tracker)
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

    private val coachMarkScrollingListener = object : ViewTreeObserver.OnScrollChangedListener {
        override fun onScrollChanged() {
            val scrollBounds = Rect()
            options.scrollingParent?.getHitRect(scrollBounds)

            val shopId = mShopIdCoachMarked ?: return
            val storiesLayout = getViewByShopId(shopId) ?: return
            if (storiesLayout.getLocalVisibleRect(scrollBounds)) {
                requestShowCoachMark()
            } else {
                hideCoachMark()
            }
        }
    }

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
                    Lifecycle.Event.ON_DESTROY -> {
                        options.scrollingParent?.viewTreeObserver
                            ?.removeOnScrollChangedListener(coachMarkScrollingListener)
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
                                showCoachMarkOnId(message.shopId, message.text)
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

    fun updateStories(
        shopIds: List<String>,
        categoryIds: List<String> = emptyList(),
        productIds: List<String> = emptyList(),
    ) {
        options.trackerSender.reset()

        getViewModel().onIntent(
            StoriesWidgetIntent.GetStoriesStatus(shopIds, categoryIds, productIds)
        )
    }

    private fun hideCoachMark() {
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

    private fun showCoachMarkOnId(shopId: String, text: String) {
        getViewByShopId(shopId)
            ?.let { showCoachMarkOnView(it, text) }
    }

    private fun showCoachMarkOnView(view: StoriesWidgetLayout, text: String) {
        if (!options.coachMarkStrategy.canShowCoachMark() || text.isBlank()) return
        if (showCoachMarkJob?.isActive == true) return
        showCoachMarkJob = lifecycleOwner.lifecycleScope.launch {
            delay(1000)
            coachMark.show(view, text)
        }
    }

    private fun observeScrollingView(view: View) {
        val vto = view.viewTreeObserver
        vto.addOnScrollChangedListener(coachMarkScrollingListener)
    }

    private fun StoriesWidgetLayout.onAttached(shopId: String) {
        setListener(storiesViewListener)

        val observer = getOrCreateObserver()
        observer.observe(shopId)
        assign(shopId, observer)

        if (shopId == mShopIdCoachMarked) requestShowCoachMark()
    }

    private fun StoriesWidgetLayout.onDetached() {
        coachMark.hide(this)
        setListener(null)
        viewToObserverMap.remove(this)
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
            entryPoint: StoriesEntryPoint,
            fragment: Fragment,
            builderOptions: Builder.() -> Unit
        ): StoriesWidgetManager {
            val builder = Builder(entryPoint, fragment)
            builder.builderOptions()
            return builder.build()
        }

        fun create(
            entryPoint: StoriesEntryPoint,
            activity: AppCompatActivity,
            builderOptions: Builder.() -> Unit
        ): StoriesWidgetManager {
            val builder = Builder(entryPoint, activity)
            builder.builderOptions()
            return builder.build()
        }
    }

    class Builder private constructor(
        private val entryPoint: StoriesEntryPoint,
        private val context: Context,
        private val lifecycleOwner: LifecycleOwner,
        private val viewModelStoreOwner: ViewModelStoreOwner
    ) {

        constructor(entryPoint: StoriesEntryPoint, fragment: Fragment) : this(
            entryPoint,
            fragment.requireContext(),
            fragment.viewLifecycleOwner,
            fragment
        )
        constructor(entryPoint: StoriesEntryPoint, activity: AppCompatActivity) : this(
            entryPoint,
            activity,
            activity,
            activity
        )

        private var mScrollingParent: View? = null
        private var mAnimationStrategy: AnimationStrategy = NoAnimateAnimationStrategy()
        private val userSession = UserSession(context.applicationContext)
        private var mTrackerBuilder: StoriesWidgetTracker.Builder? = null
        private var mTrackerSender: StoriesWidgetTracker.Sender? = null
        private var mCoachMarkStrategy: CoachMarkStrategy? = null

        fun setScrollingParent(view: View?) = builder {
            this.mScrollingParent = view
        }

        fun setAnimationStrategy(animStrategy: AnimationStrategy) = builder {
            mAnimationStrategy = animStrategy
        }

        fun setTrackerBuilder(trackerBuilder: StoriesWidgetTracker.Builder) = builder {
            mTrackerBuilder = trackerBuilder
        }

        fun setTrackerSender(trackerSender: StoriesWidgetTracker.Sender) = builder {
            mTrackerSender = trackerSender
        }

        fun setCoachMarkStrategy(coachMarkStrategy: CoachMarkStrategy) = builder {
            mCoachMarkStrategy = coachMarkStrategy
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
                mTrackerBuilder ?: DefaultTrackerBuilder(entryPoint, userSession),
                mTrackerSender ?: DefaultTrackerSender(),
                mCoachMarkStrategy ?: DefaultCoachMarkStrategy(lifecycleOwner)
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
        val trackerBuilder: StoriesWidgetTracker.Builder,
        val trackerSender: StoriesWidgetTracker.Sender,
        val coachMarkStrategy: CoachMarkStrategy
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
