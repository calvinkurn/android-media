package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_BUNDLE
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class AddressFormActivity : BaseSimpleActivity(), HasComponent<AddNewAddressRevampComponent> {

    private var isPositiveFlow: Boolean? = true
    private var isEdit: Boolean = false
    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun getNewFragment(): Fragment? {
        var fragment: AddressFormFragment? = null
        if (intent.data?.lastPathSegment != null) {
            val addressId = intent.data?.lastPathSegment
            isEdit = true
            fragment = AddressFormFragment.newInstance(addressId = addressId)
        }
        else if (intent.extras != null) {
            val bundle = intent.extras
            isPositiveFlow = bundle?.getBoolean(EXTRA_IS_POSITIVE_FLOW)
            isEdit = false
            fragment = AddressFormFragment.newInstance(bundle?: Bundle())
        }
        return fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!isEdit) {
            if (isPositiveFlow == true) AddNewAddressRevampAnalytics.onClickBackPositive(userSession.userId)
            else AddNewAddressRevampAnalytics.onClickBackNegative(userSession.userId)
        } else {
            EditAddressRevampAnalytics.onClickBackArrowEditAddress(userSession.userId)
        }
    }

}