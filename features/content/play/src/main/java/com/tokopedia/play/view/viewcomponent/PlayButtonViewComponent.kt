package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class PlayButtonViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val ivPlay = findViewById<AppCompatImageView>(R.id.iv_play)
    private val tvWatchAgain = findViewById<TextView>(R.id.tv_watch_again)

    init {
        rootView.setOnClickListener {
            listener.onButtonClicked(this)
        }
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

        fun onButtonClicked(view: PlayButtonViewComponent)
    }
}