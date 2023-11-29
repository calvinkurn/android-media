package com.tokopedia.autocompletecomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.autocompletecomponent.initialstate.DELETE_RECENT_SEARCH_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocompletecomponent.util.ChooseAddressUtilsWrapper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class AutoCompleteModule {
    @AutoCompleteScope
    @Provides
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    fun provideDeleteRecentSearchUseCase(): com.tokopedia.usecase.UseCase<Boolean> {
        return DeleteRecentSearchUseCase(GraphqlUseCase())
    }

    @AutoCompleteScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AutoCompleteScope
    @Provides
    fun provideChooseAddressUtilsWrapper(@ApplicationContext context: Context): ChooseAddressUtilsWrapper {
        return ChooseAddressUtilsWrapper(context)
    }
}
