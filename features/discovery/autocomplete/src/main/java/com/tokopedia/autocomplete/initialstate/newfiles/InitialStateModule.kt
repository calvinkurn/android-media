package com.tokopedia.autocomplete.initialstate.newfiles

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponseMapper
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchUseCase
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class InitialStateModule {

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateUseCaseFlow(
            initialStateRepository: InitialStateRepository
    ): InitialStateUseCaseFlow {
        return InitialStateUseCaseFlow(
                initialStateRepository
        )
    }

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateUseCase(
            initialStateRepository: InitialStateRepository
    ): InitialStateUseCase {
        return InitialStateUseCase(
                initialStateRepository
        )
    }

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateRepository(
            @AutoCompleteQualifier initialStateApi: InitialStateApi,
            initialStateMapper: InitialStateMapper,
            popularSearchResponseMapper: PopularSearchResponseMapper
    ): InitialStateRepository {
        return InitialStateRepositoryImpl(
                InitialStateDataSource(initialStateApi, initialStateMapper, popularSearchResponseMapper, PersistentCacheManager.instance)
        )
    }

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateMapper(): InitialStateMapper {
        return InitialStateMapper()
    }

    @AutoCompleteScope
    @Provides
    internal fun providePopularSearchMapper(): PopularSearchResponseMapper {
        return PopularSearchResponseMapper()
    }

    @AutoCompleteScope
    @Provides
    internal fun provideDeleteRecentSearchUseCase(
            initialStateRepository: InitialStateRepository,
            initialStateUseCase: InitialStateUseCase
    ): DeleteRecentSearchUseCaseNew {
        return DeleteRecentSearchUseCaseNew(
                initialStateRepository,
                initialStateUseCase
        )
    }

    @AutoCompleteScope
    @Provides
    internal fun providePopularSearchUseCase(
            initialStateRepository: InitialStateRepository
    ): PopularSearchUseCase {
        return PopularSearchUseCase(
                initialStateRepository
        )
    }

//    @AutoCompleteScope
//    @Provides
//    internal fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
//        return UserSession(context)
//    }
}