package com.tokopedia.play.broadcaster.view.custom.pinnedmessage

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgFormBinding
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.showKeyboard
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 11/10/21
 */
class PinnedMessageFormView : ConstraintLayout {

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

    private val binding = ViewPlayBroPinnedMsgFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var mListener: Listener? = null

    init {
        setupView()
        setupInsets()
    }

    private fun setupView() {
        with(binding) {
            textFieldPinnedMsg.labelText.visibility = View.GONE
            val whiteColor = MethodChecker.getColor(
                context, unifyR.color.Unify_Static_White
            )
            val secondaryColor = MethodChecker.getColor(
                context, R.color.play_bro_dms_pinned_msg_form_border
            )
            textFieldPinnedMsg.textInputLayout.boxStrokeColor = secondaryColor
            textFieldPinnedMsg.editText.setTextColor(whiteColor)
            textFieldPinnedMsg.editText.setHintTextColor(secondaryColor)
            textFieldPinnedMsg.counterView?.setTextColor(whiteColor)
            textFieldPinnedMsg.clearIconView.setImage(
                newLightEnable = whiteColor,
                newDarkEnable = whiteColor
            )

            val tempSecondaryColor = textFieldPinnedMsg.secondaryColorList
            val secondaryColorStateList = ColorStateList(
                textFieldPinnedMsg.disabledStateList,
                tempSecondaryColor.apply { this[0] = whiteColor }
            )
            textFieldPinnedMsg.textInputLayout.setHelperTextColor(secondaryColorStateList)
            textFieldPinnedMsg.secondaryColorStateList = secondaryColorStateList

            textFieldPinnedMsg.editText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    mListener?.onPinnedMessageSaved(
                        view = this@PinnedMessageFormView,
                        message = textFieldPinnedMsg.editText.text.toString()
                    )
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            showInputMethod()

            iconClose.setOnClickListener {
                mListener?.onCloseButtonClicked(this@PinnedMessageFormView)
            }
        }
    }

    private fun setupInsets() {
        binding.root.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(
                top = insets.systemWindowInsetTop + padding.top,
                bottom = insets.systemWindowInsetBottom + padding.bottom
            )
        }
    }

    fun setPinnedMessage(message: String) {
        binding.textFieldPinnedMsg.editText.setText(message)
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loaderLoading.visibility = View.VISIBLE
            binding.textFieldPinnedMsg.visibility = View.GONE
        } else {
            binding.loaderLoading.visibility = View.GONE
            binding.textFieldPinnedMsg.visibility = View.VISIBLE
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
        binding.textFieldPinnedMsg.editText.doOnLayout {
            binding.textFieldPinnedMsg.editText.requestFocus()
            binding.textFieldPinnedMsg.editText.showKeyboard()
        }
    }

    interface Listener {

        fun onCloseButtonClicked(view: PinnedMessageFormView)
        fun onPinnedMessageSaved(view: PinnedMessageFormView, message: String)
    }
}