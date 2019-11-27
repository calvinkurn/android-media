package com.tokopedia.salam.umrah.checkout.di

import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutInstallmentActivity
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutFragment
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutInstallmentFragment
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutPilgrimsFragment
import com.tokopedia.salam.umrah.common.di.UmrahComponent
import dagger.Component

/**
 * @author by firman on 4/11/2019
 */
@UmrahCheckoutScope
@Component(modules = [UmrahCheckoutModule::class, UmrahCheckoutViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahCheckoutComponent {

    fun inject(umrahCheckoutFragment: UmrahCheckoutFragment)
    fun inject(umrahCheckoutActivity:UmrahCheckoutActivity)
    fun inject(umrahCheckoutPilgrimsFragment: UmrahCheckoutPilgrimsFragment)
    fun inject(umrahCheckoutInstallmentFragment: UmrahCheckoutInstallmentFragment)

}