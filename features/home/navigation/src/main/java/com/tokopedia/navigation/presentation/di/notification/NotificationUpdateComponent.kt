package com.tokopedia.navigation.presentation.di.notification

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.navigation.presentation.activity.NotificationActivity
import com.tokopedia.navigation.presentation.di.notification.module.CommonModule
import com.tokopedia.navigation.presentation.di.notification.module.NotificationQueryModule
import com.tokopedia.navigation.presentation.di.notification.module.NotificationUpdateModule
import com.tokopedia.navigation.presentation.di.notification.scope.NotificationUpdateScope
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment
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