package com.tokopedia.core.common.category.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.core.common.category.constant.ConstantCategoryCommon
import com.tokopedia.core.common.category.data.CategoryDataSource
import com.tokopedia.core.common.category.data.FetchCategoryDataSource
import com.tokopedia.core.common.category.data.network.OkHttpFactory.Companion.create
import com.tokopedia.core.common.category.data.network.TopAdsAuthInterceptor
import com.tokopedia.core.common.category.data.repository.CategoryRepository
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl
import com.tokopedia.core.common.category.data.source.db.CategoryDB.Companion.getInstance
import com.tokopedia.core.common.category.data.source.db.CategoryDao
import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdBaseInterceptor
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

@Module
class CategoryPickerModuleNew(val context:Context) {

    @ApplicationContext
    @Provides
    fun provideApplicationContext(): Context {
        return context
    }

    @Provides
    fun provideCategoryRepository(
        categoryDataSource: CategoryDataSource,
        fetchCategoryDataSource: FetchCategoryDataSource
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDataSource, fetchCategoryDataSource)
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_RETROFIT)
    @Provides
    fun provideCategoryPickerRetrofit(
        @Named(ConstantCategoryCommon.CATEGORY_PICKER_OKHTTP) okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit{
        return retrofitBuilder.baseUrl(TokopediaUrl.getInstance().HADES).client(okHttpClient).build()
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_OKHTTP)
    @Provides
    fun provideOkHttpClientNoAuth(
        tkpdBearerWithAuthInterceptor: TopAdsAuthInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        tkpdBaseInterceptor: TkpdBaseInterceptor,
        @Named(ConstantCategoryCommon.CATEGORY_PICKER_CHUCK) chuckInterceptor: ChuckerInterceptor,
        debugInterceptor: DebugInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return create(context).buildDaggerClientNoAuthWithBearer(
            tkpdBearerWithAuthInterceptor,
            fingerprintInterceptor,
            tkpdBaseInterceptor,
            chuckInterceptor,
            debugInterceptor
        )
    }

    @Provides
    fun provideAbstractionRouter(@ApplicationContext context: Context?): AbstractionRouter {
        return context as AbstractionRouter
    }

    @Provides
    fun provideTopAdsAuthInterceptor(
        @ApplicationContext context: Context,
        abstractionRouter: NetworkRouter,
        userSession: UserSessionInterface
    ): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(
            context,
            abstractionRouter, userSession
        )
    }

    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_USER_SESION)
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideFingerprintInterceptor(
        networkRouter: NetworkRouter,
        @Named(ConstantCategoryCommon.CATEGORY_PICKER_USER_SESION) userSession: UserSessionInterface
    ): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Named(ConstantCategoryCommon.CATEGORY_PICKER_CHUCK)
    @Provides
    fun provideChuckInterceptor(
        @ApplicationContext context: Context,
        @Named(ConstantCategoryCommon.CHUCK_ENABLED) localCacheHandler: LocalCacheHandler
    ): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context,
            localCacheHandler.getBoolean(ConstantCategoryCommon.IS_CHUCK_ENABLED, false)
        )
        return ChuckerInterceptor(context, collector)
    }

    @Named(ConstantCategoryCommon.CHUCK_ENABLED)
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, ConstantCategoryCommon.CHUCK_ENABLED)
    }

    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
    }

    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @Provides
    fun provideCategoryDao(@ApplicationContext context: Context): CategoryDao {
        return getInstance(context).getCategoryDao()
    }


    @Provides
    fun provideRxGQLUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    fun povideGetCatagoryLiteTreeUseCase(graphqlUseCase: GraphqlUseCase): GetCategoryLiteTreeUseCase {
        return GetCategoryLiteTreeUseCase(graphqlUseCase)
    }
}