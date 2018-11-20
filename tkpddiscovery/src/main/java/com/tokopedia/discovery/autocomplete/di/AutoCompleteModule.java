package com.tokopedia.discovery.autocomplete.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.di.module.net.DiscoveryNetModule;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteDataSource;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepositoryImpl;
import com.tokopedia.discovery.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.discovery.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.discovery.newdiscovery.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.discovery.newdiscovery.network.BrowseApi;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.domain.interactor.SearchMapper;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@Module(includes = DiscoveryNetModule.class)
public class AutoCompleteModule {
    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context) {
        return new SearchPresenter(context);
    }

    @Provides
    AutoCompleteUseCase provideSearchUseCase(
            AutoCompleteRepository autoCompleteRepository
    ) {
        return new AutoCompleteUseCase(
                autoCompleteRepository
        );
    }

    @Provides
    AutoCompleteRepository provideAutoCompleteRepository(
        @AutoCompleteQualifier BrowseApi browseApi,
        SearchMapper autoCompleteMapper,
        CacheManager cacheManager
    ) {
        return new AutoCompleteRepositoryImpl(
            new AutoCompleteDataSource(browseApi, autoCompleteMapper, cacheManager)
        );
    }

    @Provides
    SearchMapper provideSearchMapper() {
        return new SearchMapper();
    }

    @Provides
    DeleteRecentSearchUseCase provideDeleteRecentSearchUseCase(
            AutoCompleteRepository autoCompleteRepository,
            AutoCompleteUseCase autoCompleteUseCase
    ) {
        return new DeleteRecentSearchUseCase(
                autoCompleteRepository,
                autoCompleteUseCase
        );
    }

    @AutoCompleteScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
