package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveDurationInfoUiModel
import com.tokopedia.play_common.view.PlayNoImageEditText
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 06/07/21.
 */
class InteractiveSetupViewComponent(
    container: ViewGroup
) : ViewComponent(container, R.id.cl_interactive_setup) {

    private val etTitle: PlayNoImageEditText = findViewById(R.id.et_interactive_title)
    private val tvTimer: Typography = findViewById(R.id.tv_timer)

    val title: String
        get() = etTitle.text.toString()

    init {
        etTitle.setText(R.string.play_interactive_title_default)
    }

    fun setActiveTitle(title: String) {
        etTitle.setText(title)
    }

    fun setActiveDuration(duration: InteractiveDurationInfoUiModel) {
        tvTimer.text = duration.formatted
    }
}