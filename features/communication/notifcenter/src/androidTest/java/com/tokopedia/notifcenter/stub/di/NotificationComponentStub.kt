package com.tokopedia.notifcenter.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.di.module.NotificationFragmentModule
import com.tokopedia.notifcenter.di.module.NotificationViewModelModule
import com.tokopedia.notifcenter.stub.di.base.NotificationFakeBaseAppComponent
import com.tokopedia.notifcenter.test.base.BaseNotificationAffiliateTest
import com.tokopedia.notifcenter.test.base.BaseNotificationSellerTest
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import dagger.Component

@ActivityScope
@Component(
    modules = [
        // Stub modules
        NotificationModuleStub::class,
        NotificationUseCaseModuleStub::class,

        // Real modules
        NotificationViewModelModule::class,
        NotificationFragmentModule::class
    ],
    dependencies = [NotificationFakeBaseAppComponent::class]
)
interface NotificationComponentStub : NotificationComponent {
    fun inject(base: BaseNotificationTest)
    fun inject(base: BaseNotificationSellerTest)
    fun inject(base: BaseNotificationAffiliateTest)
}
