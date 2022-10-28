package com.tokopedia.devicefingerprint.datavisor.repository

import android.content.Context
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorRepository
import com.tokopedia.devicefingerprint.di.DeviceFingerprintScope
import dagger.Module
import dagger.Provides

@Module
class DataVisorRepositoryModule {

    @Provides
    @DeviceFingerprintScope
    fun provideDataVisorRepository(
        @DeviceFingerprintScope
        context: Context
    ): DataVisorRepository {
        return DataVisorSharedPreferences(context)
    }
}