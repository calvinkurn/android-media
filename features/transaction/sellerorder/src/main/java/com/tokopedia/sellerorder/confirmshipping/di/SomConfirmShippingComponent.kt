package com.tokopedia.sellerorder.confirmshipping.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.confirmshipping.presentation.fragment.SomConfirmShippingFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-11-15.
 */
@SomConfirmShippingScope
@Component(modules = [SomConfirmShippingViewModelModule::class], dependencies = [SomComponent::class])
interface SomConfirmShippingComponent {
    fun inject(somConfirmShippingActivity: SomConfirmShippingActivity)
    fun inject(somConfirmShippingFragment: SomConfirmShippingFragment)
}