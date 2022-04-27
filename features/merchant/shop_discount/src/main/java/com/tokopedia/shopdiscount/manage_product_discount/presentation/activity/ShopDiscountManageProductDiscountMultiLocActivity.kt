package com.tokopedia.shopdiscount.manage_product_discount.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageProductDiscountMultiLocFragment

class ShopDiscountManageProductDiscountMultiLocActivity : BaseSimpleActivity() {

    companion object {
        const val MODE_PARAM = "MODE_PARAM"
    }

    private var mode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_manage_product_discount_multi_loc
    }

    override fun getNewFragment(): Fragment = ShopDiscountManageProductDiscountMultiLocFragment.createInstance(mode)

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    private fun getIntentData() {
        mode = intent.extras?.getString(ShopDiscountManageProductDiscountActivity.MODE_PARAM).orEmpty()
    }

}