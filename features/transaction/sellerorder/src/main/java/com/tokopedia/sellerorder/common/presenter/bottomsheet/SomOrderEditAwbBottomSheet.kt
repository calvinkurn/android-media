package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.content.Context
import android.content.res.Configuration
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.SomConsts
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.*

class SomOrderEditAwbBottomSheet(
        context: Context
) : SomBottomSheet(LAYOUT, true, true, false, SomConsts.TITLE_UBAH_RESI, context, true) {

    companion object {
        private const val KEYBOARD_HEIGHT_PERCENTAGE_PORTRAIT = 0.4f
        private const val KEYBOARD_HEIGHT_PERCENTAGE_LANDSCAPE = 0.5f

        private val LAYOUT = R.layout.bottomsheet_cancel_order
    }

    private var listener: SomOrderEditAwbBottomSheetListener? = null

    override fun setupChildView() {
        childViews?.run {
            tf_cancel_notes?.clearFocus()
            tf_cancel_notes?.setLabelStatic(true)
            tf_cancel_notes?.setMessage(context.getString(R.string.change_no_resi_notes))
            if (DeviceScreenInfo.isTablet(context)) {
                tf_cancel_notes?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        btnContainer?.let {
                            val layoutParams = it.layoutParams
                            layoutParams.height = layoutParams.height + getKeyboardHeightEstimation()
                            it.layoutParams = layoutParams
                        }
                    } else {
                        btnContainer?.let {
                            val layoutParams = it.layoutParams
                            layoutParams.height = layoutParams.height - getKeyboardHeightEstimation()
                            it.layoutParams = layoutParams
                        }
                        hideKeyboard(context, tf_cancel_notes?.rootView)
                    }
                }
            }
            tf_cancel_notes?.textFieldInput?.hint = context.getString(R.string.change_no_resi_hint)
            btn_cancel_order_canceled?.setOnClickListener {
                hideKeyboard(context, tf_cancel_notes?.rootView)
                dismiss()
            }
            btn_cancel_order_confirmed?.text = context.getString(R.string.change_no_resi_btn_ubah)
            btn_cancel_order_confirmed?.setOnClickListener {
                hideKeyboard(context, tf_cancel_notes?.rootView)
                dismiss()
                listener?.onEditAwbButtonClicked(tf_cancel_notes?.textFieldInput?.text.toString())
            }
            handleHideKeyboardWhenClickOnBottomSheet()
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

    override fun dismiss() {
        childViews?.tf_cancel_notes?.rootView?.windowToken?.run {
            hideKeyboard(context, childViews?.tf_cancel_notes?.rootView)
        }
        super.dismiss()
    }

    private fun handleHideKeyboardWhenClickOnBottomSheet() {
        bottomSheetLayout?.setOnClickListener {
            childViews?.tf_cancel_notes?.clearFocus()
        }
    }

    fun setListener(listener: SomOrderEditAwbBottomSheetListener) {
        this.listener = listener
    }

    interface SomOrderEditAwbBottomSheetListener {
        fun onEditAwbButtonClicked(cancelNotes: String)
    }
}