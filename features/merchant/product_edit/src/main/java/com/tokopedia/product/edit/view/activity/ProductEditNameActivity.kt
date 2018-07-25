package com.tokopedia.product.edit.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.price.ProductEditNameFragment
import com.tokopedia.product.edit.R

class ProductEditNameActivity : BaseSimpleActivity(){

    override fun getNewFragment(): Fragment{
        return ProductEditNameFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
