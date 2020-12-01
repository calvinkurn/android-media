package com.tokopedia.loginregister.external_register.ovo.view

import android.os.Bundle
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment

/**
 * Created by Yoris Prayogo on 01/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class OvoRegisterInitialFragment: RegisterInitialFragment() {


    fun onSuccessOvoRegister(){

    }

    fun onFailedOvoRegister(){

    }

    companion object {
        fun createInstance(bundle: Bundle? = null): OvoRegisterInitialFragment {
            val fragment = OvoRegisterInitialFragment().apply {
                bundle?.run {
                    arguments = this
                }
            }
            return fragment
        }
    }
}