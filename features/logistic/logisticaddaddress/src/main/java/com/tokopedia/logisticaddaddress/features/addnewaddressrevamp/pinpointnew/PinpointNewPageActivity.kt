package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_BUNDLE
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class PinpointNewPageActivity: BaseSimpleActivity(), HasComponent<AddNewAddressRevampComponent> {

    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun getNewFragment(): PinpointNewPageFragment? {
        val bundle = intent.getBundleExtra(EXTRA_BUNDLE)
        var fragment: PinpointNewPageFragment? = null
        if (bundle != null) {
            fragment = PinpointNewPageFragment.newInstance(bundle)
        }
        return fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AddNewAddressRevampAnalytics.onClickBackArrowPinpoint(userSession.userId)
    }

    companion object{

        fun createIntent(content: Context, bundle: Bundle): Intent {
            return Intent(content, PinpointNewPageActivity::class.java).putExtra(EXTRA_BUNDLE, bundle)
        }
    }

}