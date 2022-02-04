package com.tokopedia.media.picker.common.di

import android.content.Context
import com.tokopedia.media.picker.common.data.repository.TestAlbumRepository
import com.tokopedia.media.picker.common.data.repository.TestMediaRepository
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.repository.MediaRepositoryImpl
import com.tokopedia.media.picker.ui.PickerParam
import javax.inject.Inject

class TestPickerInterceptor {

    @Inject lateinit var albumRepository: TestAlbumRepository

    @Inject lateinit var mediaRepository: TestMediaRepository

    fun mockAlbum(data: List<Album>) {
        albumRepository.data = data.toMutableList()
    }

    suspend fun realMedia(context: Context, bucketId: Long, param: PickerParam) {
        val real = MediaRepositoryImpl(context)
        mediaRepository.data = real(bucketId, param).toMutableList()
    }

    fun mockMedia(data: List<Media>) {
        mediaRepository.data = data.toMutableList()
    }

}