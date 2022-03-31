package com.tokopedia.tokofood.feature.ordertracking.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.di.module.TokoFoodOrderTrackingModule
import com.tokopedia.tokofood.feature.ordertracking.presentation.fragment.TokoFoodOrderTrackingFragment

class TokoFoodOrderTrackingActivity: BaseSimpleActivity(), HasComponent<TokoFoodOrderTrackingComponent> {

    override fun getNewFragment(): Fragment {
        return TokoFoodOrderTrackingFragment()
    }

    override fun getComponent(): TokoFoodOrderTrackingComponent {
        return DaggerTokoFoodOrderTrackingComponent
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokoFoodOrderTrackingModule(TokoFoodOrderTrackingModule())
            .build()
    }

}