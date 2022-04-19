package com.tokopedia.loginregister.login.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent

/**
 * Created by Ade Fulki on 28/09/20.
 */

object LoginComponentBuilder {

    fun getComponent(application: Application): LoginComponent {
        return DaggerLoginComponent
                .builder()
                .loginRegisterComponent(
                        DaggerLoginRegisterComponent
                                .builder()
                                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                                .build()
                )
                .build()
    }
}