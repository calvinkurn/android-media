package com.tokopedia.play_common.shortsuploader.di

import android.content.Context
import androidx.work.WorkManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.PlayShortsUploaderImpl
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalyticImpl
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

    @Provides
    fun providePlayShortsUploadAnalytic(): PlayShortsUploadAnalytic {
        return PlayShortsUploadAnalyticImpl()
    }
}
