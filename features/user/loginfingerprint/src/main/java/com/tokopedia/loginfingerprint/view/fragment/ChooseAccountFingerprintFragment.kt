package com.tokopedia.loginfingerprint.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.loginphone.chooseaccount.view.fragment.ChooseAccountFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class ChooseAccountFingerprintFragment: ChooseAccountFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverFingerprint()
    }

    private fun initObserverFingerprint() {
        chooseAccountViewModel.getAccountListFingerprintResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetFingerprintAccounts()
                is Fail -> onErrorGetFingerprintAccounts()
            }
        })
    }

    override fun getAccountList() {
        chooseAccountViewModel.getAccountListFingerprint()
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