package com.tokopedia.logisticaddaddress.features.addeditaddress.addressform

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.toAddressUiState
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.di.addeditaddress.AddEditAddressComponent
import com.tokopedia.logisticaddaddress.di.addeditaddress.DaggerAddEditAddressComponent

class AddressFormActivity : BaseSimpleActivity(), HasComponent<AddEditAddressComponent> {

    var fragment: AddressFormFragment? = null

    override fun getComponent(): AddEditAddressComponent {
        return DaggerAddEditAddressComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fragment?.onNewIntent(intent)
    }

    override fun getNewFragment(): Fragment? {
        val bundle = intent.extras

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
