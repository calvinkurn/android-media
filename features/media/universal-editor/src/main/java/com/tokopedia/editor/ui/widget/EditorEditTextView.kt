@file:Suppress("UsePropertyAccessSyntax", "DEPRECATION")

package com.tokopedia.editor.ui.widget

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import com.tokopedia.editor.R
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment.Companion.toGravity
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.getTypeface as unifyTypeFaceGetter

class EditorEditTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatEditText(context, attributeSet) {

    /**
     * Set default properties for [EditorEditTextView]
     *
     * This setter will set the textSize and padding default.
     */
    fun default() {
        setBackgroundResource(R.drawable.bg_text_rounded)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE)
        setPadding(DEFAULT_BACKGROUND_PADDING.toPx())
    }

    /**
     * Set a custom view id for [EditorEditTextView]
     *
     * This id will be recognized as viewId during view creation in container,
     * e.g. when the user add a new text object into [DynamicTextCanvasLayout].
     *
     * If we didn't set this, thus the view inside the container is unrecognisable.
     *
     * This setter will used for [viewId] in [InputTextUiModel]
     */
    fun setViewId() {
        id = View.generateViewId()
    }

    /**
     * Apply the custom styles from [InputTextModel]
     *
     * This insets contain a text, typeface, alignment, as well as colors.
     * The styles will be generated from [InputTextActivity].
     */
    fun styleInsets(model: InputTextModel) {
        val typeface = unifyTypeFaceGetter(context, model.fontDetail.fontName)
        setTypeface(typeface, model.fontDetail.fontStyle)

        setText(model.text)
        setTextColor(model.textColor)
        setGravity(model.textAlign.toGravity())
        setBackgroundTextColor(model.backgroundColor)
    }

    /**
     * Mark the EditText of [EditorEditTextView] as read-only.
     */
    fun setAsTextView() {
        isClickable = false
        isFocusable = false
        isCursorVisible = false
    }

    private fun setBackgroundTextColor(color: Int?) {
        val mColor = color ?: Color.TRANSPARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            background.colorFilter = BlendModeColorFilter(mColor, BlendMode.DST_ATOP)
        } else {
            background.setColorFilter(mColor, PorterDuff.Mode.DST_ATOP)
        }
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f
        private const val DEFAULT_BACKGROUND_PADDING = 4
    }
}
