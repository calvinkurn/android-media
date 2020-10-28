package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 12/06/20.
 */
class ActionBarViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.action_bar){

    private var ivSwitchCamera: AppCompatImageView = findViewById(R.id.iv_switch)
    private var tvClose: AppCompatTextView = findViewById(R.id.tv_close)
    private var tvTitle: Typography = findViewById(R.id.tv_title)

    init {
        ivSwitchCamera.setOnClickListener { listener.onCameraIconClicked() }
        tvClose.setOnClickListener { listener.onCloseIconClicked() }
    }

    fun setTitle(label: String) {
        tvTitle.text = label
    }

    fun setupCloseButton(actionTitle: String) {
        tvClose.text = actionTitle
        tvClose.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    interface Listener {

        fun onCameraIconClicked()

        fun onCloseIconClicked()
    }
}