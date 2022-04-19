package com.tokopedia.flight.common.di.component

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.common.di.module.FlightModule
import com.tokopedia.flight.common.di.scope.FlightScope
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.resend_eticket.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import com.tokopedia.flight.search.data.FlightRouteDao
import com.tokopedia.flight.search.data.cache.db.dao.FlightComboDao
import com.tokopedia.flight.search.data.cache.db.dao.FlightJourneyDao
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * @author by furqan on 09/06/2021
 */
@FlightScope
@Component(
    modules = [FlightModule::class],
    dependencies = [BaseAppComponent::class]
)
interface FlightComponent {
    @ApplicationContext
    fun context(): Context
    fun flightRepository(): FlightRepository
    fun gson(): Gson
    fun userSessionInterface(): UserSessionInterface
    fun flightJourneyNewDao(): FlightJourneyDao
    fun flightRouteNewDao(): FlightRouteDao
    fun flightComboNewDao(): FlightComboDao
    fun graphqlRepository(): GraphqlRepository
    fun multiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
    fun dispatcherProvider(): CoroutineDispatchers

    fun inject(baseFlightActivity: BaseFlightActivity)
}