package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import dagger.Module
import dagger.Provides
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on December 15, 2022
 */
@Module
class PlayShortsUploaderTestModule(
    private val mockPlayShortsUploader: PlayShortsUploader = mockk(relaxed = true)
) {

    @Provides
    fun providePlayShortsUploader(): PlayShortsUploader = mockPlayShortsUploader
}
