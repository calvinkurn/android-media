package com.tokopedia.salam.umrah.orderdetail.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.orderdetail.di.DaggerUmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.fragment.UmrahOrderDetailFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UmrahOrderDetailActivity : BaseSimpleActivity(), HasComponent<UmrahOrderDetailComponent> {

    private lateinit var orderId: String

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var trackingUmrahUtil: UmrahTrackingAnalytics

    override fun getComponent(): UmrahOrderDetailComponent =
            DaggerUmrahOrderDetailComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun getNewFragment(): Fragment? =
            UmrahOrderDetailFragment.getInstance(orderId)

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            orderId = uri.lastPathSegment
        } else if (savedInstanceState != null) {
            orderId = savedInstanceState.getString(KEY_ORDER_ID, "")
        }

        component.inject(this)
        if (!userSession.isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_ORDER_ID, orderId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_LOGIN -> if (userSession.isLoggedIn) recreate() else finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        trackingUmrahUtil.umrahOrderDetailBack()
    }

    companion object {
        const val KEY_ORDER_ID = "KEY_ORDER_ID"
        private const val REQUEST_CODE_LOGIN = 6

    }

}
