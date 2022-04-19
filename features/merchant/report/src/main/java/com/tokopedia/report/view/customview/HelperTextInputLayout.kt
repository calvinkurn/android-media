package com.tokopedia.report.view.customview

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import com.google.android.material.textfield.TextInputLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.core.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.EditText
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.report.R

class HelperTextInputLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr){

    var helperTextInputEnabled = false
        set(value) {
            if (field == value) return
            field = value
            switchHelperText()
        }
    private var errorTextEnabled = false
    private var helperTextAnimator: ViewPropertyAnimator? = null
    var helperTextInput: CharSequence = ""
        set(value) {
            if (field == value) return

            field = value
            helperTextInputEnabled = value.isNotBlank()
            setHelperTextOnHelperView(value)
        }
    var helperTextInputColor: ColorStateList? = null
        set(value) {
            field = value
            if (value != null)
                helperTextView.setTextColor(value)
        }
    var helperTextAppearance =  R.style.HelperTextAppearance
        set(value) {
            field = value
            TextViewCompat.setTextAppearance(helperTextView, value)
        }
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
            helperTextInputColor = a.getColorStateList(R.styleable.HelperTextInputLayout_helperTextColor)
        } finally {
            a.recycle()
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (child is EditText){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                helperTextView.setPadding(child.paddingStart, 0, child.paddingEnd, child.paddingBottom)
            } else {
                helperTextView.setPadding(child.paddingLeft, 0, child.paddingRight, child.paddingBottom)
            }
        }
    }

    override fun setErrorEnabled(enabled: Boolean) {
        if (errorTextEnabled == enabled) return
        errorTextEnabled = enabled

        if (errorTextEnabled && helperTextInputEnabled)
            switchHelperText()
        super.setErrorEnabled(enabled)
        if (!errorTextEnabled)
            switchHelperText()
    }

    private fun setHelperTextOnHelperView(text: CharSequence?){
        helperTextAnimator?.cancel()
        if (text.isNullOrBlank()){
            helperTextView.gone()
            if (helperTextView.text == text) return
            helperTextAnimator = helperTextView.animate().setInterpolator(INTERPOLATOR)
                    .alpha(0f).setDuration(200).withEndAction {
                        helperTextView.text = null
                    }
        } else {
            helperTextView.visible()
            helperTextView.text = text
            helperTextAnimator = helperTextView.animate().setInterpolator(INTERPOLATOR)
                    .alpha(1f).setDuration(200)
        }
        helperTextAnimator?.start()
    }

    private fun switchHelperText(){
        if (errorTextEnabled || !helperTextInputEnabled){
            setHelperTextOnHelperView(null)
        } else if (!errorTextEnabled && helperTextInputEnabled){
            setHelperTextOnHelperView(if (helperTextInput.isNotBlank()) helperText else null)
        }
    }

    companion object{
        private val INTERPOLATOR = FastOutSlowInInterpolator()
    }
}