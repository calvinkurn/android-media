package com.tokopedia.updateinactivephone.data.factory

import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper
import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService
import com.tokopedia.updateinactivephone.data.source.CloudGetUploadHostSource
import com.tokopedia.updateinactivephone.data.source.CloudUploadImageDataSource

import javax.inject.Inject

class UploadImageSourceFactory @Inject constructor(
        private val uploadImageService: UploadImageService,
        private val uploadImageMapper: UploadImageMapper,
        private val uploadHostMapper: UploadHostMapper) {

    fun createCloudUploadImageDataStore(): CloudUploadImageDataSource {
        return CloudUploadImageDataSource(uploadImageService, uploadImageMapper)
    }

    fun createCloudUploadHostDataStore(): CloudGetUploadHostSource {
        return CloudGetUploadHostSource(uploadImageService, uploadHostMapper)
    }

}
