package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.constant.WithdrawConstant
import com.tokopedia.withdraw.saldowithdrawal.di.component.DaggerWithdrawComponent
import kotlinx.android.synthetic.main.swd_dialog_disabled_bank_account.*
import javax.inject.Inject

class DisabledAccountBottomSheet : BottomSheetUnify() {
    private val childLayout = R.layout.swd_dialog_disabled_bank_account

    @Inject
    lateinit var userSession: dagger.Lazy<UserSession>

    private val childView: View by lazy(LazyThreadSafetyMode.NONE) {
        LayoutInflater.from(context).inflate(childLayout, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChild(childView)
        childView.findViewById<View>(R.id.btnCheckPremiumProgram).setOnClickListener {
            WithdrawConstant.openRekeningAccountInfoPage(context, userSession.get())
        }
    }

    private fun initInjector() {
        activity?.let {
            DaggerWithdrawComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build().inject(this)
        } ?: run {
            dismiss()
        }
    }


}