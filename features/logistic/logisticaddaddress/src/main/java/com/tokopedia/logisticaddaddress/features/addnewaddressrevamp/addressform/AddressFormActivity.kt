package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
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
        if (intent.data?.lastPathSegment != null) {
            val addressId = intent.data?.lastPathSegment
            fragment = AddressFormFragment.newInstance(addressId = addressId, bundle)
        } else if (bundle != null) {
            fragment = AddressFormFragment.newInstance(bundle)
        }
        return fragment
    }
}
