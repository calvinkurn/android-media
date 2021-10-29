package com.tokopedia.home_account.stub.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment

class HomeAccountUserFragmentStub : HomeAccountUserFragment() {

    companion object {
        fun newInstance(bundle: Bundle?): Fragment {
            return HomeAccountUserFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}