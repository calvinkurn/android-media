package com.tokopedia.sellerhomedrawer.di.module

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetSellerHomeUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerTokoCashUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SellerHomeDashboardScope
@Module
class SellerHomeDashboardUseCaseModule {

    @Provides
    fun provideGraphQlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @SellerHomeDashboardScope
    @Provides
    fun provideFlashSaleGetSellerStatusUseCase(graphqlUseCase: GraphqlUseCase): FlashSaleGetSellerStatusUseCase =
        FlashSaleGetSellerStatusUseCase(graphqlUseCase)

    @SellerHomeDashboardScope
    @Provides
    fun provideGetShopStatusUseCase(graphqlUseCase: GraphqlUseCase, @Named(SellerHomeParamConstant.RAW_GM_STATUS) rawQuery: String) :
            GetShopStatusUseCase = GetShopStatusUseCase(graphqlUseCase, rawQuery)

    @SellerHomeDashboardScope
    @Provides
    fun provideGetSellerHomeUserAttributesUseCase(graphqlUseCase: GraphqlUseCase, @Named(SellerHomeParamConstant.SELLER_DRAWER_DATA) rawQuery: String) :
            GetSellerHomeUserAttributesUseCase = GetSellerHomeUserAttributesUseCase(graphqlUseCase, rawQuery)

    @SellerHomeDashboardScope
    @Provides
    fun provideSellerTokoCashUseCase(): SellerTokoCashUseCase =
            SellerTokoCashUseCase()

}