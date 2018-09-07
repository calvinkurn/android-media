package com.tokopedia.discovery.autocomplete.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteDataSource;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepositoryImpl;
import com.tokopedia.discovery.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.discovery.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.domain.interactor.SearchMapper;

import dagger.Module;
import dagger.Provides;

@AutoCompleteScope
@Module(includes = ApiModule.class)
public class AutoCompleteModule {

    @AutoCompleteScope
    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context) {
        return new SearchPresenter(context);
    }

    @AutoCompleteScope
    @Provides
    AutoCompleteUseCase provideSearchUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AutoCompleteRepository autoCompleteRepository
    ) {
        return new AutoCompleteUseCase(
                threadExecutor,
                postExecutionThread,
                autoCompleteRepository
        );
    }

    @AutoCompleteScope
    @Provides
    AutoCompleteRepository provideAutoCompleteRepository(
        BrowseApi browseApi,
        SearchMapper autoCompleteMapper
    ) {
        return new AutoCompleteRepositoryImpl(
            new AutoCompleteDataSource(browseApi, autoCompleteMapper)
        );
    }

    @AutoCompleteScope
    @Provides
    SearchMapper provideSearchMapper(Gson gson) {
        return new SearchMapper(gson);
    }

    @AutoCompleteScope
    @Provides
    DeleteRecentSearchUseCase provideDeleteRecentSearchUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AutoCompleteRepository autoCompleteRepository,
            AutoCompleteUseCase autoCompleteUseCase
    ) {
        return new DeleteRecentSearchUseCase(
                threadExecutor,
                postExecutionThread,
                autoCompleteRepository,
                autoCompleteUseCase
        );
    }
}
