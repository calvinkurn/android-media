package com.tokopedia.feedplus.profilerecommendation.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.profilerecommendation.view.fragment.FollowRecommendationFragment

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationActivity : BaseSimpleActivity() {

    companion object {
        const val EXTRA_INTEREST_IDS = FollowRecommendationFragment.EXTRA_INTEREST_IDS
    }

    override fun getNewFragment(): Fragment? {
        return FollowRecommendationFragment.newInstance(intent?.getIntArrayExtra(EXTRA_INTEREST_IDS) ?: intArrayOf())
    }
}