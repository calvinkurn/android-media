package com.tokopedia.sellerhomedrawer.di.module

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhomedrawer.constant.SellerHomeParamConstant
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SellerHomeDashboardScope
@Module
class SellerHomeDashboardUseCaseModule {

    @Provides
    fun provideGraphQlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideFlashSaleGetSellerStatusUseCase(graphqlUseCase: GraphqlUseCase): FlashSaleGetSellerStatusUseCase =
        FlashSaleGetSellerStatusUseCase(graphqlUseCase)

    @Provides
    fun provideGetShopStatusUseCase(graphqlUseCase: GraphqlUseCase, @Named(SellerHomeParamConstant.RAW_GM_STATUS) rawQuery: String) :
            GetShopStatusUseCase = GetShopStatusUseCase(graphqlUseCase, rawQuery)

}