package com.tokopedia.shop.info.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.info.data.GQLQueryStringConst
import com.tokopedia.shop.info.di.scope.ShopInfoScope
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

@Module(includes = [ShopInfoViewModelModule::class])
class ShopInfoModule {
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @ShopInfoScope
    @Provides
    fun provideShopNoteViewModel(): ShopNoteUiModel {
        return ShopNoteUiModel()
    }

    @ShopInfoScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopInfoScope
    @Named(GQLQueryNamedConstant.SHOP_NOTES_BY_SHOP_ID)
    @Provides
    fun getGqlQueryShopNotesByShopId(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_get_shop_notes_by_shop_id)
    }

    @ShopInfoScope
    @IntoMap
    @StringKey(GQLQueryStringConst.GET_SHOP_PACK_SPEED)
    @Provides
    fun getStringQueryShopPackSpeed(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_package_process)
    }

    @ShopInfoScope
    @IntoMap
    @StringKey(GQLQueryStringConst.GET_SHOP_RATING)
    @Provides
    fun getStringQueryShopRating(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_get_shop_rating)
    }

    @ShopInfoScope
    @IntoMap
    @StringKey(GQLQueryStringConst.GET_SHOP_SATISFACTION)
    @Provides
    fun getStringQueryShopSatisfaction(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_get_shop_satisfaction)
    }

    @ShopInfoScope
    @Provides
    fun provideGetShopNotesByShopIdUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                           @Named(GQLQueryNamedConstant.SHOP_NOTES_BY_SHOP_ID) gqlQuery: String?): GetShopNotesByShopIdUseCase {
        return GetShopNotesByShopIdUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @ShopInfoScope
    @Provides
    fun provideGetShopStatisticUseCase(graphqlUseCase: MultiRequestGraphqlUseCase,
                                       queries: Map<String, String>): GetShopStatisticUseCase {
        return GetShopStatisticUseCase(queries, graphqlUseCase)
    }


    @ShopInfoScope
    @Provides
    fun getCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }
}