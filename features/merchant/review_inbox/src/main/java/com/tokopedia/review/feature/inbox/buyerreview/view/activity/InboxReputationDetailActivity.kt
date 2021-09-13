package com.tokopedia.review.feature.inbox.buyerreview.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationDetailFragment
import java.util.*

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailActivity constructor() : BaseSimpleActivity(), HasComponent<Any?> {
    private var reputationTracking: ReputationTracking? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reputationTracking = ReputationTracking()
    }

    override fun getNewFragment(): Fragment? {
        var tab: Int = -1
        var isFromApplink: Boolean = false
        var reputationId: String? = DEFAULT_REPUTATION_ID
        val intentData: Uri? = getIntent().getData()
        val intentExtras: Bundle? = getIntent().getExtras()

        // if from applink
        if (intentData != null && intentData.getPathSegments().size >= 2) {
            isFromApplink = true
            reputationId = intentData.getPathSegments().get(1)
        }
        if (intentExtras != null && !isFromApplink) {
            if (intentExtras.getInt(ARGS_TAB, -1) != -1) {
                tab = intentExtras.getInt(ARGS_TAB)
            }
            reputationId = intentExtras.getString(REPUTATION_ID, "")
            isFromApplink = intentExtras.getBoolean(ARGS_IS_FROM_APPLINK, false)
        }
        return InboxReputationDetailFragment.Companion.createInstance(
            tab,
            isFromApplink,
            reputationId
        )
    }

    public override fun getComponent(): BaseAppComponent {
        return (getApplication() as BaseMainApplication).getBaseAppComponent()
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (getFragment() != null && getFragment() is InboxReputationDetailFragment) {
            val fragment: InboxReputationDetailFragment? =
                getFragment() as InboxReputationDetailFragment?
            reputationTracking!!.onClickBackButtonReputationDetailTracker(
                Objects.requireNonNull(
                    fragment
                ).getOrderId()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val ARGS_POSITION: String = "ARGS_POSITION"
        val ARGS_TAB: String = "ARGS_TAB"
        val ARGS_IS_FROM_APPLINK: String = "ARGS_IS_FROM_APPLINK"
        val REPUTATION_ID: String = "reputation_id"
        val CACHE_PASS_DATA: String =
            InboxReputationDetailActivity::class.java.getName() + "-passData"
        val DEFAULT_REPUTATION_ID: String = "0"
        fun getCallingIntent(
            context: Context?,
            adapterPosition: Int, tab: Int
        ): Intent {
            val intent: Intent = Intent(context, InboxReputationDetailActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putInt(ARGS_POSITION, adapterPosition)
            bundle.putInt(ARGS_TAB, tab)
            intent.putExtras(bundle)
            return intent
        }

        fun getCallingIntent(
            context: Context?,
            reputationId: String?
        ): Intent {
            val intent: Intent = Intent(context, InboxReputationDetailActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putString(REPUTATION_ID, reputationId)
            bundle.putBoolean(ARGS_IS_FROM_APPLINK, true)
            intent.putExtras(bundle)
            return intent
        }
    }
}