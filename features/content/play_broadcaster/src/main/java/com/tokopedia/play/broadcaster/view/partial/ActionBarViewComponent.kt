package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 12/06/20.
 */
class ActionBarViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.cl_actionbar){

    private val ivClose = findViewById<ImageUnify>(R.id.iv_close)
    private val tvTitle = findViewById<Typography>(R.id.tv_title)
    private val btnClose = findViewById<UnifyButton>(R.id.btn_close)

    init {
        findViewById<ImageUnify>(R.id.iv_switch).setOnClickListener { listener.onCameraIconClicked() }

        ivClose.setOnClickListener { listener.onCloseIconClicked() }
        ivClose.show()

        btnClose.setOnClickListener { listener.onCloseIconClicked() }
        btnClose.hide()
    }

    fun setTitle(label: String) {
        tvTitle.text = label
    }

    fun setActionTitle(text: String) {
        btnClose.text = text
        btnClose.show()
        ivClose.hide()
    }

    interface Listener {
        fun onCameraIconClicked()
        fun onCloseIconClicked()
    }
}