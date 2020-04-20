package com.tokopedia.home.account.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.design.dialog.IAccessRequestListener
import com.tokopedia.home.account.presentation.fragment.setting.GeneralSettingFragment

class GeneralSettingActivity : BaseSimpleActivity(), IAccessRequestListener {
    override fun getNewFragment(): Fragment? {
        return GeneralSettingFragment.createInstance()
    }
    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, GeneralSettingActivity::class.java)
        }
    }

    override fun clickDeny() {

    }

    override fun clickAccept() {
        val fragment = fragment as GeneralSettingFragment
        fragment.onClickAcceptButton()
    }

}
