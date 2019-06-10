package com.tokopedia.product.manage.item.imagepicker.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.product.manage.item.imagepicker.data.repository.CatalogImageRepositoryImpl;
import com.tokopedia.product.manage.item.imagepicker.data.source.CatalogApi;
import com.tokopedia.product.manage.item.imagepicker.data.source.CatalogImageDataSource;
import com.tokopedia.product.manage.item.imagepicker.domain.CatalogImageRepository;
import com.tokopedia.product.manage.item.imagepicker.domain.interactor.ClearCacheCatalogUseCase;
import com.tokopedia.product.manage.item.imagepicker.domain.interactor.GetCatalogImageUseCase;
import com.tokopedia.product.manage.item.imagepicker.util.CatalogConstant;
import com.tokopedia.product.manage.item.imagepicker.view.presenter.ImagePickerCatalogPresenter;
import com.tokopedia.shop.common.util.CacheApiTKPDResponseValidator;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

@CatalogImageScope
@Module
public class ImagePickerCatalogModule {

    @CatalogImageScope
    @Provides
    ImagePickerCatalogPresenter provideImagePickerCatalogPresenter(GetCatalogImageUseCase getCatalogImageUseCase,
                                                                   ClearCacheCatalogUseCase clearCacheCatalogUseCase){
        return new ImagePickerCatalogPresenter(getCatalogImageUseCase, clearCacheCatalogUseCase);
    }

    @CatalogImageScope
    @Provides
    CatalogImageRepository provideCatalogImageRepository(CatalogImageDataSource catalogImageDataSource){
        return new CatalogImageRepositoryImpl(catalogImageDataSource);
    }

    @CatalogImageScope
    @Provides
    CatalogApi provideCatalogApi(Retrofit.Builder retrofit, @CatalogQualifier OkHttpClient okHttpClient){
        return retrofit
                .client(okHttpClient)
                .baseUrl(CatalogConstant.URL_HADES)
                .build()
                .create(CatalogApi.class);
    }

    @CatalogImageScope
    @CatalogQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new CacheApiInterceptor(context, new CacheApiTKPDResponseValidator<>(HeaderErrorListResponse.class)));
        if(GlobalConfig.isAllowDebuggingTools()){
            builder.addInterceptor(new HttpLoggingInterceptor());
            builder.addInterceptor(new ChuckInterceptor(context));
        }
        return builder.build();
    }
}
