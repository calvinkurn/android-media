package com.tokopedia.sellerorder.requestpickup.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.requestpickup.di.DaggerSomConfirmReqPickupComponent
import com.tokopedia.sellerorder.requestpickup.di.SomConfirmReqPickupComponent
import com.tokopedia.sellerorder.requestpickup.presentation.fragment.SomConfirmReqPickupFragment

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupActivity: BaseSomActivity(), HasComponent<SomConfirmReqPickupComponent> {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
        }
        return SomConfirmReqPickupFragment.newInstance(bundle)
    }

    override fun getComponent(): SomConfirmReqPickupComponent =
        DaggerSomConfirmReqPickupComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}