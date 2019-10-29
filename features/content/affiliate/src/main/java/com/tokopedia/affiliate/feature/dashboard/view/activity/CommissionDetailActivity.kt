package com.tokopedia.affiliate.feature.dashboard.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.feature.dashboard.view.fragment.CommissionDetailFragment

/**
 * @author by yoasfs on 2019-08-12
 */
class CommissionDetailActivity: BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context, affPostId: String): Intent {
            val intent = Intent(context, CommissionDetailActivity::class.java)
            intent.putExtra(CommissionDetailFragment.PARAM_AFF_ID, affPostId)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        return CommissionDetailFragment.newInstance(intent.extras ?: Bundle())
    }
}