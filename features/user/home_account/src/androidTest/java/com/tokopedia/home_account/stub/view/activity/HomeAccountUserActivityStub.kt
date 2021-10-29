package com.tokopedia.home_account.stub.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.stub.di.HomeAccountUserComponentsStub
import com.tokopedia.home_account.stub.view.fragment.HomeAccountUserFragmentStub
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment

class HomeAccountUserActivityStub : HomeAccountUserActivity() {

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun initializeHomeAccountUserComponents(): HomeAccountUserComponents {
        return HomeAccountTest.homeAccountUserComponents
    }

    override fun createHomeAccountUserFragment(): Fragment =
        HomeAccountUserFragmentStub.newInstance(intent?.extras)

}