package com.tokopedia.broadcaster.log.di.module

import android.content.Context
import com.tokopedia.broadcaster.log.data.BroadcasterChuckerDb
import com.tokopedia.broadcaster.log.data.dao.NetworkLogDao
import com.tokopedia.broadcaster.log.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.log.data.repository.ChuckerLogRepositoryImpl
import com.tokopedia.broadcaster.log.di.scope.NetworkLogScope
import com.tokopedia.broadcaster.log.di.scope.FeatureContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class NetworkLogModule constructor(
    private val context: Context
) {

    @Provides
    @FeatureContext
    fun provideFeatureContext(): Context {
        return context
    }

    @Provides
    @NetworkLogScope
    fun provideDbBuilder(
        @FeatureContext context: Context
    ): BroadcasterChuckerDb {
        return BroadcasterChuckerDb.instance(context)
    }

    @Provides
    @NetworkLogScope
    fun provideDbDao(
        db: BroadcasterChuckerDb
    ): NetworkLogDao {
        return db.chuckerDao()
    }

    @Provides
    @NetworkLogScope
    fun provideChuckerRepository(
        dbDao: NetworkLogDao
    ): ChuckerLogRepository {
        return ChuckerLogRepositoryImpl(dbDao)
    }

    @Provides
    @NetworkLogScope
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}