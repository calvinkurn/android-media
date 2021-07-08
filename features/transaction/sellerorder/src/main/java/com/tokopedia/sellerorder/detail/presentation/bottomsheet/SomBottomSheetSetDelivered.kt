package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.*
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*
import kotlinx.android.synthetic.main.bottomsheet_set_delivered.view.*

class SomBottomSheetSetDelivered(
        context: Context,
        private val listener: SomBottomSheetSetDeliveredListener
) : SomBottomSheet(LAYOUT, true, true, false, context.getString(R.string.bottomsheet_set_delivered), context, true), TextWatcher, View.OnClickListener {

    companion object {
        private const val KEYBOARD_HEIGHT_PERCENTAGE_PORTRAIT = 0.25f
        private const val KEYBOARD_HEIGHT_PERCENTAGE_LANDSCAPE = 0.45f

        private val LAYOUT = R.layout.bottomsheet_set_delivered
    }

    private fun getSetDeliveredContainer(): ConstraintLayout? = childViews?.findViewById(R.id.containerSetDeliveredBottomSheet)
    private fun getSetDeliveredReceiverNameInputText(): TextFieldUnify? = childViews?.findViewById(R.id.tfSomDetailSetDeliveredReceiverName)
    private fun getSetDeliveredButton(): UnifyButton? = childViews?.findViewById(R.id.btnSomDetailChangeStatus)

    override fun setupChildView() {
        setupInputFieldReceiverName()
        getSetDeliveredButton()?.setOnClickListener(this)
        handleHideKeyboardWhenClickOnBottomSheet()
    }

    override fun show() {
        reset()
        super.show()
    }

    override fun dismiss(): Boolean {
        dismissKeyboard()
        return super.dismiss()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // noop
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // noop
    }

    override fun afterTextChanged(s: Editable?) {
        getSetDeliveredButton()?.isEnabled = !s.isNullOrBlank()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSomDetailChangeStatus -> onSetDeliveredClicked()
            else -> {}
        }
    }

    private fun onSetDeliveredClicked() {
        startLoading()
        getSetDeliveredReceiverNameInputText()?.clearFocus()
        val receiverName = getSetDeliveredReceiverNameInputText()?.textFieldInput?.text?.toString()
        if (receiverName.isNullOrBlank()) {
            getSetDeliveredReceiverNameInputText()?.apply {
                isTextFieldError = true
                setMessage(context?.getString(R.string.et_empty_error).orEmpty())
            }
            getSetDeliveredButton()?.isEnabled = false
            finishLoading()
        } else {
            listener.doSetDelivered(receiverName)
        }
    }

    private fun reset() {
        getSetDeliveredButton()?.isEnabled = false
        getSetDeliveredReceiverNameInputText()?.apply {
            textFieldInput.setText("")
            isTextFieldError = false
            setMessage("")
        }
        finishLoading()
    }

    private fun getKeyboardHeightEstimation(): Int {
        val heightPercentage = getKeyboardHeightPercentage()
        return (getScreenHeight() * heightPercentage).toInt()
    }

    private fun getKeyboardHeightPercentage(): Float {
        return if (isPortrait()) KEYBOARD_HEIGHT_PERCENTAGE_PORTRAIT else KEYBOARD_HEIGHT_PERCENTAGE_LANDSCAPE
    }

    private fun isPortrait(): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun handleHideKeyboardWhenClickOnBottomSheet() {
        bottomSheetLayout?.setOnClickListener {
            getSetDeliveredReceiverNameInputText()?.clearFocus()
        }
    }

    private fun dismissKeyboard() {
        getSetDeliveredReceiverNameInputText()?.rootView?.windowToken?.run {
            CommonUtils.hideKeyboard(context, getSetDeliveredReceiverNameInputText()?.rootView)
        }
    }

    private fun setupInputFieldReceiverName() {
        getSetDeliveredReceiverNameInputText()?.apply {
            textFieldInput.addTextChangedListener(this@SomBottomSheetSetDelivered)
            clearFocus()
        }
        if (DeviceScreenInfo.isTablet(context)) {
            getSetDeliveredReceiverNameInputText()?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    getSetDeliveredContainer()?.let {
                        it.setPadding(0, 0, 0, it.paddingBottom + getKeyboardHeightEstimation())
                    }
                } else {
                    getSetDeliveredContainer()?.let {
                        it.setPadding(0, 0, 0, it.paddingBottom - getKeyboardHeightEstimation())
                    }
                    dismissKeyboard()
                }
            }
        }
    }

    private fun startLoading() {
        getSetDeliveredButton()?.isLoading = true
    }

    private fun finishLoading() {
        getSetDeliveredButton()?.isLoading = false
    }

    fun onFailedSetDelivered() {
        finishLoading()
    }

    interface SomBottomSheetSetDeliveredListener {
        fun doSetDelivered(receiverName: String)
    }
}