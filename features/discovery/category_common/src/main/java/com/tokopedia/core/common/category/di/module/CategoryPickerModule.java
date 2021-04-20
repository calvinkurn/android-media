package com.tokopedia.core.common.category.di.module;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.common.category.constant.ConstantCategoryCommon;
import com.tokopedia.core.common.category.data.network.OkHttpFactory;
import com.tokopedia.core.common.category.data.network.TopAdsAuthInterceptor;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.db.CategoryDB;
import com.tokopedia.core.common.category.data.source.db.CategoryDao;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase;
import com.tokopedia.core.common.category.presenter.CategoryPickerPresenter;
import com.tokopedia.core.common.category.presenter.CategoryPickerPresenterImpl;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
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
@Module
public class CategoryPickerModule {

    private final Context context;

    public CategoryPickerModule(Context context) {
        this.context = context;
    }

    @ApplicationContext
    @Provides
    Context provideApplicationContext() {
        return this.context;
    }

    @Provides
    CategoryRepository provideCategoryRepository(CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource) {
        return new CategoryRepositoryImpl(categoryDataSource, fetchCategoryDataSource);
    }

    @Provides
    CategoryPickerPresenter provideCategoryPickerPresenter(
            GetCategoryLiteTreeUseCase getCategoryLiteTreeUseCase
    ) {
        return new CategoryPickerPresenterImpl(getCategoryLiteTreeUseCase);
    }

    @Provides
    CategoryDao provideCategoryDao(@ApplicationContext Context context) {
        return CategoryDB.getInstance(context).getCategoryDao();
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_RETROFIT)
    @Provides
    Retrofit provideCategoryPickerRetrofit(@Named(ConstantCategoryCommon.CATEGORY_PICKER_OKHTTP) OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getHADES()).client(okHttpClient).build();
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_OKHTTP)
    @Provides
    OkHttpClient provideOkHttpClientNoAuth(TopAdsAuthInterceptor tkpdBearerWithAuthInterceptor,
                                           FingerprintInterceptor fingerprintInterceptor,
                                           TkpdBaseInterceptor tkpdBaseInterceptor,
                                           @Named(ConstantCategoryCommon.CATEGORY_PICKER_CHUCK) ChuckerInterceptor chuckInterceptor,
                                           DebugInterceptor debugInterceptor,
                                           @ApplicationContext Context context) {

        return OkHttpFactory.create(context).buildDaggerClientNoAuthWithBearer(tkpdBearerWithAuthInterceptor,
                fingerprintInterceptor,
                tkpdBaseInterceptor,
                chuckInterceptor,
                debugInterceptor);
    }

    @Provides
    AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context) {
        return (AbstractionRouter) context;
    }

    @Provides
    TopAdsAuthInterceptor provideTopAdsAuthInterceptor(@ApplicationContext Context context, NetworkRouter abstractionRouter, UserSessionInterface userSession) {
        return new TopAdsAuthInterceptor(context, abstractionRouter, userSession);
    }

    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_USER_SESION)
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter, @Named(ConstantCategoryCommon.CATEGORY_PICKER_USER_SESION) UserSessionInterface userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_CHUCK)
    @Provides
    ChuckerInterceptor provideChuckInterceptor(@ApplicationContext Context context,
                                               @Named(ConstantCategoryCommon.CHUCK_ENABLED) LocalCacheHandler localCacheHandler) {
        ChuckerCollector collector = new ChuckerCollector(context, localCacheHandler.getBoolean(ConstantCategoryCommon.IS_CHUCK_ENABLED, false));
        return new ChuckerInterceptor(context, collector);
    }

    @Named(ConstantCategoryCommon.CHUCK_ENABLED)
    @Provides
    public LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, ConstantCategoryCommon.CHUCK_ENABLED);
    }

    @Provides
    DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @Provides
    GraphqlUseCase provideRxGQLUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    GetCategoryLiteTreeUseCase povideGetCatagoryLiteTreeUseCase(GraphqlUseCase graphqlUseCase) {
        return new GetCategoryLiteTreeUseCase(graphqlUseCase);
    }
}
