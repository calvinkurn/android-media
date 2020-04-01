package com.tokopedia.autocomplete.di;

import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.autocomplete.domain.interactor.SearchMapper;
import com.tokopedia.autocomplete.network.BrowseApi;
import com.tokopedia.autocomplete.repository.AutoCompleteDataSource;
import com.tokopedia.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.autocomplete.repository.AutoCompleteRepositoryImpl;
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;

import dagger.Module;
import dagger.Provides;

@AutoCompleteScope
@Module
public class AutoCompleteModule {

    @AutoCompleteScope
    @Provides
    AutoCompleteUseCase provideSearchUseCase(
            AutoCompleteRepository autoCompleteRepository
    ) {
        return new AutoCompleteUseCase(
                autoCompleteRepository
        );
    }

    @AutoCompleteScope
    @Provides
    AutoCompleteRepository provideAutoCompleteRepository(
        @AutoCompleteQualifier BrowseApi browseApi,
        SearchMapper autoCompleteMapper
    ) {
        return new AutoCompleteRepositoryImpl(
            new AutoCompleteDataSource(browseApi, autoCompleteMapper, PersistentCacheManager.instance)
        );
    }

    @AutoCompleteScope
    @Provides
    SearchMapper provideSearchMapper() {
        return new SearchMapper();
    }
}
