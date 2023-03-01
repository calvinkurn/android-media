package com.tokopedia.shop.common.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
// import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.ShopPageConstant
// import com.tokopedia.shop.common.data.source.cloud.api.ShopApi
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop.common.di.ShopPageContext
// import com.tokopedia.shop.common.di.ShopQualifier
// import com.tokopedia.shop.common.di.scope.ShopScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
// import retrofit2.Retrofit

/**
 * @author sebastianuskh on 4/13/17.
 */
@Module(includes = [ShopCommonModule::class])
class ShopModule(val context: Context) {

    @Provides
    @ShopPageContext
    fun provideActivityContext() = context

//    @ShopScope
//    @Provides
//    fun provideShopApi(@ShopQualifier retrofit: Retrofit): ShopApi {
//        return retrofit.create(ShopApi::class.java)
//    }

    @Provides
    fun provideGqlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return getInstance().multiRequestGraphqlUseCase
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideTkpdAuthInterceptor(
        @ShopPageContext context: Context,
        userSession: UserSessionInterface,
        networkRouter: NetworkRouter
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @Provides
    fun provideShopPageSharedPref(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            ShopPageConstant.SHOP_PAGE_SHARED_PREFERENCE,
            Context.MODE_PRIVATE
        )
    }
}
