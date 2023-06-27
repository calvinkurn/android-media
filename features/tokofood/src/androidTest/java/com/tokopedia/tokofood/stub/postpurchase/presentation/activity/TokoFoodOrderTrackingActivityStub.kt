package com.tokopedia.tokofood.stub.postpurchase.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.tokofood.feature.ordertracking.presentation.activity.TokoFoodOrderTrackingActivity
import com.tokopedia.tokofood.stub.postpurchase.presentation.fragment.BaseTokoFoodOrderTrackingFragmentStub

class TokoFoodOrderTrackingActivityStub : TokoFoodOrderTrackingActivity() {

    override fun getNewFragment(): Fragment {
        return BaseTokoFoodOrderTrackingFragmentStub.newInstance()
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, TokoFoodOrderTrackingActivityStub::class.java)
    }
}
