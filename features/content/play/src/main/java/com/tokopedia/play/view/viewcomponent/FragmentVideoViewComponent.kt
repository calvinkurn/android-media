package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 05/08/20
 */
class FragmentVideoViewComponent(
        private val channelId: String,
        private val container: ViewGroup,
        @IdRes idRes: Int,
        private val fragmentManager: FragmentManager,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    fun init() {
        fragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG) ?: getPlayVideoFragment().also {
            fragmentManager.beginTransaction()
                    .replace(rootView.id, it, VIDEO_FRAGMENT_TAG)
                    .commit()
        }

        rootView.setOnClickListener {
            listener.onFragmentClicked(this@FragmentVideoViewComponent)
        }
    }

    fun release() {
        fragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }

    private fun getPlayVideoFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayVideoFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val VIDEO_FRAGMENT_TAG = "fragment_video"
    }

    interface Listener {

        fun onFragmentClicked(view: FragmentVideoViewComponent)
    }
}