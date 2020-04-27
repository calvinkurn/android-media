package com.tokopedia.play.ui.youtube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 27/04/20
 */
class YouTubeView(
        container: ViewGroup,
        fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_youtube, container, true)
                    .findViewById(R.id.fragment_youtube)

    private val youtubeFragment = fragmentManager.findFragmentById(R.id.fragment_youtube) as? YouTubePlayerSupportFragment

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun setYouTubeId(youtubeId: String) {

    }

    internal fun release() {

    }
}