package com.tokopedia.home_account.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_account.view.HomeAccountUserFragment
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@HomeAccountUserScope
@SessionCommonScope
@Component(modules = [HomeAccountUserModules::class,
    HomeAccountUserUsecaseModules::class,
    HomeAccountUserViewModelModules::class,
    HomeAccountUserQueryModules::class,
    RecommendationModule::class,
    SessionModule::class], dependencies = [BaseAppComponent::class])
interface HomeAccountUserComponents {
    fun inject(view: HomeAccountUserFragment?)
}