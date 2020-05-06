package com.tokopedia.play.ui.fragment.youtube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.custom.ScaleFriendlyFrameLayout
import com.tokopedia.play.view.fragment.PlayYouTubeFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentYouTubeView(
        container: ViewGroup,
        fragmentManager: FragmentManager,
        listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_youtube, container, true)
                    .findViewById(R.id.fl_youtube)

    private val flYouTube = view as ScaleFriendlyFrameLayout

    init {
        fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) ?: PlayYouTubeFragment.newInstance().also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, YOUTUBE_FRAGMENT_TAG)
                    .commit()
        }

        view.setOnClickListener {
            listener.onFragmentClicked(this@FragmentYouTubeView, isScaling = flYouTube.isScaling)
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    companion object {
        private const val YOUTUBE_FRAGMENT_TAG = "fragment_youtube_video"
    }

    interface Listener {

        fun onFragmentClicked(view: FragmentYouTubeView, isScaling: Boolean)
    }
}