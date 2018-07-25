package com.tokopedia.product.edit.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.price.ProductEditPriceFragment

class ProductEditPriceActivity : BaseSimpleActivity(){


    override fun getNewFragment(): Fragment{
        return ProductEditPriceFragment.createInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
