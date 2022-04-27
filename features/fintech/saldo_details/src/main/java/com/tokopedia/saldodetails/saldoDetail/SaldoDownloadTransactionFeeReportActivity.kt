package com.tokopedia.saldodetails.saldoDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.saldoDetail.SaldoDepositFragment.Companion.REQUEST_WITHDRAW_CODE
import com.tokopedia.saldodetails.saldoDetail.coachmark.SaldoCoachMarkListener
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT]
 */


class SaldoDownloadTransactionFeeReportActivity : BaseSimpleActivity(){

    override fun getNewFragment(): Fragment {
        return SaldoDownloadTransactionFeeReportFragment.createInstance()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initializeView()
    }
    private fun initializeView() {

    }

    override fun getTagFragment() = TAG
    override fun getScreenName() = null

    companion object {
        private val TAG = "DEPOSIT_FRAGMENT"

    }
}
