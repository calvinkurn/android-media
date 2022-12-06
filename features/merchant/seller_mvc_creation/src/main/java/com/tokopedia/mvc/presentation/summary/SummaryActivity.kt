package com.tokopedia.mvc.presentation.summary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.summary.fragment.SummaryFragment
import com.tokopedia.mvc.util.constant.BundleConstant

class SummaryActivity: BaseSimpleActivity() {

    companion object {
        fun start(
            context: Context,
            voucherConfiguration: VoucherConfiguration
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
            }
            val starter = Intent(context, SummaryActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }

        fun buildEditModeIntent(
            context: Context?,
            voucherId: String
        ): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putString(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
            }
            val intent = Intent(context, SummaryActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
    override fun getNewFragment(): SummaryFragment {
        val pageMode = intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode
        val voucherId = intent?.extras?.getString(BundleConstant.BUNDLE_VOUCHER_ID)
        val voucherConfiguration = intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
        return SummaryFragment.newInstance(pageMode, voucherId, voucherConfiguration)
    }
}
