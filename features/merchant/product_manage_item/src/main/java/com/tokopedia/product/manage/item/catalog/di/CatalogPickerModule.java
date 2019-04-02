package com.tokopedia.product.manage.item.catalog.di;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.product.manage.item.catalog.data.repository.CatalogRepositoryImpl;
import com.tokopedia.product.manage.item.catalog.data.source.CatalogDataSource;
import com.tokopedia.product.manage.item.catalog.domain.CatalogRepository;
import com.tokopedia.product.manage.item.catalog.domain.FetchCatalogDataUseCase;
import com.tokopedia.product.manage.item.catalog.view.presenter.CatalogPickerPresenter;
import com.tokopedia.product.manage.item.catalog.view.presenter.CatalogPickerPresenterImpl;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.SearchApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/3/17.
 */
@ActivityScope
@Module
public class CatalogPickerModule {

    @ActivityScope
    @Provides
    CatalogPickerPresenter provideCatalogPickerPresenter(FetchCatalogDataUseCase fetchCatalogDataUseCase){
        return new CatalogPickerPresenterImpl(fetchCatalogDataUseCase);
    }

    @ActivityScope
    @Provides
    CatalogRepository provideCatalogRepository(CatalogDataSource catalogDataSource){
        return new CatalogRepositoryImpl(catalogDataSource);
    }

    @ActivityScope
    @Provides
    SearchApi provideSearchApi(@AceQualifier Retrofit retrofit){
        return retrofit.create(SearchApi.class);
    }

}
