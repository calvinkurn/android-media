package com.tokopedia.media.picker.common.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.common.data.repository.*
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import javax.inject.Inject

class TestPickerInterceptor {

    @Inject lateinit var albumRepository: TestAlbumRepository
    @Inject lateinit var mediaRepository: TestMediaRepository
    @Inject lateinit var deviceInfoRepository: TestDeviceInfoRepository
    @Inject lateinit var bitmapConverterRepository: TestBitmapConverterRepository
    @Inject lateinit var createMediaRepository: TestCreateMediaRepository

    @Inject lateinit var localDataSource: LoaderDataSource
    @Inject lateinit var dispatchers: CoroutineDispatchers

    fun mockAlbum(data: List<Album>) {
        albumRepository.data = data.toMutableList()
    }

    suspend fun realMedia(bucketId: Long) {
        val real = MediaRepository(localDataSource, dispatchers)
        mediaRepository.data = real(bucketId).toMutableList()
    }

    fun mockMedia(data: List<Media>) {
        mediaRepository.data = data.toMutableList()
    }

    fun mockStorageIsFull(isFull: Boolean) {
        deviceInfoRepository.isStorageAlmostFull = isFull
    }

}
