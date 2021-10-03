package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.databinding.BottomsheetSendEmailBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohSendEmailBottomSheet {
    private var actionListener: ActionListener? = null

    fun show(context: Context,
             fragmentManager: FragmentManager,
             gqlGroup: String,
             orderData: UohListOrder.Data.UohOrders.Order) {
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        // TODO : coba2 by autoClearedNullable<BottomsheetSendEmailBinding>()
        val binding = BottomsheetSendEmailBinding.inflate(LayoutInflater.from(context))
        binding.run {
            tfEmail.textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val isEmailValid = UohUtils.isEmailValid(s.toString())
                    btnSendEmail.isEnabled = isEmailValid
                    if (s.toString().isEmpty()) {
                        tfEmail.setError(true)
                        tfEmail.setMessage(UohConsts.EMAIL_MUST_NOT_BE_EMPTY)
                    } else {
                        if (!isEmailValid) {
                            tfEmail.setError(true)
                            tfEmail.setMessage(UohConsts.WRONG_FORMAT_EMAIL)
                        } else {
                            tfEmail.setError(false)
                            tfEmail.setMessage("")
                        }
                    }
                }

            })

            val email = tfEmail.textFieldInput.text.toString()
            btnSendEmail.setOnClickListener { actionListener?.onEmailSent(email, gqlGroup, orderData)  }
        }
        bottomSheet.isFullpage = false
        bottomSheet.isKeyboardOverlap = false
        bottomSheet.setChild(binding.root)
        bottomSheet.setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
        bottomSheet.show(fragmentManager, "")
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
    }

    interface ActionListener {
        fun onEmailSent(email: String, gqlGroup: String, orderData: UohListOrder.Data.UohOrders.Order)
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}