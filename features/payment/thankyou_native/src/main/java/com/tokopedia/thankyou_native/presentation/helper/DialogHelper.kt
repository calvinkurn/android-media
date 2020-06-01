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
        showSingleActionDialog(R.string.thank_payment_failed, R.string.thank_payment_failed_description,
                R.string.thank_shop_again) {
            dialogUnify?.cancel()
            listener.gotoHomePage()
        }
    }

    private fun showPaymentWaitingDialog() {
        showTwoActionDialog(R.string.thank_exit_this_page, R.string.thank_payment_waiting_desc,
                R.string.thank_exit_page, R.string.thank_cancel, {
            dialogUnify?.cancel()
            listener.gotoPaymentWaitingPage()
        }, { dialogUnify?.cancel() })
    }

    private fun showPaymentSuccessDialog() {
        showSingleActionDialog(R.string.thank_payment_success, R.string.thank_payment_success_description,
                R.string.thank_see_transaction_list) {
            dialogUnify?.cancel()
            listener.gotoOrderList()
        }
    }

    private fun showPaymentTimeExpired() {
        showSingleActionDialog(R.string.thank_payment_expired_title,
                R.string.thank_payment_expired_desc, R.string.thank_shop_again) {
            dialogUnify?.cancel()
            listener.gotoHomePage()
        }
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
    fun gotoHomePage()
    fun gotoPaymentWaitingPage()
    fun gotoOrderList()
}

