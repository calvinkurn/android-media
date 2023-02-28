package com.tokopedia.mvc.presentation.summary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.SharedPreferencesUtil
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.summary.fragment.SummaryFragment
import com.tokopedia.mvc.util.constant.BundleConstant

class SummaryActivity: BaseSimpleActivity() {

    companion object {
        fun start(
            context: Context?,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct> = emptyList(),
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                putParcelableArrayList(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
            }
            val starter = Intent(context, SummaryActivity::class.java)
                .putExtras(bundle)
            starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context?.startActivity(starter)
        }

        fun buildEditModeIntent(
            context: Context?,
            voucherId: Long
        ): Intent {
            SharedPreferencesUtil().setEditCouponSourcePage(context, context?.javaClass.toString())
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
            }
            val intent = Intent(context, SummaryActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }

        fun buildDuplicateModeIntent(
            context: Context?,
            voucherId: Long
        ): Intent {
            SharedPreferencesUtil().setEditCouponSourcePage(context, context?.javaClass.toString())
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
                putBoolean(BundleConstant.BUNDLE_KEY_ENABLE_DUPLICATE_VOUCHER, true)
            }
            val intent = Intent(context, SummaryActivity::class.java)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
    override fun getNewFragment(): SummaryFragment {
        val pageMode = intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode
        val voucherId = intent?.extras?.getLong(BundleConstant.BUNDLE_VOUCHER_ID).orZero()
        val voucherConfiguration = intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
        val selectedProducts = intent?.extras?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS).orEmpty()
        val enableDuplicateVoucher = intent?.extras?.getBoolean(BundleConstant.BUNDLE_KEY_ENABLE_DUPLICATE_VOUCHER, false)
        return SummaryFragment.newInstance(
            pageMode ?: PageMode.CREATE,
            voucherId,
            voucherConfiguration,
            selectedProducts,
            enableDuplicateVoucher.orFalse()
        )
    }
}
