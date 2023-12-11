package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.creation.common.upload.uploader.CreationUploader
import dagger.Module
import dagger.Provides
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on December 15, 2022
 */
@Module
class PlayShortsUploaderTestModule(
    private val mockCreationUploader: CreationUploader = mockk(relaxed = true)
) {

    @Provides
    fun provideCreationUploader(): CreationUploader = mockCreationUploader
}
