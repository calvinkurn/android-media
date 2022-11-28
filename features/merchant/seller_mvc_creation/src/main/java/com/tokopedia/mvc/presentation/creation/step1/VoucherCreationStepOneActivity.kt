package com.tokopedia.mvc.presentation.creation.step1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.util.constant.BundleConstant

class VoucherCreationStepOneActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, voucherId: Long) {
            val intent = Intent(context, VoucherCreationStepOneActivity::class.java)
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_voucher_creation_step_one
    override fun getNewFragment() = VoucherCreationStepOneFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_voucher_creation_step_one)
    }
}
