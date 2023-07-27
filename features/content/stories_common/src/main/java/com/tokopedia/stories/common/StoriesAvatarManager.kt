package com.tokopedia.stories.common

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.stories.common.di.DaggerStoriesAvatarComponent
import com.tokopedia.stories.common.di.StoriesAvatarComponent

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarManager(
    context: Context,
    private val viewModelStoreOwner: ViewModelStoreOwner,
) {

    private val component = createComponent(context)
    private val viewModelFactory = component.viewModelFactory()

    fun manage(storiesView: StoriesAvatarView) {
        storiesView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                storiesView.onAttached()
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

    private fun StoriesAvatarView.onAttached() {
        attach(getViewModel())
    }

    private fun StoriesAvatarView.onDetached() {
        detach()
    }

    companion object {
        fun tiedTo(fragment: Fragment): StoriesAvatarManager {
            return StoriesAvatarManager(
                fragment.requireContext(),
                fragment,
            )
        }

        fun tiedTo(activity: AppCompatActivity): StoriesAvatarManager {
            return StoriesAvatarManager(
                activity,
                activity,
            )
        }
    }
}
