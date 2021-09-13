package com.tokopedia.review.feature.inbox.buyerreview.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationReportFragment

/**
 * @author by nisie on 9/13/17.
 */
class InboxReputationReportActivity : BaseSimpleActivity(), HasComponent<Any?> {
    override fun getNewFragment(): Fragment? {
        val reviewId: String = intent.extras!!
            .getString(ARGS_REVIEW_ID, "")
        val shopId: String? = intent.extras!!.getString(ARGS_SHOP_ID)
        return InboxReputationReportFragment.createInstance(reviewId, shopId)
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val ARGS_SHOP_ID: String = "ARGS_SHOP_ID"
        val ARGS_REVIEW_ID: String = "ARGS_REVIEW_ID"
        fun getCallingIntent(context: Context?, shopId: Long, reviewId: String?): Intent {
            val intent: Intent = Intent(context, InboxReputationReportActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putString(ARGS_SHOP_ID, shopId.toString())
            bundle.putString(ARGS_REVIEW_ID, reviewId)
            intent.putExtras(bundle)
            return intent
        }
    }
}