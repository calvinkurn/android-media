package com.tokopedia.hotel.common.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.di.module.HotelModule
import com.tokopedia.hotel.common.di.scope.HotelScope
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by furqan on 25/03/19
 */
@HotelScope
@Component(modules = [HotelModule::class], dependencies = [BaseAppComponent::class])
interface HotelComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun trackingHotel(): TrackingHotelUtil

    fun dispatcherProvider(): CoroutineDispatchers

    fun inject(hotelBaseActivity: HotelBaseActivity)

}