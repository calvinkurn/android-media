package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_BUNDLE

class AddressFormActivity : BaseSimpleActivity(), HasComponent<AddNewAddressRevampComponent> {

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = intent.getBundleExtra(EXTRA_BUNDLE)
        var fragment: AddressFormFragment? = null
        if (bundle != null) {
            fragment = AddressFormFragment.newInstance(bundle)
        }
        return fragment
    }

    companion object {
        fun createIntent(context: Context, bundle: Bundle): Intent {
            return Intent(context, AddressFormActivity::class.java).putExtra(EXTRA_BUNDLE, bundle)
        }
    }



}