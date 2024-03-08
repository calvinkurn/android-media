@file:Suppress("UsePropertyAccessSyntax", "DEPRECATION")

package com.tokopedia.editor.ui.widget

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.LineBackgroundSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment.Companion.toGravity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.getTypeface as unifyTypeFaceGetter

class EditorEditTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatEditText(context, attributeSet) {

    // var for text background color, set via span
    private var backgroundColor = Color.TRANSPARENT
    private var alignment = RoundedSpan.ALIGN_CENTER

    // used for edittext padding & span padding
    private val padding: Int = 16.toPx()
    private val textBackgroundPadding = 7.toPx()
    private val textRoundedSize = 8.toPx()

    private var isSpanImplemented = false

    init {
        inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        setPadding(padding)
        setShadowLayer(padding.toFloat(), 0f, 0f, 0)
        setLineSpacing(LINE_HEIGHT_EXTRA, LINE_HEIGHT_MULTIPLIER)
        textSize = DEFAULT_FONT_SIZE

        // set for edittext background color, to remove underline
        setBackgroundColor(Color.TRANSPARENT)

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(newText: Editable?) {
                if (newText?.toString()?.length.orZero() <= 0) {
                    isSpanImplemented = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, start: Int, end: Int, count: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, end: Int, count: Int) {
                if (start == 0 && end == 0 && count > 0 && !isSpanImplemented) {
                    setText(text)
                }
            }
        })
    }

    private fun setSpan(paramText: CharSequence?): SpannableString {
        val spanString = SpannableString(paramText)
        val roundedSpan = RoundedSpan(
            backgroundColor = backgroundColor,
            padding = textBackgroundPadding,
            radius = textRoundedSize,
            lineHeightExtra = lineSpacingExtra
        ).apply {
            this.setAlignment(alignment)
        }
        spanString.setSpan(
            roundedSpan,
            0,
            text?.length ?: 1,
            SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spanString
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        this.editableText?.let {
            it.getSpans(0, text?.length ?: 0, LineBackgroundSpan::class.java)?.apply {
                if (this.isNotEmpty()) {
                    it.removeSpan(this.first())
                }
            }
        }

        var spanString: SpannableString? = null
        if (text?.length.orZero() > 0) {
            spanString = setSpan(text)
            isSpanImplemented = true
        }

        super.setText(spanString ?: text, type)
        setSelection(spanString?.length ?: 0)
    }

    fun setColor(textColor: Int, backgroundColor: Int) {
        setTextColor(textColor)
        this.backgroundColor = backgroundColor

        setText(text)
    }

    fun setAlignment(alignment: Int) {
        this.alignment = alignment

        setText(text)
    }

    /**
     * Set default properties for [EditorEditTextView]
     *
     * This setter will set the textSize and padding default.
     */
    fun default() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE)
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

        this.alignment = model.textAlign.value
        setText(model.text)
        setColor(model.textColor, model.backgroundColor ?: Color.TRANSPARENT)
        setGravity(model.textAlign.toGravity())
    }

    /**
     * Mark the EditText of [EditorEditTextView] as read-only.
     */
    fun setAsTextView() {
        isClickable = false
        isFocusable = false
        isCursorVisible = false
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f

        private const val LINE_HEIGHT_EXTRA = 12f
        private const val LINE_HEIGHT_MULTIPLIER = 1f
    }
}
