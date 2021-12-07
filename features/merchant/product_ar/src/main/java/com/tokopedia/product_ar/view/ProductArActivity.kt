package com.tokopedia.product_ar.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ProductArActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = ProductArFragment.newInstance()
}