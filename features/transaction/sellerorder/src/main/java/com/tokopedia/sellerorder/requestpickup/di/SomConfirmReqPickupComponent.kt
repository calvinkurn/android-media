package com.tokopedia.sellerorder.requestpickup.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity
import com.tokopedia.sellerorder.requestpickup.presentation.fragment.SomConfirmReqPickupFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-11-12.
 */
@SomConfirmReqPickupScope
@Component(modules = [SomConfirmReqPickupViewModelModule::class], dependencies = [SomComponent::class])
interface SomConfirmReqPickupComponent {
    fun inject(somConfirmReqPickupActivity: SomConfirmReqPickupActivity)
    fun inject(somConfirmReqPickupFragment: SomConfirmReqPickupFragment)
}