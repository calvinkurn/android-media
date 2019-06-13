package com.tokopedia.power_merchant.subscribe.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment
import dagger.Component
import retrofit2.Retrofit

@PowerMerchantSubscribeScope
@Component(modules = [PowerMerchantSubscribeModule::class], dependencies = BaseAppComponent.class)
interface PowerMerchantSubscribeComponent {

    fun inject(fragment: PowerMerchantSubscribeFragment)
}