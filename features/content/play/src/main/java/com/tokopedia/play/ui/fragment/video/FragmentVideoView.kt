package com.tokopedia.play.ui.fragment.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayVideoFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentVideoView(
        channelId: String,
        container: ViewGroup,
        fragmentManager: FragmentManager,
        listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_video, container, true)
                    .findViewById(R.id.fl_video)

    init {
        fragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG) ?: PlayVideoFragment.newInstance(channelId).also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, VIDEO_FRAGMENT_TAG)
                    .commit()
        }

        view.setOnClickListener {
            listener.onFragmentClicked(this@FragmentVideoView)
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
        private const val VIDEO_FRAGMENT_TAG = "fragment_video"
    }

    interface Listener {

        fun onFragmentClicked(view: FragmentVideoView)
    }
}