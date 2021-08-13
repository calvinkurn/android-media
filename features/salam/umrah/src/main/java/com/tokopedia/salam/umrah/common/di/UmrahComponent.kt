package com.tokopedia.salam.umrah.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.presentation.activity.UmrahBaseActivity
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * @author by furqan on 08/10/2019
 */
@UmrahScope
@Component(modules = [UmrahModule::class], dependencies = [BaseAppComponent::class])
interface UmrahComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcher(): CoroutineDispatchers

    fun graphQlRepository(): GraphqlRepository

    fun umrahTracking(): UmrahTrackingAnalytics

    fun multiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase

    fun inject(umrahBaseActivity: UmrahBaseActivity)

}