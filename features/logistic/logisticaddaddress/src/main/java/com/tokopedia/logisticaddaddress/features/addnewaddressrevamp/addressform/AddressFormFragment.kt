package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent

class AddressFormFragment : BaseDaggerFragment() {

    override fun getScreenName(): String  = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    companion object {

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

}