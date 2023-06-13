package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetSendEmailBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohSendEmailBottomSheet : BottomSheetUnify() {
    private var listener: UohSendEmailBottomSheetListener? = null
    private var binding by autoClearedNullable<BottomsheetSendEmailBinding>()

    companion object {
        private const val TAG: String = "UohLsFinishOrderBottomSheet"

        @JvmStatic
        fun newInstance(): UohSendEmailBottomSheet { return UohSendEmailBottomSheet() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var email = ""
        binding = BottomsheetSendEmailBinding.inflate(LayoutInflater.from(context), null, false)
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
                            email = s.toString()
                        }
                    }
                }
            })

            btnSendEmail.setOnClickListener {
                dismiss()
                listener?.onEmailSent(email)
            }
        }
        showCloseIcon = true
        showHeader = true
        isFullpage = false
        isKeyboardOverlap = false
        setChild(binding?.root)
        setTitle(UohConsts.RESEND_ETICKET_BOTTOMSHEET_TITLE)
        setCloseClickListener { dismiss() }
    }

    interface UohSendEmailBottomSheetListener {
        fun onEmailSent(email: String)
    }

    fun setListener(listener: UohSendEmailBottomSheetListener) {
        this.listener = listener
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}
