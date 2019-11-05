package com.tokopedia.product.detail.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment

class DynamicProductDetailActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = DynamicProductDetailFragment()

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

}