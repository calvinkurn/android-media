package com.tokopedia.sellerhome.di.module

import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import dagger.Module

@SellerHomeDashboardScope
@Module(includes = [SellerHomeQueryModule::class, SellerHomeUseCaseModule::class])
class SellerHomeDrawerModule {
}