package com.tokopedia.loginregister.external_register.ovo.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class OvoFinalPageActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return OvoFinalFragment.createInstance()
    }
}