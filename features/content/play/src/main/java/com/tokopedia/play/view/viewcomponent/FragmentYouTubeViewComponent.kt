package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.custom.ScaleFriendlyFrameLayout
import com.tokopedia.play.view.fragment.PlayYouTubeFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by jegul on 05/08/20
 */
class FragmentYouTubeViewComponent(
        private val channelId: String,
        private val container: ViewGroup,
        @IdRes idRes: Int,
        private val fragmentManager: FragmentManager,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val flYouTube = rootView as ScaleFriendlyFrameLayout

    private var isAlreadyInit: AtomicBoolean = AtomicBoolean(false)

    init {
        rootView.setOnClickListener {
            listener.onFragmentClicked(this@FragmentYouTubeViewComponent, isScaling = flYouTube.isScaling)
        }
    }

    fun safeInit() = synchronized(this) {
        if (isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(false, true)

        fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) ?: getPlayYouTubeFragment().also {
            fragmentManager.beginTransaction()
                    .replace(rootView.id, it, YOUTUBE_FRAGMENT_TAG)
                    .commit()
        }
    }

    fun safeRelease() = synchronized(this) {
        if (!isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(true, false)

        fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }

    private fun getPlayYouTubeFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayYouTubeFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val YOUTUBE_FRAGMENT_TAG = "fragment_youtube_video"
    }

    interface Listener {

        fun onFragmentClicked(view: FragmentYouTubeViewComponent, isScaling: Boolean)
    }
}