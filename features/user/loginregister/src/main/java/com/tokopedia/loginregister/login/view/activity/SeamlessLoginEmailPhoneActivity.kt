package com.tokopedia.loginregister.login.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.login.view.fragment.SeamlessLoginEmailPhoneFragment

/**
 * Created by Yoris Prayogo on 30/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class SeamlessLoginEmailPhoneActivity: LoginActivity() {
    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SeamlessLoginEmailPhoneFragment.createInstance(bundle)
    }
}