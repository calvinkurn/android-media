package com.tokopedia.promotionstarget.data.di.modules

import android.content.Context
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.di.GRATIFF_NOTIFICATION
import com.tokopedia.promotionstarget.data.di.HACHIKO_COUPON_DETAIL
import com.tokopedia.promotionstarget.data.gql.GraphqlHelper
import com.tokopedia.promotionstarget.domain.presenter.DialogCreator
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Named

@Module
class TestGratiffPresenterModule {

    @Provides
    @Named(GRATIFF_NOTIFICATION)
    fun provideGratiffNotification(context: Context): String = ""

    @Provides
    @Named(HACHIKO_COUPON_DETAIL)
    fun provideHachikoCouponDetail(context: Context): String = ""

    @Provides
    fun provideDialogCreator() :DialogCreator = mockk()

}