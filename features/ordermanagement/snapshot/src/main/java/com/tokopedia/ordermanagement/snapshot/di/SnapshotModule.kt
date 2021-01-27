package com.tokopedia.ordermanagement.snapshot.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ordermanagement.snapshot.util.SnapshotDispatcherProvider
import com.tokopedia.ordermanagement.snapshot.util.SnapshotProductionDispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 1/25/21.
 */

@Module
class SnapshotModule(val context: Context) {
    @SnapshotScope
    @Provides
    fun provideContext(): Context = context

    @SnapshotScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @SnapshotScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SnapshotScope
    @Provides
    fun provideUohDispatcherProvider(): SnapshotDispatcherProvider = SnapshotProductionDispatcherProvider()

    @SnapshotScope
    @Provides
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }
}