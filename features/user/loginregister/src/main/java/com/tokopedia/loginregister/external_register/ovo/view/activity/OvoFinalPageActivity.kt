package com.tokopedia.loginregister.external_register.ovo.view.activity

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
import com.tokopedia.loginregister.external_register.ovo.view.fragment.OvoErrorFragment
import com.tokopedia.loginregister.external_register.ovo.view.fragment.OvoSuccessFragment

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class OvoFinalPageActivity: BaseSimpleActivity(), HasComponent<ExternalRegisterComponent> {
    companion object {
        const val TYPE_SUCCESS = 1
        const val TYPE_ERROR = 0
        const val TYPE_PARAM = "typeParam"
        const val KEY_GOTO_REGISTER = "goToNormalRegister"

        const val KEY_QUERY_IS_SUCCESS = "isSuccess"
        const val KEY_QUERY_IS_CONTINUE = "isContinue"

        const val QUERY_TRUE = "1"
        const val QUERY_FALSE = "0"
        fun createIntentSuccess(activity: FragmentActivity?): Intent {
            return Intent(activity, OvoFinalPageActivity::class.java).apply {
                putExtra(TYPE_PARAM, TYPE_SUCCESS)
            }
        }

        fun createIntentError(activity: FragmentActivity?, isContinueToRegister: Boolean = false): Intent {
            return Intent(activity, OvoFinalPageActivity::class.java).apply {
                putExtra(TYPE_PARAM, TYPE_ERROR)
                putExtra(KEY_GOTO_REGISTER, isContinueToRegister)
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
        intent?.run {
            if(data?.queryParameterNames?.isNotEmpty() == true){
                val isSuccess = data?.getQueryParameter(KEY_QUERY_IS_SUCCESS) ?: "0"
                val isContinue = data?.getQueryParameter(KEY_QUERY_IS_CONTINUE) ?: "0"
                if(isSuccess == QUERY_TRUE){
                    return OvoSuccessFragment.createInstance()
                } else if(isSuccess == QUERY_FALSE){
                    return OvoErrorFragment.createInstance().apply {
                        arguments = Bundle().apply {
                            putBoolean(KEY_GOTO_REGISTER, isContinue == QUERY_TRUE)
                        }
                    }
                }
            } else {
                if(intent?.getIntExtra(TYPE_PARAM, TYPE_ERROR) == TYPE_ERROR){
                    return OvoErrorFragment.createInstance().apply {
                        arguments  = Bundle().apply {
                            putBoolean(KEY_GOTO_REGISTER, intent?.getBooleanExtra(KEY_GOTO_REGISTER, false) ?: false)
                        }
                    }
                } else if(intent?.getIntExtra(TYPE_PARAM, TYPE_ERROR) == TYPE_SUCCESS) {
                    return OvoSuccessFragment.createInstance()
                }
            }
        }
        return OvoErrorFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.run {
            if (getIntExtra(TYPE_PARAM, TYPE_SUCCESS) == TYPE_SUCCESS) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.title = "    ${getString(R.string.title_external_register)}"
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = getString(R.string.title_external_register_verify)
            }
        }
    }

    override fun onBackPressed() {
        if(fragment is OvoErrorFragment){
            (fragment as OvoErrorFragment).onBackButtonClicked()
        }
        super.onBackPressed()
    }
}