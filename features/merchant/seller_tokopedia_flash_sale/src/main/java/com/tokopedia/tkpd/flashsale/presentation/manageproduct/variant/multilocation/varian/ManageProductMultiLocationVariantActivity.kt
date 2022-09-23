package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.VARIANT_POSITION

class ManageProductMultiLocationVariantActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, product: ReservedProduct.Product, position: Int) : Intent{
            val intent = Intent(context, ManageProductMultiLocationVariantActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
            bundle.putInt(VARIANT_POSITION, position)
            intent.putExtras(bundle)
            return intent
        }
    }

    private val product by lazy {
        intent?.extras?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }

    private val variantPositionOnProduct by lazy {
        intent?.extras?.getInt(VARIANT_POSITION).toZeroIfNull()
    }
    override fun getLayoutRes() = R.layout.stfs_activity_manage_product_multi_location_variant
    override fun getNewFragment() =
        ManageProductMultiLocationVariantFragment.newInstance(product, variantPositionOnProduct)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun sendResultToRequester(){
        var intentResult = Intent()
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            intentResult = (it as ManageProductMultiLocationVariantFragment).getIntentResult()
        }
        setResult(Activity.RESULT_OK, intentResult)
        finish()
    }
}