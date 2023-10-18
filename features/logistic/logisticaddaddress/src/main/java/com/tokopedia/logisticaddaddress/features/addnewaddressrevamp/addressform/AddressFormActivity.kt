package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.toAddressUiState
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent

class AddressFormActivity : BaseSimpleActivity(), HasComponent<AddNewAddressRevampComponent> {

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = intent.extras
        var fragment: AddressFormFragment? = null

        if (getAddressUiState(bundle).isAdd() && bundle != null) {
            fragment = AddressFormFragment.newInstance(bundle)
        } else {
            if (intent.data?.lastPathSegment != null) {
                val addressId = intent.data?.lastPathSegment
                fragment = AddressFormFragment.newInstance(addressId = addressId, bundle)
            } else if (bundle != null) {
                fragment = AddressFormFragment.newInstance(bundle)
            }
        }

        return fragment
    }

    private fun getAddressUiState(bundle: Bundle?): AddressUiState? {
        val addressUiStateBundle = bundle?.getString(EXTRA_ADDRESS_STATE)
        return if (addressUiStateBundle?.isNotEmpty() == true) {
            addressUiStateBundle.toAddressUiState()
        } else {
            null
        }
    }
}
