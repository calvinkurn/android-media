package com.tokopedia.settingbank.banklist.v2.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.settingbank.banklist.v2.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.di.SettingBankModule
import com.tokopedia.settingbank.banklist.v2.util.SettingBankRemoteConfig
import com.tokopedia.settingbank.banklist.v2.view.fragment.SettingBankFragment


class SettingBankActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent>, SettingBankCallback {

    override fun getComponent(): SettingBankComponent = DaggerSettingBankComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .settingBankModule(SettingBankModule(this))
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SettingBankRemoteConfig.instance(this).isOldFlowEnabled()) {
            this.startActivity(com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity.createIntent(this))
            finish()
            return
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SettingBankFragment()
    }

    companion object {

        const val ADD_ACCOUNT_REQUEST_CODE = 101
        const val REQUEST_ON_DOC_UPLOAD = 102
        const val UPLOAD_DOCUMENT_MESSAGE = "UPLOAD_DOCUMENT_MESSAGE"

    }

    override fun getTagFragment(): String {
        return SettingBankFragment::class.java.name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ACCOUNT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshBankList()
        } else if (requestCode == REQUEST_ON_DOC_UPLOAD && resultCode == Activity.RESULT_OK) {
            onAccountDocUploaded(data)
        }
    }

    override fun onAddBankAccountClick() {
        startActivityForResult(ChooseBankActivity.createIntent(this), ADD_ACCOUNT_REQUEST_CODE)
    }

    private fun refreshBankList() {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            if (fragment is SettingBankFragment) {
                fragment.loadUserBankAccountList()
            }
        }
    }

    private fun onAccountDocUploaded(data: Intent?) {
        data?.let {
            val message: String? = data.getStringExtra(UPLOAD_DOCUMENT_MESSAGE)
            val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
            fragment?.let {
                if (fragment is SettingBankFragment) {
                    fragment.loadUserBankAccountList()
                    fragment.showToasterOnUI(message)
                }
            }
        }
    }

}

interface SettingBankCallback {
    fun onAddBankAccountClick()
}
