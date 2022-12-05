package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant

class ManageProductVariantActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, product: ReservedProduct.Product, flashSaleId: String): Intent {
            return Intent(context, ManageProductVariantActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelable(BundleConstant.BUNDLE_KEY_PRODUCT, product)
                bundle.putString(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
                putExtras(bundle)
            }
        }
    }

    private val product by lazy {
        intent?.extras?.getParcelable<ReservedProduct.Product>(BundleConstant.BUNDLE_KEY_PRODUCT)
    }

    private val campaignId by lazy {
        intent.extras?.getString(BundleConstant.BUNDLE_FLASH_SALE_ID).orEmpty()
    }

    override fun getLayoutRes() = R.layout.stfs_activity_manage_product_variant
    override fun getNewFragment() = ManageProductVariantFragment.newInstance(product, campaignId)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
