package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.ManageAddressTest
import com.tokopedia.manageaddress.di.module.ManageAddressModule
import com.tokopedia.manageaddress.di.module.ManageAddressViewModelModule
import com.tokopedia.manageaddress.shareaddress.directshareaddress.DirectShareAddressTest
import com.tokopedia.manageaddress.shareaddress.requestshareaddress.RequestShareAddressTest
import com.tokopedia.manageaddress.shareaddress.shareaddressfromnotif.ShareAddressFromNotifTest
import dagger.Component

@ActivityScope
@Component(
    modules = [ManageAddressModule::class, ManageAddressViewModelModule::class],
    dependencies = [TestAppComponent::class]
)
interface ManageAddressComponentStub : ManageAddressComponent {

    fun inject(test: ManageAddressTest)
    fun inject(test: DirectShareAddressTest)
    fun inject(test: RequestShareAddressTest)
    fun inject(test: ShareAddressFromNotifTest)
}
