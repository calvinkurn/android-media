package com.tokopedia.product.manage.item.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.view.fragment.ProductAddVideoRecommendationFragment

class ProductAddVideoRecommendationActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment{
        return ProductAddVideoRecommendationFragment.createInstance()
    }
}
