package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_BUNDLE
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class PinpointNewPageActivity : BaseSimpleActivity(), HasComponent<AddNewAddressRevampComponent> {

    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }
    private var isEdit: Boolean? = false
    private var isGetPinPointOnly: Boolean? = false

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): PinpointNewPageFragment? {
        val bundle = intent.getBundleExtra(EXTRA_BUNDLE)
        var fragment: PinpointNewPageFragment? = null
        if (bundle != null) {
            isEdit = bundle.getBoolean(EXTRA_IS_EDIT)
            isEdit = bundle.getBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY)
            fragment = PinpointNewPageFragment.newInstance(bundle)
        }
        return fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isGetPinPointOnly == false) {
            if (isEdit == true) {
                EditAddressRevampAnalytics.onClickBackPinpoint(userSession.userId)
            } else {
                AddNewAddressRevampAnalytics.onClickBackArrowPinpoint(userSession.userId)
            }
        }
    }

    companion object {

        fun createIntent(content: Context, bundle: Bundle): Intent {
            return Intent(content, PinpointNewPageActivity::class.java).putExtra(EXTRA_BUNDLE, bundle)
        }
    }
}
