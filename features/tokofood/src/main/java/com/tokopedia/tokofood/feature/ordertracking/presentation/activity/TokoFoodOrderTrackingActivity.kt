package com.tokopedia.tokofood.feature.ordertracking.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment.BaseTokoFoodOrderTrackingFragment
import com.tokopedia.tokofood.feature.ordertracking.di.component.DaggerTokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent

class TokoFoodOrderTrackingActivity: BaseSimpleActivity(), HasComponent<TokoFoodOrderTrackingComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.data?.let {
            Bundle().apply {
                val params = UriUtil.uriQueryParamsToMap(it)
                val orderIdParam = params[DeeplinkMapperTokoFood.PATH_ORDER_ID].orEmpty()
                putString(DeeplinkMapperTokoFood.PATH_ORDER_ID, orderIdParam)
            }
        }
        return BaseTokoFoodOrderTrackingFragment.newInstance(bundle)
    }

    override fun getComponent(): TokoFoodOrderTrackingComponent {
        return DaggerTokoFoodOrderTrackingComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

}