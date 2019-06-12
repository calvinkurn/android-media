package com.tokopedia.power_merchant.subscribe.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@PowerMerchantSubscribeScope
@Component(modules = [PowerMerchantSubscribeModule::class], dependencies = [BaseAppComponent::class])
class PowerMerchantSubscribeComponent {

}