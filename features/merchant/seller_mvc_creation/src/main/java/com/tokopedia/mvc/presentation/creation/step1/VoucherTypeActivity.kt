package com.tokopedia.mvc.presentation.creation.step1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherType
import com.tokopedia.mvc.util.constant.BundleConstant

class VoucherTypeActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun buildCreateModeIntent(
            context: Context,
            voucherConfiguration: VoucherConfiguration
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
            }
            val starter = Intent(context, VoucherTypeActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }

        fun buildEditModeIntent(
            context: Context,
            voucherConfiguration: VoucherConfiguration
        ): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
            }

            val intent = Intent(context, VoucherTypeActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }

        private const val VOUCHER_TYPE_SEGMENT = 2
    }

    private val pageMode by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }

    private val isVoucherProduct by lazy {
        val appLinkData = RouteManager.getIntent(this, intent?.data.toString()).data
        if (appLinkData?.pathSegments?.isNotEmpty() == true) {
            appLinkData.pathSegments?.getOrNull(VOUCHER_TYPE_SEGMENT) == VoucherType.PRODUCT.type
        } else {
            voucherConfiguration?.isVoucherProduct
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_creation_voucher_type
    override fun getNewFragment() = VoucherTypeFragment.newInstance(
        pageMode ?: PageMode.CREATE,
        voucherConfiguration ?: VoucherConfiguration(isVoucherProduct = isVoucherProduct ?: false)
    )

    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_creation_voucher_type)
    }
}
