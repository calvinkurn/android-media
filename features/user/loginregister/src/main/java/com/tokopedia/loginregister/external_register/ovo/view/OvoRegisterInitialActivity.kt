package com.tokopedia.loginregister.external_register.ovo.view

import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity

/**
 * Created by Yoris Prayogo on 01/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoRegisterInitialActivity: RegisterInitialActivity() {
    override fun getNewFragment(): Fragment? {
        return OvoRegisterInitialFragment.createInstance()
    }
}