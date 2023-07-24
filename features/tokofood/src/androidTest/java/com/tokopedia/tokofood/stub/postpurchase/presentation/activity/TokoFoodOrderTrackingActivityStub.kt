package com.tokopedia.tokofood.stub.postpurchase.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.tokochat.common.util.TokoChatValueUtil
import com.tokopedia.tokofood.feature.ordertracking.presentation.activity.TokoFoodOrderTrackingActivity
import com.tokopedia.tokofood.stub.common.util.TokoFoodOrderTrackingComponentStubInstance
import com.tokopedia.tokofood.stub.postpurchase.presentation.fragment.BaseTokoFoodOrderTrackingFragmentStub

class TokoFoodOrderTrackingActivityStub : TokoFoodOrderTrackingActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle().apply {
            putString(DeeplinkMapperTokoFood.PATH_ORDER_ID, "1234567")
        }.apply {
            putBoolean(
                TokoChatValueUtil.IS_FROM_BUBBLE_KEY,
                intent.getBooleanExtra(TokoChatValueUtil.IS_FROM_BUBBLE_KEY, false)
            )
        }
        return BaseTokoFoodOrderTrackingFragmentStub.newInstance(bundle)
    }

    override fun initInjector() {
        TokoFoodOrderTrackingComponentStubInstance.getTokoFoodOrderTrackingComponentStub(
            this
        ).inject(this)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) =
            Intent(context, TokoFoodOrderTrackingActivityStub::class.java)
    }
}
