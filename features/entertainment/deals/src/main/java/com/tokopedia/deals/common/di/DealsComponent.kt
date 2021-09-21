package com.tokopedia.deals.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by jessica on 11/06/20
 */

@DealsScope
@Component(modules = [DealsModule::class, DealsViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface DealsComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun dispatcher(): Dispatchers

    fun graphQlRepository(): GraphqlRepository

    fun dealsLocationUtils(): DealsLocationUtils

    fun irisSession(): IrisSession

    fun dispatcherProvider(): CoroutineDispatchers

    fun inject(dealsBaseActivity: DealsBaseActivity)
}