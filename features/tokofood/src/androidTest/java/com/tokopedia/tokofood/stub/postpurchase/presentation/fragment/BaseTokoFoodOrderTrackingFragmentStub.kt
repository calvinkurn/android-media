package com.tokopedia.tokofood.stub.postpurchase.presentation.fragment

import android.os.Bundle
import com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment.BaseTokoFoodOrderTrackingFragment
import com.tokopedia.tokofood.stub.common.util.TokoFoodOrderTrackingComponentStubInstance

class BaseTokoFoodOrderTrackingFragmentStub : BaseTokoFoodOrderTrackingFragment() {

    override fun initInjector() {
        TokoFoodOrderTrackingComponentStubInstance.getTokoFoodOrderTrackingComponentStub(
            context!!.applicationContext
        ).inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?): BaseTokoFoodOrderTrackingFragmentStub {
            return if (bundle == null) {
                BaseTokoFoodOrderTrackingFragmentStub()
            } else {
                BaseTokoFoodOrderTrackingFragmentStub().apply {
                    arguments = bundle
                }
            }
        }
    }
}
