package com.tokopedia.product.edit.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.price.BaseProductAddFragment

class BaseProductAddActivity : BaseSimpleActivity(){


    override fun getNewFragment(): Fragment{
        return BaseProductAddFragment.createInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
