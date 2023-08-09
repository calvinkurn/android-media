package com.tokopedia.stories.common

import android.content.Context
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarManager private constructor(
    private val key: StoriesKey,
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelStoreOwner: ViewModelStoreOwner
) {

    private val component = createComponent(context)
    private val viewModelFactory = component.storiesViewModelFactory()

    private val viewToObserverMap = mutableMapOf<StoriesAvatarView, StoriesAvatarMeta>()

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
    }

    fun manage(storiesView: StoriesAvatarView, shopId: String) {
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
    }

    fun updateStories(shopIds: List<String>) {
        getViewModel().onIntent(
            StoriesAvatarIntent.GetStoriesStatus(shopIds)
        )
    }

    fun hideCoachMark() {
        coachMark.hide()
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

    private fun showCoachMarkOnView(view: StoriesAvatarView) {
        coachMark.show(
            view,
            "Ada update menarik dari toko ini"
        )
    }

    private fun StoriesAvatarView.onAttached(shopId: String) {
        val observer = getOrCreateObserver()
        observer.observe(shopId)
        assign(shopId, observer)

        if (shopId == mShopIdCoachMarked) {
            getViewModel().onIntent(StoriesAvatarIntent.ShowCoachMark)
        }
    }

    private fun StoriesAvatarView.onDetached() {
        coachMark.hide(this)
    }

    private fun StoriesAvatarView.getObserver(): StoriesAvatarObserver? {
        return viewToObserverMap[this]?.observer
    }

    private fun StoriesAvatarView.createObserver(): StoriesAvatarObserver {
        return StoriesAvatarObserver(
            getViewModel(),
            lifecycleOwner,
            this
        )
    }

    private fun StoriesAvatarView.getOrCreateObserver(): StoriesAvatarObserver {
        return getObserver() ?: createObserver()
    }

    private fun StoriesAvatarView.assign(shopId: String, observer: StoriesAvatarObserver) {
        val meta = getOrCreateMeta().copy(
            shopId = shopId,
            observer = observer
        )
        viewToObserverMap[this] = meta
    }

    private fun getViewByShopId(shopId: String): StoriesAvatarView? {
        val meta = viewToObserverMap.entries.firstOrNull {
            it.value.shopId == shopId
        } ?: return null
        return meta.key
    }

    private fun StoriesAvatarView.getMeta(): StoriesAvatarMeta? {
        return viewToObserverMap[this]
    }

    private fun StoriesAvatarView.getOrCreateMeta(): StoriesAvatarMeta {
        return getMeta() ?: StoriesAvatarMeta.Empty
    }

    private fun StoriesAvatarView.setMeta(meta: StoriesAvatarMeta) {
        viewToObserverMap[this] = meta
    }

    companion object {

        fun create(key: StoriesKey, fragment: Fragment): StoriesAvatarManager {
            return StoriesAvatarManager(
                key,
                fragment.requireContext(),
                fragment.viewLifecycleOwner,
                fragment
            )
        }

        fun create(key: StoriesKey, activity: AppCompatActivity): StoriesAvatarManager {
            return StoriesAvatarManager(
                key,
                activity,
                activity,
                activity
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
