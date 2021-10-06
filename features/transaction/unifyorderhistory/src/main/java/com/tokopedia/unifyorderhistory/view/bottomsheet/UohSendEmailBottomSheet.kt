package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.databinding.BottomsheetSendEmailBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohSendEmailBottomSheet : BottomSheetUnify() {
    private var actionListener: ActionListener? = null
    private var bottomSheetSendEmail : BottomSheetUnify? = null
    private var binding by autoClearedNullable<BottomsheetSendEmailBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = BottomsheetSendEmailBinding.inflate(LayoutInflater.from(context), null, false)
    }

    fun show(fragmentManager: FragmentManager, gqlGroup: String, orderData: UohListOrder.Data.UohOrders.Order) {
        bottomSheetSendEmail = BottomSheetUnify()
        binding?.run {
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
        bottomSheetSendEmail?.run {
            showCloseIcon = true
            showHeader = true
            isFullpage = false
            isKeyboardOverlap = false
            setChild(binding?.root)
            setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
            setCloseClickListener { dismiss() }
        }
        bottomSheetSendEmail?.show(fragmentManager, "")
    }

    interface ActionListener {
        fun onEmailSent(email: String, gqlGroup: String, orderData: UohListOrder.Data.UohOrders.Order)
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }

    fun setLayoutError() {
        binding?.run {
            tfEmail.run {
                setError(true)
                setMessage(getString(R.string.toaster_failed_send_email))
            }

        }
    }

    fun doDismiss() {
        if (bottomSheetSendEmail?.isVisible == true) bottomSheetSendEmail?.dismiss()
    }
}