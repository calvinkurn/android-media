package com.tokopedia.promotionstarget.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.di.CLAIM_POP_GRATIFICATION
import com.tokopedia.promotionstarget.di.GET_COUPON_DETAIL
import com.tokopedia.promotionstarget.di.GET_POP_GRATIFICATION
import com.tokopedia.promotionstarget.di.scopes.PromoTargetScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@PromoTargetScope
@Module
class PromoTargetModule {

    @Provides
    @PromoTargetScope
    @Named(GET_POP_GRATIFICATION)
    fun provideGetPopGratification(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.query_get_pop_gratification)

    @Provides
    @PromoTargetScope
    @Named(CLAIM_POP_GRATIFICATION)
    fun provideClaimPopGratification(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.query_get_pop_gratification)

    @Provides
    @PromoTargetScope
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @PromoTargetScope
    @Named(GET_COUPON_DETAIL)
    fun provideCouponDetailString(context: Context): String = context.resources.openRawResource(R.raw.query_hachiko_catalog_detail)
            .bufferedReader()
            .readText()

}