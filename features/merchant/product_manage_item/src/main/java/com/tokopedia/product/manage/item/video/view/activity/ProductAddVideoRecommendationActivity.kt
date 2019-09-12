package com.tokopedia.product.manage.item.video.view.activity

import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoRecommendationFragment

class ProductAddVideoRecommendationActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment{
        return ProductAddVideoRecommendationFragment.createInstance()
    }
}
