package com.tokopedia.sellerorder.confirmshipping.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_SHIPMENT_NAME
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_SHIPMENT_PRODUCT_NAME
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.confirmshipping.di.DaggerSomConfirmShippingComponent
import com.tokopedia.sellerorder.confirmshipping.di.SomConfirmShippingComponent
import com.tokopedia.sellerorder.confirmshipping.presentation.fragment.SomConfirmShippingFragment

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingActivity: BaseSimpleActivity(), HasComponent<SomConfirmShippingComponent> {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
            bundle.putString(PARAM_CURR_SHIPMENT_NAME, "")
            bundle.putString(PARAM_CURR_SHIPMENT_PRODUCT_NAME, "")
        }
        return SomConfirmShippingFragment.newInstance(bundle)
    }

    override fun getComponent(): SomConfirmShippingComponent =
        DaggerSomConfirmShippingComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}