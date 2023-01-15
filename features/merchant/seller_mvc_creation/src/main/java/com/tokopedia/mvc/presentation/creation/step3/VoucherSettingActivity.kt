package com.tokopedia.mvc.presentation.creation.step3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.util.constant.BundleConstant

class VoucherSettingActivity : BaseSimpleActivity()  {

    companion object {
        @JvmStatic
        fun start(
            context: Context,
            voucherConfiguration: VoucherConfiguration
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
            }
            val starter = Intent(context, VoucherSettingActivity::class.java)
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

            val intent = Intent(context, VoucherSettingActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

    private val pageMode by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }

    override fun getLayoutRes() = R.layout.smvc_activity_creation_voucher_setting
    override fun getNewFragment() = VoucherSettingFragment.newInstance(
        pageMode ?: PageMode.CREATE,
        voucherConfiguration ?: VoucherConfiguration()
    )
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_creation_voucher_setting)
    }
}
