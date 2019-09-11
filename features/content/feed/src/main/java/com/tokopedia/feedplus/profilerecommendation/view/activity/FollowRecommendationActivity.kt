package com.tokopedia.feedplus.profilerecommendation.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.profilerecommendation.view.fragment.FollowRecommendationFragment

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return FollowRecommendationFragment.newInstance()
    }
}