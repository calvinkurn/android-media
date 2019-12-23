package com.tokopedia.travelhomepage.destination.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.homepage.di.TravelHomepageViewModelModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by jessica on 2019-12-23
 */

@TravelHomepageDestinationScope
@Component(modules = [TravelHomepageDestinationModule::class, TravelHomepageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TravelHomepageDestinationComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

}