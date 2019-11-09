package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewFragment
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity


// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<AppComponent> {
    companion object {
        fun newInstance(context: Context) = Intent(context, CreateReviewActivity::class.java)
    }

    override fun getNewFragment(): Fragment {
        val productId: String
        val reputationId: String
        val bundle = intent.extras
        val uri = intent.data

        if (uri != null && uri.pathSegments.size > 0) {
            val uriSegment = uri.pathSegments

            productId = uri.lastPathSegment ?: ""
            reputationId = uriSegment[uriSegment.size - 2]
        } else {
            productId = bundle?.getString(InboxReputationFormActivity.ARGS_PRODUCT_ID) ?: ""
            reputationId = bundle?.getString(InboxReputationFormActivity.ARGS_REPUTATION_ID) ?: ""
        }

        return CreateReviewFragment.createInstance(
                productId,
                reputationId,
                bundle?.getInt(CreateReviewFragment.REVIEW_CLICK_AT, 0) ?: 0
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.run {
            (applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .cancel(getInt(CreateReviewFragment.REVIEW_NOTIFICATION_ID))
        }

        supportActionBar?.elevation = 0f
    }


    override fun getComponent(): AppComponent {
        return (application as MainApplication).appComponent
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            val intent = RouteManager.getIntent(this, ApplinkConst.HOME)

            setResult(Activity.RESULT_OK, intent)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }

        finish()
    }
}