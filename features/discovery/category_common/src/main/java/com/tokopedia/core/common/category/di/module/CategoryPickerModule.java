package com.tokopedia.core.common.category.di.module;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;
import com.tokopedia.core.common.category.constant.ConstantCoreNetwork;
import com.tokopedia.core.common.category.data.network.OkHttpFactory;
import com.tokopedia.core.common.category.data.network.TopAdsAuthInterceptor;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.common.category.data.source.db.CategoryDB;
import com.tokopedia.core.common.category.data.source.db.CategoryDao;
import com.tokopedia.core.common.category.di.qualifier.CategoryPickerOkHttp;
import com.tokopedia.core.common.category.di.qualifier.CategoryPickerRetrofit;
import com.tokopedia.core.common.category.di.scope.CategoryPickerScope;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryWithParentChildUseCase;
import com.tokopedia.core.common.category.presenter.CategoryPickerPresenter;
import com.tokopedia.core.common.category.presenter.CategoryPickerPresenterImpl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
@Module
public class CategoryPickerModule {

    private final Context context;

    public CategoryPickerModule(Context context) {
        this.context = context;
    }

    @ApplicationContext
    @CategoryPickerScope
    @Provides
    Context provideApplicationContext() {
        return this.context;
    }

    @CategoryPickerScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource,
                                                 CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @CategoryPickerScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@CategoryPickerRetrofit Retrofit retrofit){
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

    @CategoryPickerScope
    @Provides
    CategoryDao provideCategoryDao(@ApplicationContext Context context){
        return CategoryDB.getInstance(context).getCategoryDao();
    }

    @CategoryPickerRetrofit
    @CategoryPickerScope
    @Provides
    Retrofit provideCategoryPickerRetrofit(@CategoryPickerOkHttp OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getHADES()).client(okHttpClient).build();
    }

    @CategoryPickerOkHttp
    @CategoryPickerScope
    @Provides
    OkHttpClient provideOkHttpClientNoAuth(TopAdsAuthInterceptor tkpdBearerWithAuthInterceptor,
                                           FingerprintInterceptor fingerprintInterceptor,
                                           TkpdBaseInterceptor tkpdBaseInterceptor,
                                           ChuckInterceptor chuckInterceptor,
                                           DebugInterceptor debugInterceptor,
                                           CacheApiInterceptor cacheApiInterceptor,
                                           @ApplicationContext Context context) {

        cacheApiInterceptor.setResponseValidator(new CacheApiResponseValidator());
        return OkHttpFactory.create(context).buildDaggerClientNoAuthWithBearer(tkpdBearerWithAuthInterceptor,
                fingerprintInterceptor,
                tkpdBaseInterceptor,
                chuckInterceptor,
                debugInterceptor,
                cacheApiInterceptor);
    }

    @CategoryPickerScope
    @Provides
    AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context) {
        return (AbstractionRouter) context;
    }

    @CategoryPickerScope
    @Provides
    TopAdsAuthInterceptor provideTopAdsAuthInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter) {
        return new TopAdsAuthInterceptor(context, abstractionRouter);
    }

    @CategoryPickerScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @CategoryPickerScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @CategoryPickerScope
    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter, UserSessionInterface userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @CategoryPickerScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context,
                                             @Named(ConstantCoreNetwork.CHUCK_ENABLED) LocalCacheHandler localCacheHandler) {
        return new ChuckInterceptor(context)
                .showNotification(localCacheHandler.getBoolean(ConstantCoreNetwork.IS_CHUCK_ENABLED, false));
    }

    @Named(ConstantCoreNetwork.CHUCK_ENABLED)
    @CategoryPickerScope
    @Provides
    public LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, ConstantCoreNetwork.CHUCK_ENABLED);
    }

    @CategoryPickerScope
    @Provides
    DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @CategoryPickerScope
    @Provides
    CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }
}
