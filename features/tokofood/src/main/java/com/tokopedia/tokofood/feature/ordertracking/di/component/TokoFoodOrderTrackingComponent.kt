package com.tokopedia.tokofood.feature.ordertracking.di.component

import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment.BaseTokoFoodOrderTrackingFragment
import com.tokopedia.tokofood.feature.ordertracking.di.module.TokoFoodOrderTrackingModule
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.feature.ordertracking.presentation.activity.TokoFoodOrderTrackingActivity
import dagger.Component

@TokoFoodOrderTrackingScope
@Component(modules = [TokoFoodOrderTrackingModule::class], dependencies = [BaseAppComponent::class, TokoChatConfigComponent::class])
interface TokoFoodOrderTrackingComponent {
    fun inject(tokoFoodOrderTrackingFragment: BaseTokoFoodOrderTrackingFragment)
    fun inject(tokoFoodOrderTrackingActivity: TokoFoodOrderTrackingActivity)
}
