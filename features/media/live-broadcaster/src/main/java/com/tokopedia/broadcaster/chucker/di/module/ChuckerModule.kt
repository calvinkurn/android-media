package com.tokopedia.broadcaster.chucker.di.module

import android.content.Context
import com.tokopedia.broadcaster.chucker.data.BroadcasterChuckerDb
import com.tokopedia.broadcaster.chucker.data.dao.ChuckerDao
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepositoryImpl
import com.tokopedia.broadcaster.chucker.di.scope.ChuckerScope
import com.tokopedia.broadcaster.chucker.di.scope.FeatureContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ChuckerModule constructor(
    private val context: Context
) {

    @Provides
    @FeatureContext
    fun provideFeatureContext(): Context {
        return context
    }

    @Provides
    @ChuckerScope
    fun provideDbBuilder(
        @FeatureContext context: Context
    ): BroadcasterChuckerDb {
        return BroadcasterChuckerDb.instance(context)
    }

    @Provides
    @ChuckerScope
    fun provideDbDao(
        db: BroadcasterChuckerDb
    ): ChuckerDao {
        return db.chuckerDao()
    }

    @Provides
    @ChuckerScope
    fun provideChuckerRepository(
        dbDao: ChuckerDao
    ): ChuckerLogRepository {
        return ChuckerLogRepositoryImpl(dbDao)
    }

    @Provides
    @ChuckerScope
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}