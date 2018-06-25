package com.tokopedia.topads.keyword.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewItemFragment

class TopAdsKeywordNewItemActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return TopAdsKeywordNewItemFragment.newInstance()
    }

}