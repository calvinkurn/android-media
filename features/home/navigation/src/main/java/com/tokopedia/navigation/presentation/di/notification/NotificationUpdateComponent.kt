package com.tokopedia.navigation.presentation.di.notification

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.navigation.presentation.activity.NotificationActivity
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment
import dagger.Component

/**
 * @author : Steven 10/04/19
 */


@NotificationUpdateScope
@Component(
        modules = [(NotificationUpdateModule::class)],
        dependencies = [(BaseAppComponent::class)]
)
interface NotificationUpdateComponent {
    fun inject (fragment: NotificationActivity)
    fun inject (fragment: NotificationUpdateFragment)
}