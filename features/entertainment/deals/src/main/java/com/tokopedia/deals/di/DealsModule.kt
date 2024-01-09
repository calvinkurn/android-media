package com.tokopedia.deals.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.utils.DealsLocationUtils
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by jessica on 11/06/20
 */

@Module
class DealsModule(val context: Context) {

    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 100
    private val NET_WRITE_TIMEOUT = 100
    private val NET_CONNECT_TIMEOUT = 100
    private val NET_RETRY = 3

    @DealsQualifier
    @Provides
    fun provideDealsContext() = context

    @ActivityScope
    @Provides
    fun provideDispatcher(): Dispatchers = Dispatchers

    @ActivityScope
    @Provides
    fun provideMainDispacther(): CoroutineDispatcher = Dispatchers.Main

    @ActivityScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @ActivityScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat(GSON_DATE_FORMAT)
            .setPrettyPrinting()
            .serializeNulls()
            .create()
    }

    @ActivityScope
    @DealsQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    @Provides
    @ActivityScope
    fun provideNetworkRouter(@DealsQualifier context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ActivityScope
    @Provides
    fun provideTkpdOldAuthInterceptor(
        @DealsQualifier context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ): TkpdOldAuthInterceptor {
        return TkpdOldAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ActivityScope
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(StringResponseConverter())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@DealsQualifier context: Context): UserSessionInterface = UserSession(context)

    @ActivityScope
    @Provides
    fun provideIrisSession(@DealsQualifier context: Context): IrisSession = IrisSession(context)

    @ActivityScope
    @Provides
    fun provideDealsLocationUtil(@DealsQualifier context: Context): DealsLocationUtils = DealsLocationUtils(context)

    @ActivityScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
        MultiRequestGraphqlUseCase(graphqlRepository)

    @Provides
    fun provideGraphqlUseCaseDealsSearch(graphqlRepository: GraphqlRepository): GraphqlUseCase<SearchData> =
        GraphqlUseCase(graphqlRepository)
}
