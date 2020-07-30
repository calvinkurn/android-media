package com.tokopedia.shop.info.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.shop.info.data.GQLQueryStringConst
import com.tokopedia.shop.info.di.scope.ShopInfoScope
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.shop.note.data.repository.ShopNoteRepositoryImpl
import com.tokopedia.shop.note.data.source.ShopNoteDataSource
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

@ShopInfoScope
@Module(includes = [ShopInfoViewModelModule::class])
class ShopInfoModule {
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @ShopInfoScope
    @Provides
    fun provideShopNoteRepository(shopNoteDataSource: ShopNoteDataSource?): ShopNoteRepository {
        return ShopNoteRepositoryImpl(shopNoteDataSource)
    }

    @ShopInfoScope
    @Provides
    fun provideShopNoteViewModel(): ShopNoteViewModel {
        return ShopNoteViewModel()
    }

    @ShopInfoScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopInfoScope
    @Named(GQLQueryNamedConstant.SHOP_NOTES_BY_SHOP_ID)
    @Provides
    fun getGqlQueryShopNotesByShopId(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_notes_by_shop_id)
    }

    @ShopInfoScope
    @IntoMap
    @StringKey(GQLQueryStringConst.GET_SHOP_PACK_SPEED)
    @Provides
    fun getStringQueryShopPackSpeed(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_package_process)
    }

    @ShopInfoScope
    @IntoMap
    @StringKey(GQLQueryStringConst.GET_SHOP_RATING)
    @Provides
    fun getStringQueryShopRating(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_rating)
    }

    @ShopInfoScope
    @IntoMap
    @StringKey(GQLQueryStringConst.GET_SHOP_SATISFACTION)
    @Provides
    fun getStringQueryShopSatisfaction(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_satisfaction)
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
}