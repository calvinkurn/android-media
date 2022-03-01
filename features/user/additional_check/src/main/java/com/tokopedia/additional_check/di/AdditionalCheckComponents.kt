package com.tokopedia.additional_check.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber
import com.tokopedia.additional_check.view.activity.LinkAccountReminderActivity
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@ActivityScope
@SessionCommonScope
@Component(modules = [
    AdditionalCheckModules::class,
    AdditionalCheckViewmodelModules::class,
    SessionModule::class], dependencies = [BaseAppComponent::class])
interface AdditionalCheckComponents {
    fun inject(view: TwoFactorCheckerSubscriber?)
    fun inject(view: LinkAccountReminderActivity)
}