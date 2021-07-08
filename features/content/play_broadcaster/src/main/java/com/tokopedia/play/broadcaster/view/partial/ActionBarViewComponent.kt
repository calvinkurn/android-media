package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 12/06/20.
 */
class ActionBarViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.action_bar){

    private val ivClose = findViewById<ImageUnify>(R.id.iv_close)
    private val tvTitle = findViewById<Typography>(R.id.tv_title)
    private val tvClose = findViewById<Typography>(R.id.tv_close)

    init {
        findViewById<ImageUnify>(R.id.iv_switch).setOnClickListener { listener.onCameraIconClicked() }
        ivClose.setOnClickListener { listener.onCloseIconClicked() }
        ivClose.show()
        tvClose.hide()
    }

    fun setTitle(label: String) {
        tvTitle.text = label
    }

    fun setActionTitle(text: String) {
        tvClose.text = text
        tvClose.show()
        ivClose.hide()
    }

    interface Listener {
        fun onCameraIconClicked()
        fun onCloseIconClicked()
    }
}