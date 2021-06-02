package com.tokopedia.loginfingerprint.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginphone.chooseaccount.view.fragment.ChooseAccountFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class ChooseAccountFingerprintFragment: ChooseAccountFragment() {


    override fun getAccountList() {
//        chooseAccountViewModel.getAccountListFingerprint()
    }

    fun onSuccessGetFingerprintAccounts() {

    }

    fun onErrorGetFingerprintAccounts() {

    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFingerprintFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}