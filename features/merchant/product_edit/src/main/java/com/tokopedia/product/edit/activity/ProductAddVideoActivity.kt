package com.tokopedia.product.edit.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.fragment.ProductAddVideoFragment

class ProductAddVideoActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return ProductAddVideoFragment.createInstance()
    }
}
