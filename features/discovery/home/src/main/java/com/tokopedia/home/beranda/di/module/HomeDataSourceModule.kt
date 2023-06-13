package com.tokopedia.home.beranda.di.module

import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.di.HomeScope
import dagger.Module
import dagger.Provides

@Module
class HomeDataSourceModule {
    @Provides
    fun provideHomeCachedDataSource(homeDao: HomeDao) = HomeRoomDataSource(homeDao)

    @HomeScope
    @Provides
    fun provideHomeDefaultDataSource() = HomeDefaultDataSource()
}
