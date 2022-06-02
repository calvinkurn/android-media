package com.tokopedia.usercomponents.userconsent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class UserConsentModule {

    @Provides
    @ActivityScope
    @ApplicationContext
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @ActivityScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ActivityScope
    fun provideGraphQlRepository(@ApplicationContext repository: GraphqlRepository): GraphqlRepository {
        return repository
    }
}