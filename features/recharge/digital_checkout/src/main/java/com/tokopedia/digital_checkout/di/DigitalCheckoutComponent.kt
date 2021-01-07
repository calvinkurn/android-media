package com.tokopedia.digital_checkout.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.digital_checkout.presentation.fragment.DigitalCartFragment
import dagger.Component

/**
 * @author by jessica on 07/01/21
 */

@DigitalCheckoutScope
@Component(modules = [DigitalCheckoutModule::class], dependencies = [BaseAppComponent::class])
interface DigitalCheckoutComponent {

    fun inject(digitalCartFragment: DigitalCartFragment)

}