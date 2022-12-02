package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticCommon.data.module.CoreModule
import com.tokopedia.manageaddress.di.module.ShareAddressConfirmationViewModelModule
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressBottomSheet
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressConfirmationBottomSheet
import dagger.Component

@ActivityScope
@Component(
    modules = [CoreModule::class, ShareAddressConfirmationViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShareAddressComponent {
    fun inject(fragment: ShareAddressBottomSheet)
    fun inject(fragment: ShareAddressConfirmationBottomSheet)
}
