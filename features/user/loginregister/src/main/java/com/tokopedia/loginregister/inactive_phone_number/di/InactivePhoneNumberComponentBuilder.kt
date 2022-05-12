package com.tokopedia.loginregister.inactive_phone_number.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent

object InactivePhoneNumberComponentBuilder {
    fun getComponent(application: Context?): InactivePhoneNumberComponent {
        val loginRegisterComponent = DaggerLoginRegisterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
        return DaggerInactivePhoneNumberComponent
            .builder()
            .loginRegisterComponent(loginRegisterComponent)
            .build()
    }
}