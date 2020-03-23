package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.data.SuggestionMapper
import com.tokopedia.autocomplete.suggestion.SuggestionRepository
import com.tokopedia.autocomplete.suggestion.data.SuggestionApi
import com.tokopedia.autocomplete.suggestion.data.SuggestionDataSource
import com.tokopedia.autocomplete.suggestion.data.SuggestionRepositoryImpl
import com.tokopedia.cachemanager.PersistentCacheManager
import dagger.Module
import dagger.Provides

@SuggestionScope
@Module
class SuggestionRepositoryModule {

    @SuggestionScope
    @Provides
    internal fun provideSuggestionRepository(
            @SuggestionQualifier suggestionApi: SuggestionApi,
            suggestionMapper: SuggestionMapper
    ): SuggestionRepository {
        return SuggestionRepositoryImpl(
                SuggestionDataSource(suggestionApi, suggestionMapper, PersistentCacheManager.instance)
        )
    }
}