package com.tokopedia.thankyou_native.presentation.helper

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.*

class DialogHelper(val context: Context, val listener: OnDialogRedirectListener) {

    private var dialogUnify: DialogUnify? = null

    internal fun showPaymentStatusDialog(paymentStatus: PaymentStatus?) {
        dialogUnify?.cancel()
        when (paymentStatus) {
            is PaymentVerified -> showPaymentSuccessDialog()
            is PaymentExpired -> showPaymentTimeExpired()
            is PaymentWaiting -> showPaymentWaitingDialog()
            is PaymentCancelled -> showPaymentFailedDialog()
        }
    }

    private fun showPaymentFailedDialog() {
        listener.gotoHomePage()
    }

    private fun showPaymentWaitingDialog() {
        listener.gotoPaymentWaitingPage()
    }

    private fun showPaymentSuccessDialog() {
        listener.gotoOrderList()
    }

    private fun showPaymentTimeExpired() {
        listener.gotoHomePage()
    }

    private fun getString(@StringRes resId: Int): String = context.getString(resId)


    private fun showTwoActionDialog(@StringRes titleRes: Int, @StringRes descriptionRes: Int,
                                    @StringRes primaryBtnTextRes: Int, @StringRes secondaryBtnTextRes: Int,
                                    onPrimaryBtnClick: () -> Unit, onSecondaryBtnClick: () -> Unit) {
        dialogUnify = DialogUnify(context = context, actionType = DialogUnify.HORIZONTAL_ACTION,
                imageType = DialogUnify.NO_IMAGE).apply {
            setTitle(getString(titleRes))
            setDescription(getString(descriptionRes))
            setPrimaryCTAText(getString(primaryBtnTextRes))
            setSecondaryCTAText(getString(secondaryBtnTextRes))
            setPrimaryCTAClickListener { onPrimaryBtnClick.invoke() }
            setSecondaryCTAClickListener { onSecondaryBtnClick.invoke() }
            show()
        }

    }

    private fun showSingleActionDialog(@StringRes titleRes: Int, @StringRes descriptionRes: Int,
                                       @StringRes primaryBtnTextRes: Int, onPrimaryBtnClick: () -> Unit) {
        dialogUnify = DialogUnify(context = context, actionType = DialogUnify.SINGLE_ACTION,
                imageType = DialogUnify.NO_IMAGE).apply {
            setTitle(getString(titleRes))
            setDescription(getString(descriptionRes))
            setPrimaryCTAText(getString(primaryBtnTextRes))
            setPrimaryCTAClickListener { onPrimaryBtnClick.invoke() }
            show()
        }
    }


}

interface OnDialogRedirectListener {
    fun launchApplink(applink: String)
    fun gotoHomePage()
    fun gotoPaymentWaitingPage()
    fun gotoOrderList()
    fun gotoOrderList(applink: String)
}

