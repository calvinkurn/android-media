package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_FLASH_SALE_ID
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT

class ManageProductNonVariantActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, product: ReservedProduct.Product, campaignId: Long): Intent {
            return Intent(context, ManageProductNonVariantActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
                bundle.putLong(BUNDLE_FLASH_SALE_ID, campaignId)
                putExtras(bundle)
            }
        }
    }

    private val product by lazy {
        intent?.extras?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }
    private val campaignId by lazy {
        intent?.extras?.getLong(BUNDLE_FLASH_SALE_ID).orZero()
    }

    override fun getLayoutRes() = R.layout.stfs_activity_flash_sale_list_container
    override fun getNewFragment() = ManageProductNonVariantFragment.newInstance(product, campaignId)
    override fun getParentViewResourceID() = R.id.container
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_flash_sale_list_container)
    }
}