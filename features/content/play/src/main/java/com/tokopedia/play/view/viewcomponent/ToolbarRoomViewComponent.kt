package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on November 02, 2021
 */
class ToolbarRoomViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val tvTitle = findViewById<Typography>(R.id.tv_play_channel_title)

    init {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            listener.onBackButtonClicked(this)
        }
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    interface Listener {
        fun onBackButtonClicked(view: ToolbarRoomViewComponent)
    }
}
