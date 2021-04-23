package com.tokopedia.play.broadcaster.view.partial

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 17/02/21
 */
class TextFieldTitleViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        listener: Listener
) : ViewComponent(container, idRes) {

    private val textField: TextFieldUnify = rootView as TextFieldUnify

    init {
        textField.textFieldWrapper.hint = getTitleLabelText()
        textField.textFieldInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) listener.onTitleInputChanged(this@TextFieldTitleViewComponent, s.toString())
            }
        })
    }

    fun getText() = textField.textFieldInput.text.toString()

    fun setText(text: String) {
        textField.textFieldInput.setText(text)
    }

    private fun getTitleLabelText(): CharSequence {
        val asterisk = '*'
        val finalText = "${getString(R.string.play_title_input_label)}$asterisk"
        val spanBuilder = SpannableStringBuilder(finalText)
        spanBuilder.setSpan(
                ForegroundColorSpan(getColor(unifyR.color.Unify_R500)),
                finalText.indexOf(asterisk),
                finalText.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        return spanBuilder
    }

    interface Listener {

        fun onTitleInputChanged(view: TextFieldTitleViewComponent, title: String)
    }
}