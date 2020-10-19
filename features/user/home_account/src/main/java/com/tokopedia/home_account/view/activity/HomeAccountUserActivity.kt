package com.tokopedia.home_account.view.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home_account.view.HomeAccountUserFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import kotlin.system.exitProcess

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return HomeAccountUserFragment.newInstance(intent?.extras)
    }

}