package com.tokopedia.mvc.presentation.product.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.util.constant.BundleConstant

class ProductListActivity : BaseSimpleActivity() {

    companion object {
        fun start(
            context: Context,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct>,
            selectedWarehouseId: Long
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelableArrayList(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    ArrayList(selectedProducts)
                )
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                putLong(BundleConstant.BUNDLE_KEY_SELECTED_WAREHOUSE_ID, selectedWarehouseId)
            }
            val starter = Intent(context, ProductListActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }

        fun buildIntentForVoucherSummaryPage(
            context: Context,
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration,
            showCtaChangeProductOnToolbar: Boolean,
            selectedProducts: List<SelectedProduct>,
            selectedWarehouseId: Long
        ): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                putParcelableArrayList(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    ArrayList(selectedProducts)
                )
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                putBoolean(BundleConstant.BUNDLE_KEY_SHOW_CTA_CHANGE_PRODUCT_ON_TOOLBAR, showCtaChangeProductOnToolbar)
                putBoolean(BundleConstant.BUNDLE_KEY_IS_ENTRY_POINT_FROM_VOUCHER_SUMMARY_PAGE, true)
                putLong(BundleConstant.BUNDLE_KEY_SELECTED_WAREHOUSE_ID, selectedWarehouseId)
            }

            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }

        fun buildIntentForVoucherDetailPage(
            context: Context,
            showCtaChangeProductOnToolbar: Boolean,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct>,
            selectedWarehouseId: Long
        ): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putParcelableArrayList(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    ArrayList(selectedProducts)
                )
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                putBoolean(BundleConstant.BUNDLE_KEY_SHOW_CTA_CHANGE_PRODUCT_ON_TOOLBAR, showCtaChangeProductOnToolbar)
                putBoolean(BundleConstant.BUNDLE_KEY_IS_ENTRY_POINT_FROM_VOUCHER_SUMMARY_PAGE, false)
                putLong(BundleConstant.BUNDLE_KEY_SELECTED_WAREHOUSE_ID, selectedWarehouseId)
            }

            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

    private val pageMode by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode ?: PageMode.CREATE }
    private val selectedProducts by lazy {
        intent?.extras?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS)
    }
    private val voucherConfiguration by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration ?: VoucherConfiguration() }
    private val showCtaChangeProductOnToolbar by lazy { intent?.extras?.getBoolean(BundleConstant.BUNDLE_KEY_SHOW_CTA_CHANGE_PRODUCT_ON_TOOLBAR).orFalse() }
    private val isEntryPointFromVoucherSummaryPage by lazy { intent?.extras?.getBoolean(BundleConstant.BUNDLE_KEY_IS_ENTRY_POINT_FROM_VOUCHER_SUMMARY_PAGE).orFalse() }
    private val selectedWarehouseId by lazy { intent?.extras?.getLong(BundleConstant.BUNDLE_KEY_SELECTED_WAREHOUSE_ID).orZero() }

    override fun getLayoutRes() = R.layout.smvc_activity_product_list
    override fun getParentViewResourceID() = R.id.container
    override fun getNewFragment(): Fragment {
        val products = (selectedProducts as? List<SelectedProduct>).orEmpty()

        return ProductListFragment.newInstance(
            pageMode ,
            voucherConfiguration,
            products,
            showCtaChangeProductOnToolbar,
            isEntryPointFromVoucherSummaryPage,
            selectedWarehouseId
        )
    }

}
