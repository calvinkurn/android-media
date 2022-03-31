package com.tokopedia.tokofood.feature.ordertracking.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent

class TokoFoodOrderTrackingFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TokoFoodOrderTrackingComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance(): TokoFoodOrderTrackingFragment {
            return TokoFoodOrderTrackingFragment()
        }
    }
}