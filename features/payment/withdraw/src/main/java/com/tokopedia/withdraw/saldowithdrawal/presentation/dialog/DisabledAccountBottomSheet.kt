package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant

class DisabledAccountBottomSheet : BottomSheetUnify() {
    private val childLayout = R.layout.swd_dialog_disabled_bank_account

    private lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childView = LayoutInflater.from(context).inflate(childLayout, null, false)
        setChild(childView)
        childView.findViewById<View>(R.id.btnCheckPremiumProgram).setOnClickListener {
            dismiss()
            WithdrawConstant.openRekeningAccountInfoPage(context)
        }
    }

}