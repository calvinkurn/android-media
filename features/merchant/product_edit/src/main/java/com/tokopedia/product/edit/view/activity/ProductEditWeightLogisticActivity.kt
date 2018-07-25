package com.tokopedia.product.edit.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.ProductEditWeightLogisticFragment

class ProductEditWeightLogisticActivity : BaseSimpleActivity(){


    override fun getNewFragment(): Fragment{
        return ProductEditWeightLogisticFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
