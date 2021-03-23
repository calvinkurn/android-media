package com.tokopedia.loginregister.external_register.ovo.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.external_register.base.di.DaggerExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterModules
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterUseCaseModules
import com.tokopedia.loginregister.external_register.ovo.view.fragment.OvoAddNameFragment

/**
 * Created by Yoris Prayogo on 16/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAddNameActivity: BaseSimpleActivity(), HasComponent<ExternalRegisterComponent> {

    override fun getNewFragment(): Fragment? = OvoAddNameFragment.createInstance(intent?.extras)

    override fun getComponent(): ExternalRegisterComponent {
        val loginRegister = DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        return DaggerExternalRegisterComponent
                .builder()
                .loginRegisterComponent(loginRegister)
                .externalRegisterModules(ExternalRegisterModules(this))
                .externalRegisterUseCaseModules(ExternalRegisterUseCaseModules())
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = ""
        supportActionBar?.elevation = 0F
    }
}