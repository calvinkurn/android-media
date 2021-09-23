package com.tokopedia.additional_check.di

import com.google.android.datatransport.runtime.dagger.Component
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule

/**
 * @author by nisie on 10/15/18.
 */
@AdditionalCheckScope
@SessionCommonScope
@Component(modules = [AdditionalCheckModules::class,
    AdditionalCheckUseCaseModules::class,
    AdditionalCheckViewmodelModules::class,
    AdditionalCheckQueryModules::class,
    SessionModule::class], dependencies = [BaseAppComponent::class])
interface AdditionalCheckComponents {
    fun inject(view: TwoFactorCheckerSubscriber?)
}