package com.tokopedia.autocomplete.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.autocomplete.domain.interactor.SearchMapper;
import com.tokopedia.autocomplete.network.BrowseApi;
import com.tokopedia.autocomplete.presentation.presenter.SearchPresenter;
import com.tokopedia.autocomplete.repository.AutoCompleteDataSource;
import com.tokopedia.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.autocomplete.repository.AutoCompleteRepositoryImpl;
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@AutoCompleteScope
@Module
public class AutoCompleteModule {
    @AutoCompleteScope
    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context) {
        return new SearchPresenter(context);
    }

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

    @AutoCompleteScope
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
