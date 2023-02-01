package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationTitleFormBinding
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.showKeyboard
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import kotlinx.coroutines.*

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

    private val dispatchers = CoroutineDispatchersProvider
    private val scope = CoroutineScope(dispatchers.io)

    init {
        setupInsets()
        setupView()
    }

    private fun setupInsets() {
        binding.btnSave.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    @Suppress("ClickableViewAccessibility")
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

            textFieldTitle.editText.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    mListener?.onClickTextField()
                }
                false
            }

            textFieldTitle.clearIconView.setOnClickListener {
                textFieldTitle.editText.text.clear()
                mListener?.onClearTitle()
            }

            icCloseTitleForm.setOnClickListener {
                mListener?.onCloseTitleForm(this@TitleFormView)
            }

            btnSave.setOnClickListener {
                if(textFieldTitle.editText.text.isNotEmpty()) {
                    mListener?.onTitleSaved(
                        view = this@TitleFormView,
                        title = textFieldTitle.editText.text.toString()
                    )
                }
            }
        }
    }

    fun setMaxCharacter(max: Int) {
        binding.textFieldTitle.setCounter(max)
    }

    fun setTitle(title: String) {
        binding.textFieldTitle.editText.setText(title)
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loaderLoadingTitleForm.visibility = View.VISIBLE
            binding.textFieldTitle.visibility = View.GONE
            binding.btnSave.visibility = View.GONE
        } else {
            binding.loaderLoadingTitleForm.visibility = View.GONE
            binding.textFieldTitle.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.icCloseTitleForm.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
        scope.cancel()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (isAllowShowKeyboard(visibility)) showInputMethod()
    }

    private fun showInputMethod() {
        binding.textFieldTitle.editText.doOnLayout {
            scope.launch {
                delay(DELAY_SHOW_KEYBOARD)

                withContext(dispatchers.main) {
                    if(isAllowShowKeyboard(visibility)) {
                        binding.textFieldTitle.editText.requestFocus()
                        binding.textFieldTitle.editText.showKeyboard()
                    }
                }
            }
        }
    }

    private fun isAllowShowKeyboard(formVisibility: Int): Boolean {
        return formVisibility == View.VISIBLE && binding.textFieldTitle.visibility == View.VISIBLE
    }

    interface Listener {
        fun onClearTitle() { }
        fun onClickTextField() { }

        fun onCloseTitleForm(view: TitleFormView)
        fun onTitleSaved(view: TitleFormView, title: String)
    }

    companion object {
        private const val DELAY_SHOW_KEYBOARD = 1000L
    }
}
