package com.tokopedia.picker.common.di

import com.tokopedia.picker.common.data.repository.TestAlbumRepository
import com.tokopedia.picker.common.data.repository.TestMediaRepository
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.entity.Media
import javax.inject.Inject

class TestPickerInterceptor {

    @Inject lateinit var albumRepository: TestAlbumRepository

    @Inject lateinit var mediaRepository: TestMediaRepository

    fun mockAlbum(data: List<Album>) {
        albumRepository.data = data.toMutableList()
    }

    fun mockMedia(data: List<Media>) {
        mediaRepository.data = data.toMutableList()
    }

}