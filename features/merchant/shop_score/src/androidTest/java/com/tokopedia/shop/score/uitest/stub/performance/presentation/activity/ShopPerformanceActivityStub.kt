package com.tokopedia.shop.score.uitest.stub.performance.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformancePageActivity
import com.tokopedia.shop.score.uitest.stub.common.UserSessionStub
import com.tokopedia.shop.score.uitest.stub.performance.presentation.fragment.ShopPerformanceFragmentStub

class ShopPerformanceActivityStub: ShopPerformancePageActivity() {

    lateinit var userSessionInterface: UserSessionStub

    override fun getNewFragment(): Fragment {
        return ShopPerformanceFragmentStub.newInstance()
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopPerformanceFragmentStub::class.java)
    }

}