package com.tokopedia.feedcomponent.shoprecom.di

import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapperImpl
import dagger.Module
import dagger.Provides

/**
 * created by fachrizalmrsln on 14/10/22
 **/
@Module
class ShopRecomModule {

    @Provides
    fun provideShopRecommendationUiMapper(): ShopRecomUiMapper{
        return ShopRecomUiMapperImpl()
    }

}
