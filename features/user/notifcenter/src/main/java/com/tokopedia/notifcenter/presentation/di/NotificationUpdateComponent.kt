package com.tokopedia.notifcenter.presentation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.di.module.CommonModule
import com.tokopedia.notifcenter.presentation.di.module.NotificationQueryModule
import com.tokopedia.notifcenter.presentation.di.module.NotificationUpdateModule
import com.tokopedia.notifcenter.presentation.di.scope.NotificationUpdateScope
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateFragment
import dagger.Component

/**
 * @author : Steven 10/04/19
 */


@NotificationUpdateScope
@Component(
        modules = [
            CommonModule::class,
            NotificationUpdateModule::class,
            NotificationQueryModule::class
        ],
        dependencies = [(BaseAppComponent::class)]
)
interface NotificationUpdateComponent {
    fun inject(fragment: NotificationActivity)
    fun inject(fragment: NotificationUpdateFragment)
}