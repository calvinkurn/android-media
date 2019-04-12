package com.tokopedia.search.presentation.presenter;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.imagesearch.di.module.ImageSearchModule;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.di.module.ProductModule;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        ProductModule.class,
        ImageSearchModule.class,
})
public class SearchPresenterModule {

    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new SearchPresenter(context, getProductUseCase, getImageSearchUseCase);
    }
}
