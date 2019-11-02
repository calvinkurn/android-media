package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewFragment


// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<AppComponent> {
    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, CreateReviewActivity::class.java)
        }
    }

    override fun getNewFragment(): Fragment {
        var productId = ""
        var reputationId = ""
        var reviewClickAt = 0
        val bundle = intent.extras
        val uri = intent.data

        bundle?.let {
            reviewClickAt = it.getInt("REVIEW_CLICK_AT", 0)
        }

        uri?.let {
            val uriSegment = it.pathSegments
            productId = it.lastPathSegment ?: ""
            reputationId = uriSegment[uriSegment.size - 1]
        }

        return CreateReviewFragment.createInstance(productId, reputationId, reviewClickAt)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mNotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(777)
        supportActionBar?.let {
            it.elevation = 0f
        }
    }


    override fun getComponent(): AppComponent {
        return (application as MainApplication).appComponent
    }

}