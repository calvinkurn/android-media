package com.tokopedia.stories.common

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.stories.common.di.DaggerStoriesAvatarComponent
import com.tokopedia.stories.common.di.StoriesAvatarComponent

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarManager(
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelStoreOwner: ViewModelStoreOwner
) {

    private val component = createComponent(context)
    private val viewModelFactory = component.viewModelFactory()

    private val viewToObserverMap = mutableMapOf<StoriesAvatarView, StoriesAvatarObserver>()

    private val observerListener = object : StoriesAvatarObserver.Listener {
        override fun onShowCoachMark(observer: StoriesAvatarObserver, view: StoriesAvatarView) {
            coachMark.showCoachMark(
                arrayListOf(
                    CoachMark2Item(view, "Ada update menarik dari toko ini", "")
                )
            )
        }
    }

    private val coachMark = CoachMark2(context)

    init {
    }

    fun manage(storiesView: StoriesAvatarView, shopId: String) {
        storiesView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                storiesView.onAttached(shopId)
            }

            override fun onViewDetachedFromWindow(view: View) {
                storiesView.onDetached()
            }
        })
    }

    fun updateStories(shopIds: List<String>) {
        getViewModel().onIntent(
            StoriesAvatarIntent.GetStoriesStatus(shopIds)
        )
    }

    private fun getViewModel(): StoriesAvatarViewModel {
        return ViewModelProvider(viewModelStoreOwner, viewModelFactory).get()
    }

    private fun createComponent(context: Context): StoriesAvatarComponent {
        return DaggerStoriesAvatarComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun StoriesAvatarView.onAttached(shopId: String) {
        val observer = getOrCreateObserver()
        observer.observe(shopId)
        assign(observer)
    }

    private fun StoriesAvatarView.onDetached() {
        val coachMarkItems = coachMark.coachMarkItem
        if (coachMarkItems.firstOrNull { it.anchorView == this } == null) return

        coachMark.dismissCoachMark()
    }

    private fun StoriesAvatarView.getObserver(): StoriesAvatarObserver? {
        return viewToObserverMap[this]
    }

    private fun StoriesAvatarView.createObserver(): StoriesAvatarObserver {
        return StoriesAvatarObserver(
            getViewModel(),
            lifecycleOwner,
            this,
            observerListener
        )
    }

    private fun StoriesAvatarView.getOrCreateObserver(): StoriesAvatarObserver {
        return getObserver() ?: createObserver()
    }

    private fun StoriesAvatarView.assign(observer: StoriesAvatarObserver) {
        viewToObserverMap[this] = observer
    }

    companion object {
        fun tiedTo(fragment: Fragment): StoriesAvatarManager {
            return StoriesAvatarManager(
                fragment.requireContext(),
                fragment.viewLifecycleOwner,
                fragment
            )
        }

        fun tiedTo(activity: AppCompatActivity): StoriesAvatarManager {
            return StoriesAvatarManager(
                activity,
                activity,
                activity
            )
        }
    }
}
