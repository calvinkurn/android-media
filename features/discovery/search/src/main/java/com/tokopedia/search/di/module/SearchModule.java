package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.search.presentation.presenter.SearchPresenter;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchModule extends com.tokopedia.discovery.newdiscovery.di.module.SearchModule {

    @SearchScope
    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new SearchPresenter(context, getProductUseCase, getImageSearchUseCase);
    }
}
