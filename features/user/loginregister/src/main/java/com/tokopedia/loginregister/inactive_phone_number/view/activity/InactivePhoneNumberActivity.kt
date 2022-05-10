package com.tokopedia.loginregister.inactive_phone_number.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.inactive_phone_number.di.DaggerInactivePhoneNumberComponent
import com.tokopedia.loginregister.inactive_phone_number.di.InactivePhoneNumberComponent
import com.tokopedia.loginregister.inactive_phone_number.view.fragment.InactivePhoneNumberFragment

class InactivePhoneNumberActivity : BaseSimpleActivity(),
    HasComponent<InactivePhoneNumberComponent> {

    private var inactivePhoneNumberComponent: InactivePhoneNumberComponent? = null

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return InactivePhoneNumberFragment.newInstance(bundle)
    }

    override fun getLayoutRes(): Int = R.layout.activity_inactive_phone_number

    override fun getComponent(): InactivePhoneNumberComponent =
        inactivePhoneNumberComponent ?: initializeInactivePhoneNumber()

    private fun initializeInactivePhoneNumber(): InactivePhoneNumberComponent {
        val loginRegisterComponent = DaggerLoginRegisterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
        return DaggerInactivePhoneNumberComponent
            .builder()
            .loginRegisterComponent(loginRegisterComponent)
            .build().also {
                inactivePhoneNumberComponent = it
            }
    }

    override fun getToolbarResourceID(): Int = R.id.toolbar
}