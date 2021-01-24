package com.tokopedia.loginregister.external_register.ovo.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.external_register.base.di.DaggerExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterModules
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterUseCaseModules

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class OvoFinalPageActivity: BaseSimpleActivity(), HasComponent<ExternalRegisterComponent> {
    companion object {
        const val TYPE_SUCCESS = 1
        const val TYPE_ERROR = 0
        const val TYPE_PARAM = "typeParam"


        fun createIntentSuccess(activity: FragmentActivity?): Intent {
            return Intent(activity, OvoFinalPageActivity::class.java).apply {
                putExtra(TYPE_PARAM, TYPE_SUCCESS)
            }
        }

        fun createIntentError(activity: FragmentActivity?): Intent {
            return Intent(activity, OvoFinalPageActivity::class.java).apply {
                putExtra(TYPE_PARAM, TYPE_ERROR)
            }
        }
    }

    override fun getComponent(): ExternalRegisterComponent {
        val loginRegister = DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        return DaggerExternalRegisterComponent
                .builder()
                .loginRegisterComponent(loginRegister)
                .externalRegisterModules(ExternalRegisterModules(this))
                .externalRegisterUseCaseModules(ExternalRegisterUseCaseModules())
                .build()
    }

    override fun getNewFragment(): Fragment? {
        return if(intent?.getIntExtra(TYPE_PARAM, TYPE_SUCCESS) == TYPE_ERROR){
            OvoErrorFragment.createInstance()
        } else {
            OvoSuccessFragment.createInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent?.getIntExtra(TYPE_PARAM, TYPE_SUCCESS) == TYPE_SUCCESS){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.title = "    ${getString(R.string.title_external_register)}"
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = getString(R.string.title_external_register_verify)
        }
    }

    override fun onBackPressed() {
        if(fragment is OvoErrorFragment){
            (fragment as OvoErrorFragment).onBackButtonClicked()
        }
        super.onBackPressed()
    }
}