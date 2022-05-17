package com.tokopedia.updateinactivephone.features.inputoldphone.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.di.InactivePhoneComponentBuilder
import com.tokopedia.updateinactivephone.features.inputoldphone.fragment.InputOldPhoneNumberFragment

class InputOldPhoneNumberActivity : BaseSimpleActivity(),
    HasComponent<InactivePhoneComponent> {

    private var inactivePhoneNumberComponent: InactivePhoneComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return InputOldPhoneNumberFragment.newInstance(bundle)
    }

    override fun getComponent(): InactivePhoneComponent =
        inactivePhoneNumberComponent ?: initializeInactivePhoneNumber()

    private fun initializeInactivePhoneNumber(): InactivePhoneComponent {
        return InactivePhoneComponentBuilder.getComponent(application)
    }
}