package com.tokopedia.tokofood.feature.ordertracking.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment.BaseTokoFoodOrderTrackingFragment
import com.tokopedia.tokofood.feature.ordertracking.di.component.DaggerTokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent

class TokoFoodOrderTrackingActivity: BaseSimpleActivity(), HasComponent<TokoFoodOrderTrackingComponent> {

    override fun getNewFragment(): Fragment {
        return BaseTokoFoodOrderTrackingFragment.newInstance()
    }

    override fun getComponent(): TokoFoodOrderTrackingComponent {
        return DaggerTokoFoodOrderTrackingComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

}