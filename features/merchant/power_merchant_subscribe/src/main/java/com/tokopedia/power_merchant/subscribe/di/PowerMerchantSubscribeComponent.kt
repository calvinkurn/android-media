package com.tokopedia.power_merchant.subscribe.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment
import dagger.Component

@PowerMerchantSubscribeScope
@Component(modules = [PowerMerchantSubscribeModule::class])
interface PowerMerchantSubscribeComponent {

    fun inject(fragment: PowerMerchantSubscribeFragment)
}