package com.tokopedia.autocompletecomponent.unify.domain.usecase

import com.tokopedia.autocompletecomponent.di.AutoCompleteScope
import com.tokopedia.autocompletecomponent.initialstate.DELETE_RECENT_SEARCH_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRepository
import com.tokopedia.autocompletecomponent.suggestion.domain.getshopadssuggestion.GetShopAdsSuggestionUseCase
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionTrackerUseCase
import com.tokopedia.autocompletecomponent.unify.domain.AutoCompleteUnifyRequestUtil
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [SuggestionTrackerModule::class])
class AutoCompleteUseCaseModule {
    @AutoCompleteScope
    @Provides
    @Named(AutoCompleteUnifyRequestUtil.INITIAL_STATE_USE_CASE)
    fun provideInitialStateUseCase(): UseCase<UniverseSuggestionUnifyModel> =
        InitialStateUseCase(GraphqlUseCase())

    @AutoCompleteScope
    @Provides
    @Named(AutoCompleteUnifyRequestUtil.SUGGESTION_STATE_USE_CASE)
    fun provideSuggestionStateUseCase(): UseCase<UniverseSuggestionUnifyModel> =
        SuggestionStateUseCase(GraphqlUseCase(), GetShopAdsSuggestionUseCase(GraphqlUseCase()))

    @AutoCompleteScope
    @Provides
    @Named(AutoCompleteUnifyRequestUtil.URL_TRACKER_USE_CASE)
    fun provideSuggestionTrackerUseCase(suggestionRepository: SuggestionRepository): com.tokopedia.usecase.UseCase<Void?> {
        return SuggestionTrackerUseCase(suggestionRepository)
    }

    @AutoCompleteScope
    @Provides
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    fun provideDeleteRecentSearchUseCase(): com.tokopedia.usecase.UseCase<Boolean> {
        return DeleteRecentSearchUseCase(com.tokopedia.graphql.domain.GraphqlUseCase())
    }
}
