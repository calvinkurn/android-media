package com.tokopedia.content.common.uploader.di

import android.content.Context
import androidx.work.WorkManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.uploader.PlayShortsUploader
import com.tokopedia.content.common.uploader.PlayShortsUploaderImpl
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
@Module
class PlayShortsUploaderModule {

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun providePlayShortsUploader(workManager: WorkManager): PlayShortsUploader {
        return PlayShortsUploaderImpl(workManager)
    }
}
