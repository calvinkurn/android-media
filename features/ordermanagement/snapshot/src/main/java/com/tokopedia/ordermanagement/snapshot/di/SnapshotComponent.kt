package com.tokopedia.ordermanagement.snapshot.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ordermanagement.snapshot.util.SnapshotDispatcherProvider
import com.tokopedia.ordermanagement.snapshot.view.fragment.SnapshotFragment
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 1/25/21.
 */

@SnapshotScope
@Component(modules = [SnapshotModule::class, SnapshotViewModelModule::class], dependencies = [BaseAppComponent::class])
interface SnapshotComponent {
    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun dispatcherProvider(): SnapshotDispatcherProvider

    fun inject(uohListFragment: SnapshotFragment)
}