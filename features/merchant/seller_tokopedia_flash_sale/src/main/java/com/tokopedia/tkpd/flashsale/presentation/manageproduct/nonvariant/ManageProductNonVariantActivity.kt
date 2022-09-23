package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment.ChooseProductFragment
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant

class ManageProductNonVariantActivity : BaseSimpleActivity() {

    companion object {
        const val BUNDLE_KEY_PRODUCT = "KEY_PRODUCT"
        const val REQUEST_CODE_MANAGE_PRODUCT_NON_VARIANT = 1
        @JvmStatic
        fun createIntent(context: Context?, product: ReservedProduct.Product): Intent {
            return Intent(context, ManageProductNonVariantActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
                putExtras(bundle)
            }
        }
    }

    private val product by lazy {
        intent?.extras?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }

    override fun getLayoutRes() = R.layout.stfs_activity_flash_sale_list_container
    override fun getNewFragment() = ManageProductNonVariantFragment.newInstance(product)
    override fun getParentViewResourceID() = R.id.container
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_flash_sale_list_container)
    }
}