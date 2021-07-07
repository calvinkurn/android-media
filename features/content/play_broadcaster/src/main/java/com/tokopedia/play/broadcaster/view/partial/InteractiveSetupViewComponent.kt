package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.extension.toMinute
import com.tokopedia.play.broadcaster.util.extension.toSecond
import com.tokopedia.play_common.view.PlayNoImageEditText
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 06/07/21.
 */
class InteractiveSetupViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_interactive_setup) {

    private val etTitle: PlayNoImageEditText = findViewById(R.id.et_interactive_title)
    private val tvTimer: Typography = findViewById(R.id.tv_timer)

    private val title: String
        get() = etTitle.text.toString()

    init {
        etTitle.setText(R.string.play_interactive_title_default)
        etTitle.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT
                && title.length in MIN_LENGTH_CHAR..MAX_LENGTH_CHAR) {
                listener.onNextSoftKeyboardClicked(this@InteractiveSetupViewComponent, title)
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun setActiveTitle(title: String) {
        etTitle.setText(title)
    }

    fun setActiveDuration(duration: Long) {
        tvTimer.text = getString(R.string.play_interactive_selected_duration_format, duration.toMinute(), duration.toSecond())
    }

    fun focusOnEditTitle() {
        etTitle.isFocusableInTouchMode = true
        etTitle.requestFocus()
        etTitle.setSelection(title.length)
    }

    interface Listener {
        fun onNextSoftKeyboardClicked(view: InteractiveSetupViewComponent, title: String)
    }

    companion object {
        private const val MIN_LENGTH_CHAR = 3
        private const val MAX_LENGTH_CHAR = 25
    }
}