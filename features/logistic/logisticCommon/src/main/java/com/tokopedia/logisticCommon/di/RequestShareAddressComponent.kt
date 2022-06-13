package com.tokopedia.logisticCommon.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticCommon.data.module.CoreModule
import com.tokopedia.logisticCommon.di.module.RequestShareAddressModule
import com.tokopedia.logisticCommon.di.module.RequestShareAddressViewModelModule
import com.tokopedia.logisticCommon.ui.shareaddress.ShareAddressBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [RequestShareAddressModule::class, RequestShareAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface RequestShareAddressComponent {
    fun inject(fragment: ShareAddressBottomSheet)
}