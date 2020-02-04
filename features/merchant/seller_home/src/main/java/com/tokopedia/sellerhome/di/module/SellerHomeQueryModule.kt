package com.tokopedia.sellerhome.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SellerHomeScope
@Module
class SellerHomeQueryModule {

    @Provides
    @Named(SellerHomeParamConstant.RAW_GM_STATUS)
    fun provideGetShopStatusQuery(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_status)
    }

    @Provides
    @Named(SellerHomeParamConstant.SELLER_DRAWER_DATA)
    fun provideGetSellerDrawerDataQuery(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.sah_seller_drawer_data_query)
    }
}