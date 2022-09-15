package com.tokopedia.tkpd.flashsale.presentation.avp

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
        fun start(context: Context, product: ReservedProduct.Product) {
            val intent = Intent(context, ManageProductVariantActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(BundleConstant.BUNDLE_KEY_PRODUCT, product)
            intent.putExtras(bundle)

            context.startActivity(intent)
        }
    }

    private val product by lazy {
        intent?.extras?.getParcelable<ReservedProduct.Product>(BundleConstant.BUNDLE_KEY_PRODUCT)
    }
    override fun getLayoutRes() = R.layout.stfs_activity_manage_product_variant
    override fun getNewFragment() = ManageProductVariantFragment.newInstance(product)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}