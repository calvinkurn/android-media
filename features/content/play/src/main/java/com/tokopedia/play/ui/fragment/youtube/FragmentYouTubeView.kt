package com.tokopedia.play.ui.fragment.youtube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.custom.ScaleFriendlyFrameLayout
import com.tokopedia.play.view.fragment.PlayYouTubeFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentYouTubeView(
        private val channelId: String,
        container: ViewGroup,
        private val fragmentManager: FragmentManager,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_youtube, container, true)
                    .findViewById(R.id.fl_youtube)

    private val flYouTube = view as ScaleFriendlyFrameLayout

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun init() {
        fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) ?: getPlayYouTubeFragment().also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, YOUTUBE_FRAGMENT_TAG)
                    .commit()
        }

        view.setOnClickListener {
            listener.onFragmentClicked(this@FragmentYouTubeView, isScaling = flYouTube.isScaling)
        }
    }

    internal fun release() {
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

        fun onFragmentClicked(view: FragmentYouTubeView, isScaling: Boolean)
    }
}