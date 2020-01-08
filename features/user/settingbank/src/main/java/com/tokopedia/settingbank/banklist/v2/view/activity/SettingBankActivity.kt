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
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.view.fragment.OnBankSelectedListener
import com.tokopedia.settingbank.banklist.v2.view.fragment.SettingBankFragment



class SettingBankActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent>, OnBankSelectedListener {

    val ADD_ACCOUNT_REQUEST_CODE =101

    override fun getComponent(): SettingBankComponent = DaggerSettingBankComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication)
                    .baseAppComponent).build()

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SettingBankFragment()
    }

    companion object {
        const val REQUEST_ON_DOC_UPLOAD = 102
        const val UPALOAD_DOCUMENT_MESSAGE =  "UPALOAD_DOCUMENT_MESSAGE"
        fun createIntent(context: Context): Intent {
            return Intent(context, SettingBankActivity::class.java)
        }
    }


    override fun getTagFragment(): String {
        return SettingBankFragment::class.java.name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_ACCOUNT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
            fragment?.let {
                if(fragment is SettingBankFragment){
                    fragment.loadUserBankAccountList()
                }
            }
        }else if(requestCode == REQUEST_ON_DOC_UPLOAD && resultCode == Activity.RESULT_OK){
            val message : String? = intent.getStringExtra(UPALOAD_DOCUMENT_MESSAGE)
            val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
            fragment?.let {
                if(fragment is SettingBankFragment){
                    fragment.showToasterOnUI(message)
                }
            }
        }
    }


    override fun onBankSelected(bank: Bank) {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            if(fragment is SettingBankFragment){
                fragment.closeBottomSheet()
            }
        }
        startActivityForResult(AddBankActivity.createIntent(this, bank), ADD_ACCOUNT_REQUEST_CODE)
    }

}
