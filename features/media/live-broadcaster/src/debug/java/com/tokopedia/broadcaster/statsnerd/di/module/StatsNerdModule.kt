package com.tokopedia.broadcaster.statsnerd.di.module

import android.content.Context
import com.tokopedia.broadcaster.statsnerd.data.BroadcasterChuckerDb
import com.tokopedia.broadcaster.statsnerd.data.dao.StatsNerdDao
import com.tokopedia.broadcaster.statsnerd.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.statsnerd.data.repository.ChuckerLogRepositoryImpl
import com.tokopedia.broadcaster.statsnerd.di.scope.StatsNerdScope
import com.tokopedia.broadcaster.statsnerd.di.scope.FeatureContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class StatsNerdModule constructor(
    private val context: Context
) {

    @Provides
    @FeatureContext
    fun provideFeatureContext(): Context {
        return context
    }

    @Provides
    @StatsNerdScope
    fun provideDbBuilder(
        @FeatureContext context: Context
    ): BroadcasterChuckerDb {
        return BroadcasterChuckerDb.instance(context)
    }

    @Provides
    @StatsNerdScope
    fun provideDbDao(
        db: BroadcasterChuckerDb
    ): StatsNerdDao {
        return db.chuckerDao()
    }

    @Provides
    @StatsNerdScope
    fun provideChuckerRepository(
        dbDao: StatsNerdDao
    ): ChuckerLogRepository {
        return ChuckerLogRepositoryImpl(dbDao)
    }

    @Provides
    @StatsNerdScope
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}