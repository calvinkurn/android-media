package com.tokopedia.mvc.presentation.creation.step2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.creation.step1.VoucherCreationStepOneFragment

class VoucherCreationStepTwoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, VoucherCreationStepTwoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_voucher_creation_step_two
    override fun getNewFragment() = VoucherCreationStepOneFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_voucher_creation_step_two)
    }
}
