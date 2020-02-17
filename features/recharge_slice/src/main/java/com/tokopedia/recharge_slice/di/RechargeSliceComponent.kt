package com.tokopedia.recharge_slice.di

import com.tokopedia.recharge_slice.ui.activity.MainActivity
import com.tokopedia.recharge_slice.ui.provider.MainSliceProvider
import dagger.Component

@RechargeSliceScope
@Component(modules = [RechargeSliceModule::class])
interface RechargeSliceComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainSliceProvider: MainSliceProvider)
}