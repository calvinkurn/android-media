package com.tokopedia.creation.common.di

import android.content.Context
import androidx.work.WorkManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Module
class CreationUploaderModule {

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
