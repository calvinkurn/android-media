package com.tokopedia.home_account.stub.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import com.tokopedia.home_account.test.R
import com.tokopedia.home_account.topads.HomeAccountNewTopAdsVerificationTest

class InstrumentationNewHomeAccountTestActivity : AppCompatActivity(), HasComponent<HomeAccountUserComponents> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_home_account_test)

        val homeAccountFragment: Fragment = HomeAccountUserFragment.newInstance(Bundle())
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_new_home_account, homeAccountFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun getComponent(): HomeAccountUserComponents {
        return HomeAccountNewTopAdsVerificationTest.component!!
    }
}
