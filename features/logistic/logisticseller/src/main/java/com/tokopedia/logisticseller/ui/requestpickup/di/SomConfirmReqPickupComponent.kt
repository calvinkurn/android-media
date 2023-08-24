package com.tokopedia.logisticseller.ui.requestpickup.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.requestpickup.presentation.activity.RequestPickupActivity
import com.tokopedia.logisticseller.ui.requestpickup.presentation.fragment.RequestPickupFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-11-12.
 */
@ActivityScope
@Component(
    modules = [
        ConfirmRequestPickupModule::class,
        SomConfirmReqPickupViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface SomConfirmReqPickupComponent {
    fun inject(somConfirmReqPickupActivity: RequestPickupActivity)
    fun inject(requestPickupFragment: RequestPickupFragment)

}
