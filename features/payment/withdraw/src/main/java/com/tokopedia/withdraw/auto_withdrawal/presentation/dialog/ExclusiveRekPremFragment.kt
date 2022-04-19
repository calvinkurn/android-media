package com.tokopedia.withdraw.auto_withdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.analytics.AutoWithdrawAnalytics
import com.tokopedia.withdraw.auto_withdrawal.di.component.AutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.di.component.DaggerAutoWithdrawalComponent
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import javax.inject.Inject

class ExclusiveRekPremFragment : BottomSheetUnify() {

    @Inject
    lateinit var analytics: dagger.Lazy<AutoWithdrawAnalytics>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        val rootView = LayoutInflater.from(context)
                .inflate(R.layout.swd_layout_awd_exclusive_rp, null, false)
        rootView.apply {
            setChild(this)
            findViewById<View>(R.id.btnJoinPremiumProgramFree).setOnClickListener {
                WithdrawConstant.openRekeningAccountInfoPage(context)
                if(::analytics.isInitialized){
                    analytics.get().onOpenJoinRekeningBottomSheetClick()
                }
                dismiss()
            }
        }
    }



    private fun initInjector() {
        activity?.let {
            DaggerAutoWithdrawalComponent.builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication)
                            .baseAppComponent)
                    .build()
                    .inject(this)
        }
    }


    companion object {
        private const val TAG_BOTTOM_SHEET = "ExclusiveRekPremFragment"
        fun show(fragmentManager: FragmentManager)  {
            val exclusiveRekPremFragment =  ExclusiveRekPremFragment()
            exclusiveRekPremFragment.show(fragmentManager, TAG_BOTTOM_SHEET)
        }
    }

}