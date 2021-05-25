package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 04/07/20.
 */

@UohListScope
@Component(modules = [UohListModule::class, UohListViewModelModule::class], dependencies = [BaseAppComponent::class])
interface UohListComponent {
    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun dispatcherProvider(): CoroutineDispatchers

    fun inject(uohListFragment: UohListFragment)
}