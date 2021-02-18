package com.tokopedia.play.broadcaster.view.partial

import android.text.SpannableStringBuilder
import android.text.Spanned
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
) : ViewComponent(container, idRes) {

    private val textField: TextFieldUnify = rootView as TextFieldUnify

    init {
        textField.textFieldWrapper.hint = getTitleLabelText()
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
}