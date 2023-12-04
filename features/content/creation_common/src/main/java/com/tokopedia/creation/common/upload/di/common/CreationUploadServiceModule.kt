package com.tokopedia.creation.common.upload.di.common

import android.content.Context
import androidx.work.WorkManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on September 19, 2023
 */
@Module
class CreationUploadServiceModule {

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
