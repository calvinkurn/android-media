package com.tokopedia.feedplus.profilerecommendation.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.profilerecommendation.view.fragment.FollowRecomFragment

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecomActivity : BaseSimpleActivity() {

    companion object {
        private const val EXTRA_INTEREST_IDS = FollowRecomFragment.EXTRA_INTEREST_IDS

        @JvmStatic
        fun createIntent(context: Context, interestIds: IntArray): Intent {
            return Intent(context, FollowRecomActivity::class.java)
                    .putExtra(EXTRA_INTEREST_IDS, interestIds)
        }
    }

    override fun getNewFragment(): Fragment? {
        return FollowRecomFragment.newInstance(intent?.getIntArrayExtra(EXTRA_INTEREST_IDS) ?: intArrayOf())
    }
}