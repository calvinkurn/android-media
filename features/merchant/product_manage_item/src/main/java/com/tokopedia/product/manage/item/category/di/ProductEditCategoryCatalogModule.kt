package com.tokopedia.product.manage.item.category.di

import android.content.Context
import com.tokopedia.core.base.di.qualifier.ApplicationContext
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl
import com.tokopedia.core.common.category.data.source.CategoryDataSource
import com.tokopedia.core.common.category.data.source.CategoryVersionDataSource
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi
import com.tokopedia.core.common.category.data.source.db.CategoryDB
import com.tokopedia.core.common.category.data.source.db.CategoryDao
import com.tokopedia.core.common.category.domain.CategoryRepository
import com.tokopedia.core.network.di.qualifier.AceQualifier
import com.tokopedia.core.network.di.qualifier.HadesQualifier
import com.tokopedia.core.network.di.qualifier.MerlinQualifier
import com.tokopedia.product.manage.item.catalog.data.repository.CatalogRepositoryImpl
import com.tokopedia.product.manage.item.catalog.data.source.CatalogDataSource
import com.tokopedia.product.manage.item.catalog.domain.CatalogRepository
import com.tokopedia.product.manage.item.category.data.repository.CategoryRecommRepositoryImpl
import com.tokopedia.product.manage.item.category.data.source.CategoryRecommDataSource
import com.tokopedia.product.manage.item.category.domain.CategoryRecommRepository
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.MerlinApi
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.SearchApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ProductAddScope
@Module
class ProductEditCategoryCatalogModule{

    @ProductAddScope
    @Provides
    fun provideMerlinApi(@MerlinQualifier retrofit: Retrofit) = retrofit.create(MerlinApi::class.java)

    @ProductAddScope
    @Provides
    fun provideCategoryRecommRepository(categoryRecommDataSource: CategoryRecommDataSource): CategoryRecommRepository {
        return CategoryRecommRepositoryImpl(categoryRecommDataSource)
    }

    @ProductAddScope
    @Provides
    fun provideCatalogRepository(catalogDataSource: CatalogDataSource): CatalogRepository {
        return CatalogRepositoryImpl(catalogDataSource)
    }

    @ProductAddScope
    @Provides
    fun provideCategoryDao(@ApplicationContext context: Context): CategoryDao {
        return CategoryDB.getInstance(context).getCategoryDao()
    }

    @ProductAddScope
    @Provides
    fun provideCategoryRepository(categoryVersionDataSource: CategoryVersionDataSource,
                                           categoryDataSource: CategoryDataSource,
                                           fetchCategoryDataSource: FetchCategoryDataSource): CategoryRepository {
        return CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource)
    }

    @ProductAddScope
    @Provides
    fun provideHadesCategoryApi(@HadesQualifier retrofit: Retrofit): HadesCategoryApi {
        return retrofit.create(HadesCategoryApi::class.java)
    }

    @ProductAddScope
    @Provides
    fun provideSearchApi(@AceQualifier retrofit: Retrofit) = retrofit.create(SearchApi::class.java)
}