package com.tokopedia.payment.setting.detail

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.tokopedia.payment.setting.R


/**
 * Created by kris on 8/23/17. Tokopedia
 * Modified by aghny on 12/1/18
 */

class DeleteCreditCardDialogPayment : DialogFragment() {

    private var mDeleteCreditCardDialogListener: DeleteCreditCardDialogListener? = null

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mDeleteCreditCardDialogListener = activity as DeleteCreditCardDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(activity.toString() + " must implement NoticeDialogListener")
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        activity?.run {
            return AlertDialog.Builder(this)
                    .setTitle(R.string.delete_credit_card)
                    .setMessage(R.string.forever_delete_credit_card)
                    .setPositiveButton(R.string.label_title_button_yes, DialogInterface.OnClickListener {
                        dialog, whichButton -> mDeleteCreditCardDialogListener?.onConfirmDelete(arguments?.getString(TOKEN_ID))
                    })
                    .setNegativeButton(R.string.label_title_button_no, DialogInterface.OnClickListener { dialog, which -> })
                    .create()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDeleteCreditCardDialogListener = context as DeleteCreditCardDialogListener
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
