package com.tokopedia.feedcomponent.shoprecom.di

import android.content.Context
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapperImpl
import dagger.Module
import dagger.Provides

@Module
class ShopRecomModule {

    @Provides
    fun provideShopRecommendationUiMapper(): ShopRecomUiMapper{
        return ShopRecomUiMapperImpl()
    }

}
