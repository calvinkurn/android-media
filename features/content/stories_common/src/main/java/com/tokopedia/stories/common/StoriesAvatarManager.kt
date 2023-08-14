package com.tokopedia.stories.common

import android.content.Context
import android.graphics.Rect
import android.util.Log
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
import com.tokopedia.stories.common.di.DaggerStoriesAvatarComponent
import com.tokopedia.stories.common.di.StoriesAvatarComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarManager private constructor(
    private val key: StoriesKey,
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelStoreOwner: ViewModelStoreOwner,
    parentScrollingView: View? = null,
) {

    private val component = createComponent(context)
    private val viewModelFactory = component.storiesViewModelFactory()

    private val viewToObserverMap = mutableMapOf<StoriesBorderLayout, StoriesAvatarMeta>()

    private var showCoachMarkJob: Job? = null

    @Suppress("UNCHECKED_CAST")
    private val viewModelProvider by lazy {
        ViewModelProvider(
            viewModelStoreOwner,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    return viewModelFactory.create(key) as T
                }
            }
        )
    }

    private val coachMark = StoriesAvatarCoachMark(context) {
        getViewModel().onIntent(StoriesAvatarIntent.HasSeenCoachMark)
        mShopIdCoachMarked = null
    }

    private var mShopIdCoachMarked: String? = null

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_PAUSE) hideCoachMark()
            }
        })

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val viewModel = getViewModel()

                launch {
                    viewModel.stories.collectLatest {
                        viewModel.onIntent(StoriesAvatarIntent.ShowCoachMark)
                    }
                }

                launch {
                    viewModel.uiMessage.collect { message ->
                        if (message == null) return@collect

                        when (message) {
                            is StoriesAvatarMessage.ShowCoachMark -> {
                                mShopIdCoachMarked = message.shopId
                                showCoachMarkOnId(message.shopId)
                            }
                        }

                        viewModel.clearMessage(message.id)
                    }
                }
            }
        }

        Log.d("StoriesBorderLayout", "parentScrollingView: $parentScrollingView")
        parentScrollingView?.let { observeScrollingView(it) }
    }

    fun manage(storiesView: StoriesBorderLayout, shopId: String) {
        val meta = storiesView.getMeta() ?: StoriesAvatarMeta.Empty
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
            StoriesAvatarIntent.GetStoriesStatus(shopIds)
        )
    }

    fun hideCoachMark() {
        showCoachMarkJob?.cancel()
        coachMark.hide()
    }

    private fun requestShowCoachMark() {
        getViewModel().onIntent(StoriesAvatarIntent.ShowCoachMark)
    }

    private fun getViewModel(): StoriesAvatarViewModel {
        return viewModelProvider.get()
    }

    private fun createComponent(context: Context): StoriesAvatarComponent {
        return DaggerStoriesAvatarComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun showCoachMarkOnId(shopId: String) {
        getViewByShopId(shopId)
            ?.let(::showCoachMarkOnView)
    }

    private fun showCoachMarkOnView(view: StoriesBorderLayout) {
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
                Log.d("StoriesBorderLayout", "Request Stories CoachMark")
                requestShowCoachMark()
            } else {
                Log.d("StoriesBorderLayout", "Hide CoachMark")
                hideCoachMark()
            }
        }
    }

    private fun StoriesBorderLayout.onAttached(shopId: String) {
        val observer = getOrCreateObserver()
        observer.observe(shopId)
        assign(shopId, observer)

        if (shopId == mShopIdCoachMarked) requestShowCoachMark()
    }

    private fun StoriesBorderLayout.onDetached() {
        coachMark.hide(this)
    }

    private fun StoriesBorderLayout.getObserver(): StoriesAvatarObserver? {
        return viewToObserverMap[this]?.observer
    }

    private fun StoriesBorderLayout.createObserver(): StoriesAvatarObserver {
        return StoriesAvatarObserver(
            getViewModel(),
            lifecycleOwner,
            this
        )
    }

    private fun StoriesBorderLayout.getOrCreateObserver(): StoriesAvatarObserver {
        return getObserver() ?: createObserver()
    }

    private fun StoriesBorderLayout.assign(shopId: String, observer: StoriesAvatarObserver) {
        val meta = getOrCreateMeta().copy(
            shopId = shopId,
            observer = observer
        )
        viewToObserverMap[this] = meta
    }

    private fun getViewByShopId(shopId: String): StoriesBorderLayout? {
        val meta = viewToObserverMap.entries.firstOrNull {
            it.value.shopId == shopId
        } ?: return null
        return meta.key
    }

    private fun StoriesBorderLayout.getMeta(): StoriesAvatarMeta? {
        return viewToObserverMap[this]
    }

    private fun StoriesBorderLayout.getOrCreateMeta(): StoriesAvatarMeta {
        return getMeta() ?: StoriesAvatarMeta.Empty
    }

    private fun StoriesBorderLayout.setMeta(meta: StoriesAvatarMeta) {
        viewToObserverMap[this] = meta
    }

    companion object {

        fun create(key: StoriesKey, fragment: Fragment, scrollingView: View? = null): StoriesAvatarManager {
            return StoriesAvatarManager(
                key,
                fragment.requireContext(),
                fragment.viewLifecycleOwner,
                fragment,
                scrollingView
            )
        }

        fun create(key: StoriesKey, activity: AppCompatActivity, scrollingView: View? = null): StoriesAvatarManager {
            return StoriesAvatarManager(
                key,
                activity,
                activity,
                activity,
                scrollingView,
            )
        }
    }

    internal data class StoriesAvatarMeta(
        val shopId: String,
        val observer: StoriesAvatarObserver?,
        val attachListener: OnAttachStateChangeListener?
    ) {
        companion object {
            val Empty: StoriesAvatarMeta
                get() = StoriesAvatarMeta(
                    "",
                    null,
                    null
                )
        }
    }
}
