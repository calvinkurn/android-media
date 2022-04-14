package com.tokopedia.shopdiscount.manage_product_discount.presentation.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.presentation.view.fragment.ShopDiscountManageDiscountFragment
import com.tokopedia.shopdiscount.manage_product_discount.presentation.view.fragment.ShopDiscountManageProductDiscountFragment

class ShopDiscountManageProductDiscountActivity : BaseSimpleActivity() {

    private var productData: ShopDiscountSetupProductUiModel.SetupProductData = ShopDiscountSetupProductUiModel.SetupProductData()
    private var mode: String = ""

    companion object {
        const val MODE_PARAM = "MODE_PARAM"
        const val PRODUCT_MANAGE_UI_MODEL = "PRODUCT_MANAGE_UI_MODEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

//    override fun onBackPressed() {
//        (fragment as? ShopDiscountManageDiscountFragment)?.onBackPressed()
//    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_manage_product_discount
    }

    override fun getNewFragment(): Fragment = ShopDiscountManageProductDiscountFragment.createInstance()

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    private fun getIntentData() {
        productData = intent.extras?.getParcelable(PRODUCT_MANAGE_UI_MODEL) ?: ShopDiscountSetupProductUiModel.SetupProductData()
        mode = intent.extras?.getString(MODE_PARAM).orEmpty()
    }
}