package com.tokopedia.loginregister.external_register.ovo.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginregister.external_register.ovo.view.fragment.OvoAddPhoneFragment

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAddPhoneActivity: BaseSimpleActivity(){

    override fun getNewFragment(): Fragment? {
        return OvoAddPhoneFragment.createInstance()
    }
}