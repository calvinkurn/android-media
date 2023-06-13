package com.tokopedia.mvc.presentation.creation.step2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.util.constant.BundleConstant

class VoucherInformationActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun buildCreateModeIntent(
            context: Context,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct> = emptyList()
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                putParcelableArrayList(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
            }
            val starter = Intent(context, VoucherInformationActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }

        fun buildEditModeIntent(
            context: Context,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct> = emptyList()
        ): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                putParcelableArrayList(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
            }

            val intent = Intent(context, VoucherInformationActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

    private val pageMode by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private val selectedProducts by lazy { intent?.extras?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS).orEmpty() }

    override fun getLayoutRes() = R.layout.smvc_activity_creation_voucher_information
    override fun getNewFragment() = VoucherInformationFragment.newInstance(
        pageMode ?: PageMode.CREATE,
        voucherConfiguration ?: VoucherConfiguration(),
        selectedProducts
    )
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_creation_voucher_information)
    }
}
