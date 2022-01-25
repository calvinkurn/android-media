package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationTitleFormBinding
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.showKeyboard
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.play.broadcaster.R

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
class TitleFormView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayBroPreparationTitleFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var mListener: Listener? = null

    init {
        setupView()
    }

    private fun setupView() {
        with(binding) {
            textFieldTitle.labelText.visibility = View.GONE
            val whiteColor = MethodChecker.getColor(
                context, unifyR.color.Unify_Static_White
            )
            val secondaryColor = MethodChecker.getColor(
                context, R.color.play_bro_dms_title_form_border
            )
            textFieldTitle.textInputLayout.boxStrokeColor = secondaryColor
            textFieldTitle.editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            textFieldTitle.editText.setTextColor(whiteColor)
            textFieldTitle.editText.setHintTextColor(secondaryColor)
            textFieldTitle.counterView?.setTextColor(whiteColor)
            textFieldTitle.clearIconView.setImage(
                newLightEnable = whiteColor,
                newDarkEnable = whiteColor
            )

            val tempSecondaryColor = textFieldTitle.secondaryColorList
            val secondaryColorStateList = ColorStateList(
                textFieldTitle.disabledStateList,
                tempSecondaryColor.apply { this[0] = whiteColor }
            )
            textFieldTitle.textInputLayout.setHelperTextColor(secondaryColorStateList)
            textFieldTitle.secondaryColorStateList = secondaryColorStateList

            textFieldTitle.editText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(textFieldTitle.editText.text.isNotEmpty()) {
                        mListener?.onTitleSaved(
                            view = this@TitleFormView,
                            title = textFieldTitle.editText.text.toString()
                        )
                        return@setOnKeyListener true
                    }
                }
                return@setOnKeyListener false
            }

            icCloseTitleForm.setOnClickListener {
                mListener?.onCloseTitleForm(this@TitleFormView)
            }
        }
    }

    fun setTitle(title: String) {
        binding.textFieldTitle.editText.setText(title)
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loaderLoadingTitleForm.visibility = View.VISIBLE
            binding.textFieldTitle.visibility = View.GONE
        } else {
            binding.loaderLoadingTitleForm.visibility = View.GONE
            binding.textFieldTitle.visibility = View.VISIBLE
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.root.requestApplyInsetsWhenAttached()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) showInputMethod()
    }

    private fun showInputMethod() {
        binding.textFieldTitle.editText.doOnLayout {
            binding.textFieldTitle.editText.requestFocus()
            binding.textFieldTitle.editText.showKeyboard()
        }
    }

    interface Listener {
        fun onCloseTitleForm(view: TitleFormView)
        fun onTitleSaved(view: TitleFormView, title: String)
    }
}