package com.tokopedia.shopdiscount.manage_product_discount.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageProductVariantDiscountFragment

class ShopDiscountManageProductVariantDiscountActivity : BaseSimpleActivity() {

    private var productData: ShopDiscountSetupProductUiModel.SetupProductData = ShopDiscountSetupProductUiModel.SetupProductData()
    private var mode: String = ""

    companion object {
        const val MODE_PARAM = "MODE_PARAM"
        const val PRODUCT_MANAGE_UI_MODEL = "PRODUCT_MANAGE_UI_MODEL"
        const val REQUEST_CODE_APPLY_PRODUCT_VARIANT_DISCOUNT = 11
        const val PRODUCT_DATA_RESULT = "product_data_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        (fragment as? ShopDiscountManageProductVariantDiscountFragment)?.onBackPressed()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_manage_product_variant_discount
    }

    override fun getNewFragment(): Fragment = ShopDiscountManageProductVariantDiscountFragment.createInstance(mode, productData)

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    private fun getIntentData() {
        mode = intent.extras?.getString(MODE_PARAM).orEmpty()
        productData = intent.extras?.getParcelable(PRODUCT_MANAGE_UI_MODEL) ?: ShopDiscountSetupProductUiModel.SetupProductData()
    }
}