package com.tokopedia.salam.umrah.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingUtil
import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by furqan on 08/10/2019
 */
@UmrahScope
@Component(modules = [UmrahModule::class], dependencies = [BaseAppComponent::class])
interface UmrahComponent {

    /**
     *DONT PUT USECASE HERE, INSTANCE IN ACTIVITY INSTEAD, USECASE MUST HAVE DIFFERENT REFERENCE
     */

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcher(): UmrahDispatchersProvider

    fun graphQlRepository(): GraphqlRepository

    fun trackingUmrah(): TrackingUmrahUtil
    fun umrahTracking(): UmrahTrackingUtil

    fun multiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase

}