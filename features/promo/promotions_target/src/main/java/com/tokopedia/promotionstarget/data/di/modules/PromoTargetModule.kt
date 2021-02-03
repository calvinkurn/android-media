package com.tokopedia.promotionstarget.data.di.modules

import android.content.Context
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.di.*
import com.tokopedia.promotionstarget.data.gql.GraphqlHelper
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PromoTargetModule {

    @Provides
    @Named(GET_POP_GRATIFICATION)
    fun provideGetPopGratification(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.query_get_pop_gratification)

    @Provides
    @Named(CLAIM_POP_GRATIFICATION)
    fun provideClaimPopGratification(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.query_claim_pop_gratification)

    @Provides
    @Named(AUTO_APPLY)
    fun provideAutoApply(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_auto_apply)

    @Provides
    @Named(UPDATE_GRATIF)
    fun provideUpdateGratif(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_update_gratif_notification)

    @Provides
    @Named(GET_COUPON_DETAIL)
    fun provideCouponDetailString(context: Context): String = context.resources.openRawResource(R.raw.query_hachiko_catalog_detail)
            .bufferedReader()
            .readText()

    @Provides
    fun provideUserSession(context: Context): UserSession {
        return UserSession(context)
    }

}