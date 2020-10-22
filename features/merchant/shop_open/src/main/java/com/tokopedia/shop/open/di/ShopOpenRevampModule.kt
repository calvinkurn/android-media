package com.tokopedia.shop.open.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.common.GQLQueryConstant
import com.tokopedia.shop.open.common.ShopOpenDispatcherProvider
import com.tokopedia.shop.open.common.ShopOpenDispatcherProviderImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ShopOpenRevampViewModelModule::class])
@ShopOpenRevampScope
class ShopOpenRevampModule {

    @ShopOpenRevampScope
    @Provides
    fun providesGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @ShopOpenRevampScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @ShopOpenRevampScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ShopOpenRevampScope
    @Provides
    fun provideDispatcherProvider(): ShopOpenDispatcherProvider = ShopOpenDispatcherProviderImpl()

    @ShopOpenRevampScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopOpenRevampScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_GET_SURVEY_DATA)
    fun provideQueryShopOpenRevampGetSurveyData(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.shop_open_revamp_get_survey_data)
    }

    @ShopOpenRevampScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_CREATE_SHOP)
    fun provideQueryCreateShop(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.shop_open_revamp_create_shop)
    }

    @ShopOpenRevampScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_SEND_SURVEY_DATA)
    fun provideQuerySendSurvey(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.shop_open_revamp_send_survey_data)
    }

    @ShopOpenRevampScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_SAVE_SHIPMENT_LOCATION)
    fun provideQuerySaveShipmentLocation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.shop_open_revamp_saves_hipment_location)
    }

}