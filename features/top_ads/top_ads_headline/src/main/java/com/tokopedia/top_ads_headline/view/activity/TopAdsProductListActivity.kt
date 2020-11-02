package com.tokopedia.top_ads_headline.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.top_ads_headline.view.fragment.TopAdsProductListFragment

class TopAdsProductListActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return TopAdsProductListFragment.newInstance()
    }

}