package com.tokopedia.play.ui.playbutton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 10/12/19
 */
class PlayButtonView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_play_button, container, true)
                    .findViewById(R.id.cl_play_control_round)

    private val ivPlay = view.findViewById<AppCompatImageView>(R.id.iv_play)
    private val tvWatchAgain = view.findViewById<TextView>(R.id.tv_watch_again)

    init {
        view.setOnClickListener {
            listener.onButtonClicked(this)
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun showPlayButton() {
        ivPlay.setImageResource(com.tokopedia.play_common.R.drawable.ic_play_play_round)
        tvWatchAgain.hide()
        show()
    }

    fun showRepeatButton() {
        ivPlay.setImageResource(R.drawable.ic_play_repeat_round)
        tvWatchAgain.show()
        show()
    }

    interface Listener {

        fun onButtonClicked(view: PlayButtonView)
    }
}