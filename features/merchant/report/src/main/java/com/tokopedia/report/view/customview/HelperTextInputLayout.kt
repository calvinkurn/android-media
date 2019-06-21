package com.tokopedia.report.view.customview

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.design.widget.TextInputLayout
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.widget.TextView
import com.tokopedia.report.R

class HelperTextInputLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr){

    private var helperTextEnabled = false
    private var errorTextEnabled = false
    private var helperText: CharSequence = ""
        set(value) {
            if (field == value) return

            field = value
            helperTextEnabled = value.isNotEmpty()
        }
    private var helperTextColor: ColorStateList? = null
    private var helperTextAppearance =  R.style.HelperTextAppearance
    private val helperTextView = TextView(context).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextAppearance(helperTextAppearance)
        }
        this@HelperTextInputLayout.addView(this)
    }

    init {
        attrs?.let { initConfig(it) }
    }

    private fun initConfig(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.HelperTextInputLayout)
        try {
            helperText = a.getText(R.styleable.HelperTextInputLayout_helperText)
            helperTextColor = a.getColorStateList(R.styleable.HelperTextInputLayout_helperTextColor)
        } finally {
            a.recycle()
        }
    }

    companion object{
        private val INTERPOLATOR = FastOutSlowInInterpolator()
    }
}