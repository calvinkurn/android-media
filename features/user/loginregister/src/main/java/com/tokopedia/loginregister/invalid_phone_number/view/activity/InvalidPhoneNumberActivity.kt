package com.tokopedia.loginregister.invalid_phone_number.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.invalid_phone_number.di.DaggerInvalidPhoneNumberComponent
import com.tokopedia.loginregister.invalid_phone_number.di.InvalidPhoneNumberComponent
import com.tokopedia.loginregister.invalid_phone_number.view.fragment.InvalidPhoneNumberFragment

class InvalidPhoneNumberActivity : BaseSimpleActivity(), HasComponent<InvalidPhoneNumberComponent> {

    private var invalidPhoneNumberComponent: InvalidPhoneNumberComponent? = null

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return InvalidPhoneNumberFragment.newInstance(bundle)
    }

    override fun getLayoutRes(): Int = R.layout.activity_invalid_phone_number

    override fun getComponent(): InvalidPhoneNumberComponent =
        invalidPhoneNumberComponent ?: initializeInactivePhoneNumber()

    private fun initializeInactivePhoneNumber(): InvalidPhoneNumberComponent {
        val loginRegisterComponent =  DaggerLoginRegisterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
        return DaggerInvalidPhoneNumberComponent
            .builder()
            .loginRegisterComponent(loginRegisterComponent)
            .build().also {
                invalidPhoneNumberComponent = it
            }
    }

    override fun getToolbarResourceID(): Int = R.id.toolbar
}