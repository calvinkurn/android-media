package com.tokopedia.editor.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.tokopedia.editor.R
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifyprinciples.getTypeface as unifyTypeFaceGetter

class EditorEditTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatEditText(context, attributeSet) {

    init {
        id = View.generateViewId()

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        setBackgroundResource(R.drawable.bg_text_rounded)

        setPadding(4)
    }

    fun styleInsets(model: InputTextModel) {
        setText(model.text)
        gravity = textAlignment(model.textAlign)

        unifyTypeFaceGetter(context, model.textStyle.fontName).let {
            setTypeface(it, model.textStyle.fontStyle)
        }

        textColor(model)
    }

    fun setAsTextView() {
        isClickable = false
        isFocusable = false
        isCursorVisible = false
    }

    fun textAlignment(alignment: FontAlignment): Int {
        return when (alignment) {
            FontAlignment.CENTER -> Gravity.CENTER
            FontAlignment.LEFT -> Gravity.START
            FontAlignment.RIGHT -> Gravity.END
        }
    }

    private fun textColor(model: InputTextModel) {
        if (model.backgroundColor == null) {
            setTextColor(model.textColor)
            setBackgroundColor(Color.TRANSPARENT)
            return
        }

        val (textColor, backgroundColor) = model.backgroundColor ?: return
        setTextColor(textColor)
        background.setColorFilter(backgroundColor, PorterDuff.Mode.DST_ATOP)
    }
}
