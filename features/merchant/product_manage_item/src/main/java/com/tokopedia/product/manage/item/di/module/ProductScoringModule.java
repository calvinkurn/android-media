package com.tokopedia.product.manage.item.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.manage.item.data.source.ProductScoreDataSource;
import com.tokopedia.product.manage.item.data.source.cache.ProductScoreDataSourceCache;
import com.tokopedia.product.manage.item.data.source.cache.model.ProductScore.DataScoringProduct;
import com.tokopedia.product.manage.item.data.source.cache.model.ProductScore.DataScoringProductBuilder;
import com.tokopedia.product.manage.item.data.repository.ProductScoreRepositoryImpl;
import com.tokopedia.product.manage.item.domain.ProductScoreRepository;
import com.tokopedia.product.manage.item.domain.interactor.ProductScoringUseCase;
import com.tokopedia.product.manage.item.view.presenter.ProductScoringDetailPresenter;
import com.tokopedia.product.manage.item.view.presenter.ProductScoringDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

@ActivityScope
@Module
public class ProductScoringModule {

    @ActivityScope
    @Provides
    ProductScoringDetailPresenter provideProductScoringPresenter(ProductScoringUseCase productScoringUseCase){
        return new ProductScoringDetailPresenterImpl(productScoringUseCase);
    }

    @ActivityScope
    @Provides
    ProductScoringUseCase provideProductScoringUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                       ProductScoreRepository productScoreRepository){
        return new ProductScoringUseCase(threadExecutor, postExecutionThread, productScoreRepository);
    }

    @ActivityScope
    @Provides
    ProductScoreRepository provideProductScoreRepo(ProductScoreDataSource productScoreDataSource){
        return new ProductScoreRepositoryImpl(productScoreDataSource);
    }

    @ActivityScope
    @Provides
    ProductScoreDataSource provideProductScoreDataSource(ProductScoreDataSourceCache productScoreDataSourceCache){
        return new ProductScoreDataSource(productScoreDataSourceCache);
    }

    @ActivityScope
    @Provides
    ProductScoreDataSourceCache provideProductScoreDataSourceCache(DataScoringProduct dataScoringProduct){
        return new ProductScoreDataSourceCache(dataScoringProduct);
    }

    @ActivityScope
    @Provides
    DataScoringProduct provideDataScoringProduct(@ApplicationContext Context context, Gson gson){
        return new DataScoringProductBuilder(context, gson).build();
    }

}
