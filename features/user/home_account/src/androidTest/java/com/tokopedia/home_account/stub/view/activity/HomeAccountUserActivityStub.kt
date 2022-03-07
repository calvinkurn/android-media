package com.tokopedia.home_account.stub.view.activity

import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity

class HomeAccountUserActivityStub : HomeAccountUserActivity() {

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun initializeHomeAccountUserComponents(): HomeAccountUserComponents {
        return HomeAccountTest.homeAccountUserComponents
    }
}