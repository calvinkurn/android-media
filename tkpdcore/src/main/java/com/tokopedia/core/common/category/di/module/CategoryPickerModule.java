package com.tokopedia.core.common.category.di.module;

import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.common.category.di.scope.CategoryPickerScope;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryWithParentChildUseCase;
import com.tokopedia.core.common.category.presenter.CategoryPickerPresenter;
import com.tokopedia.core.common.category.presenter.CategoryPickerPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
@Module
public class CategoryPickerModule {

    @CategoryPickerScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource,
                                                 CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @CategoryPickerScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@HadesQualifier Retrofit retrofit){
        return retrofit.create(HadesCategoryApi.class);
    }


    @CategoryPickerScope
    @Provides
    CategoryPickerPresenter provideCategoryPickerPresenter(
            FetchCategoryWithParentChildUseCase fetchCategoryChildUseCase,
            FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase
    ){
        return new CategoryPickerPresenterImpl(fetchCategoryChildUseCase, fetchCategoryFromSelectedUseCase);
    }

}
