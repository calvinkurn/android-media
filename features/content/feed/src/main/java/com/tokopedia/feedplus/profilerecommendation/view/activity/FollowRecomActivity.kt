package com.tokopedia.feedplus.profilerecommendation.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.profilerecommendation.view.fragment.FollowRecomFragment

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecomActivity : BaseSimpleActivity() {

    companion object {
        const val EXTRA_INTEREST_IDS = FollowRecomFragment.EXTRA_INTEREST_IDS
    }

    override fun getNewFragment(): Fragment? {
        return FollowRecomFragment.newInstance(intent?.getIntArrayExtra(EXTRA_INTEREST_IDS) ?: intArrayOf())
    }
}