package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.databinding.BottomsheetSetDeliveredBinding

class SomBottomSheetSetDelivered(
        context: Context,
        private val listener: SomBottomSheetSetDeliveredListener
) : SomBottomSheet<BottomsheetSetDeliveredBinding>(LAYOUT, true, true, false, false, false, context.getString(R.string.bottomsheet_set_delivered), context, true), TextWatcher, View.OnClickListener {

    companion object {
        private const val KEYBOARD_HEIGHT_PERCENTAGE_PORTRAIT = 0.25f
        private const val KEYBOARD_HEIGHT_PERCENTAGE_LANDSCAPE = 0.45f

        private val LAYOUT = R.layout.bottomsheet_set_delivered
    }

    override fun bind(view: View): BottomsheetSetDeliveredBinding {
        return BottomsheetSetDeliveredBinding.bind(view)
    }

    override fun setupChildView() {
        setupInputFieldReceiverName()
        binding?.btnSomDetailChangeStatus?.setOnClickListener(this)
        handleHideKeyboardWhenClickOnBottomSheet()
    }

    override fun show() {
        reset()
        super.show()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // noop
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // noop
    }

    override fun afterTextChanged(s: Editable?) {
        binding?.btnSomDetailChangeStatus?.isEnabled = !s.isNullOrBlank()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSomDetailChangeStatus -> onSetDeliveredClicked()
            else -> {}
        }
    }

    private fun onSetDeliveredClicked() {
        binding?.run {
            startLoading()
            tfSomDetailSetDeliveredReceiverName.clearFocus()
            val receiverName = tfSomDetailSetDeliveredReceiverName.textFieldInput.text?.toString()
            if (receiverName.isNullOrBlank()) {
                tfSomDetailSetDeliveredReceiverName.run {
                    isTextFieldError = true
                    setMessage(context?.getString(R.string.et_empty_error).orEmpty())
                }
                btnSomDetailChangeStatus.isEnabled = false
                finishLoading()
            } else {
                listener.doSetDelivered(receiverName)
            }
        }
    }

    private fun reset() {
        binding?.run {
            btnSomDetailChangeStatus.isEnabled = false
            tfSomDetailSetDeliveredReceiverName.run {
                textFieldInput.setText("")
                isTextFieldError = false
                setMessage("")
            }
            finishLoading()
        }
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
            binding?.tfSomDetailSetDeliveredReceiverName?.clearFocus()
        }
    }

    private fun setupInputFieldReceiverName() {
        binding?.run {
            tfSomDetailSetDeliveredReceiverName.run {
                textFieldInput.addTextChangedListener(this@SomBottomSheetSetDelivered)
                clearFocus()
            }
            if (DeviceScreenInfo.isTablet(context)) {
                tfSomDetailSetDeliveredReceiverName.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        containerSetDeliveredBottomSheet.let {
                            it.setPadding(0, 0, 0, it.paddingBottom + getKeyboardHeightEstimation())
                        }
                    } else {
                        containerSetDeliveredBottomSheet.let {
                            it.setPadding(0, 0, 0, it.paddingBottom - getKeyboardHeightEstimation())
                        }
                        root.hideKeyboard()
                    }
                }
            }
        }
    }

    private fun startLoading() {
        binding?.btnSomDetailChangeStatus?.isLoading = true
    }

    private fun finishLoading() {
        binding?.btnSomDetailChangeStatus?.isLoading = false
    }

    fun onFailedSetDelivered() {
        finishLoading()
    }

    interface SomBottomSheetSetDeliveredListener {
        fun doSetDelivered(receiverName: String)
    }
}