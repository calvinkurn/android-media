package com.tokopedia.sellerhomedrawer.di.module

import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import dagger.Module

@SellerHomeDashboardScope
@Module(includes = [SellerHomeDashboardUseCaseModule::class, SellerHomeDashboardQueryModule::class, SellerHomeDashboardRepositoryModule::class, BaseModule::class])
class ServiceModule