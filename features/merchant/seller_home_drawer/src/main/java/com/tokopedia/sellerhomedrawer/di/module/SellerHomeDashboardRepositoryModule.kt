package com.tokopedia.sellerhomedrawer.di.module

import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.domain.factory.NotificationSourceFactory
import com.tokopedia.sellerhomedrawer.domain.repository.NotificationRepository
import com.tokopedia.sellerhomedrawer.domain.repository.NotificationRepositoryImpl
import dagger.Module
import dagger.Provides

@SellerHomeDashboardScope
@Module
class SellerHomeDashboardRepositoryModule {

    @SellerHomeDashboardScope
    @Provides
    fun provideNotificationRepository(notificationSourceFactory: NotificationSourceFactory): NotificationRepository {
        return NotificationRepositoryImpl(notificationSourceFactory)
    }

}