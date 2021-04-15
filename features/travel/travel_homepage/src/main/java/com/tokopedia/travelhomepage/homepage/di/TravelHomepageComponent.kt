package com.tokopedia.travelhomepage.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.homepage.presentation.activity.TravelHomepageActivity
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * @author by furqan on 05/08/2019
 */
@TravelHomepageScope
@Component(modules = [TravelHomepageModule::class, TravelHomepageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TravelHomepageComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcherProvider(): CoroutineDispatchers

    fun graphQlRepository(): GraphqlRepository

    fun inject(travelHomepageActivity: TravelHomepageActivity)
    fun inject(travelHomepageFragment: TravelHomepageFragment)

}