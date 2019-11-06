package com.tokopedia.payment.setting.detail.view.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.tokopedia.payment.setting.R


/**
 * Created by kris on 8/23/17. Tokopedia
 * Modified by aghny on 12/1/18
 */

class DeleteCreditCardDialogPayment : DialogFragment() {

    private var deleteCreditCardDialogListener: DeleteCreditCardDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.run {
            return AlertDialog.Builder(this)
                    .setTitle(R.string.payment_title_delete_credit_card)
                    .setMessage(R.string.payment_label_forever_delete_credit_card)
                    .setPositiveButton(R.string.payment_label_yes, DialogInterface.OnClickListener {
                        dialog, whichButton -> deleteCreditCardDialogListener?.onConfirmDelete(arguments?.getString(TOKEN_ID))
                    })
                    .setNegativeButton(R.string.payment_label_no, DialogInterface.OnClickListener { dialog, which -> })
                    .create()
        }
        return super.onCreateDialog(savedInstanceState)
    }

    fun setListener(deleteCreditCardDialogListener: DeleteCreditCardDialogListener){
        this.deleteCreditCardDialogListener = deleteCreditCardDialogListener
    }

    interface DeleteCreditCardDialogListener {
        fun onConfirmDelete(tokenId: String?)
    }

    companion object {

        private val TOKEN_ID = "TOKEN_ID"
        private val CARD_ID = "CARD_ID"

        fun newInstance(tokenId: String, cardId: String): DeleteCreditCardDialogPayment {
            val bundle = Bundle()
            bundle.putString(TOKEN_ID, tokenId)
            bundle.putString(CARD_ID, cardId)

            val cardDialog = DeleteCreditCardDialogPayment()
            cardDialog.arguments = bundle

            return cardDialog
        }
    }

}
