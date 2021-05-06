package com.tokopedia.loginregister.external_register.base.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.fragment.ExternalRegisterWebViewFragment

class ExternalRegisterWebViewActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar?.title = "Verifikasi"
        supportActionBar?.elevation = 0F
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle().apply {
            putString(ExternalRegisterConstants.PARAM.URL, intent?.getStringExtra(ExternalRegisterConstants.PARAM.URL) ?: "")
        }
        return ExternalRegisterWebViewFragment.createInstance(bundle)
    }

}