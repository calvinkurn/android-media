package com.tokopedia.mvc.presentation.quota

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.presentation.quota.fragment.QuotaInfoFragment
import com.tokopedia.mvc.util.constant.BundleConstant

class QuotaInfoActivity: BaseSimpleActivity() {

    companion object {
        fun start(
            context: Context?,
            voucherCreationQuota: VoucherCreationQuota?
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_QUOTA, voucherCreationQuota)
            }
            val starter = Intent(context, QuotaInfoActivity::class.java).putExtras(bundle)
            context?.startActivity(starter)
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
    override fun getNewFragment(): QuotaInfoFragment {
        val voucherCreationQuota = intent?.extras?.getParcelable(
            BundleConstant.BUNDLE_KEY_VOUCHER_QUOTA
        ) as? VoucherCreationQuota
        return QuotaInfoFragment.newInstance(voucherCreationQuota)
    }
}
