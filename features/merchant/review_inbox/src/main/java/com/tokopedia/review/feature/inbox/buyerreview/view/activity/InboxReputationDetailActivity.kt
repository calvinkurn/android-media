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
import javax.inject.Inject

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    @Inject
    lateinit var reputationTracking: ReputationTracking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reputationTracking = ReputationTracking()
    }

    override fun getNewFragment(): Fragment {
        var tab: Int = -1
        var isFromApplink = false
        var reputationId: String? = DEFAULT_REPUTATION_ID
        val intentData: Uri? = intent.data
        val intentExtras: Bundle? = intent.extras

        // if from applink
        if (intentData != null && intentData.pathSegments.size >= 2) {
            isFromApplink = true
            reputationId = intentData.pathSegments.get(1)
        }
        if (intentExtras != null && !isFromApplink) {
            if (intentExtras.getInt(ARGS_TAB, -1) != -1) {
                tab = intentExtras.getInt(ARGS_TAB)
            }
            reputationId = intentExtras.getString(REPUTATION_ID, "")
            isFromApplink = intentExtras.getBoolean(ARGS_IS_FROM_APPLINK, false)
        }
        return InboxReputationDetailFragment.createInstance(
            tab,
            isFromApplink,
            reputationId
        )
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (fragment != null && fragment is InboxReputationDetailFragment) {
            val fragment: InboxReputationDetailFragment? =
                fragment as InboxReputationDetailFragment?
            reputationTracking.onClickBackButtonReputationDetailTracker(
                Objects.requireNonNull(
                    fragment
                )?.orderId
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARGS_POSITION: String = "ARGS_POSITION"
        const val ARGS_TAB: String = "ARGS_TAB"
        const val ARGS_IS_FROM_APPLINK: String = "ARGS_IS_FROM_APPLINK"
        const val REPUTATION_ID: String = "reputation_id"
        val CACHE_PASS_DATA = InboxReputationDetailActivity::class.java.name + "-passData"
        const val DEFAULT_REPUTATION_ID: String = "0"

        fun getCallingIntent(
            context: Context?,
            adapterPosition: Int, tab: Int
        ): Intent {
            val intent = Intent(context, InboxReputationDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(ARGS_POSITION, adapterPosition)
            bundle.putInt(ARGS_TAB, tab)
            intent.putExtras(bundle)
            return intent
        }

        fun getCallingIntent(
            context: Context?,
            reputationId: String?
        ): Intent {
            val intent = Intent(context, InboxReputationDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString(REPUTATION_ID, reputationId)
            bundle.putBoolean(ARGS_IS_FROM_APPLINK, true)
            intent.putExtras(bundle)
            return intent
        }
    }
}