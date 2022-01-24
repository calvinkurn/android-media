package com.tokopedia.shop.score.stub.performance.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformancePageActivity
import com.tokopedia.shop.score.stub.performance.presentation.fragment.ShopPerformanceFragmentStub

class ShopPerformanceActivityStub: ShopPerformancePageActivity() {

    override fun getNewFragment(): Fragment {
        return ShopPerformanceFragmentStub.newInstance()
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopPerformanceActivityStub::class.java)
    }

}