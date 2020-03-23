package com.tokopedia.recharge_slice.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.recharge_slice.ui.activity.MainActivity
import com.tokopedia.recharge_slice.ui.provider.MainSliceProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@RechargeSliceScope
@Component(modules = [RechargeSliceModule::class])
interface RechargeSliceComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainSliceProvider: MainSliceProvider)
}