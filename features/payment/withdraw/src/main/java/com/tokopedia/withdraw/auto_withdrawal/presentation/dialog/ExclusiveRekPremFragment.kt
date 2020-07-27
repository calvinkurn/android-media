package com.tokopedia.withdraw.auto_withdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant

class ExclusiveRekPremFragment : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = LayoutInflater.from(context)
                .inflate(R.layout.swd_layout_awd_exclusive_rp, null, false)
        rootView.apply {
            setChild(this)
            findViewById<View>(R.id.btnJoinPremiumProgramFree).setOnClickListener {
                WithdrawConstant.openRekeningAccountInfoPage(context)
                dismiss()
            }
        }
    }


    companion object {
        fun getInstance(): ExclusiveRekPremFragment {
            return ExclusiveRekPremFragment()
        }
    }

}