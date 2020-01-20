package com.tokopedia.home.account.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.account.di.module.PushNotifCheckerModule
import com.tokopedia.home.account.di.scope.PushNotifCheckerScope
import com.tokopedia.home.account.presentation.fragment.PushNotifCheckerFragment
import dagger.Component


@Component(modules = [PushNotifCheckerModule::class], dependencies = [BaseAppComponent::class])
@PushNotifCheckerScope
interface PushNotifCheckerComponent {
    fun inject(fragment: PushNotifCheckerFragment)
}