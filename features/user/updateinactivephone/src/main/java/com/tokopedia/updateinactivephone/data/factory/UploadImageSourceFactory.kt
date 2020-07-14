package com.tokopedia.updateinactivephone.data.factory

import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper
import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper
import com.tokopedia.updateinactivephone.data.source.CloudGetUploadHostSource
import com.tokopedia.updateinactivephone.data.source.CloudUploadImageDataSource
import retrofit2.Retrofit

import javax.inject.Inject

class UploadImageSourceFactory @Inject constructor(
        private val retrofit: Retrofit,
        private val uploadImageMapper: UploadImageMapper,
        private val uploadHostMapper: UploadHostMapper) {

    fun createCloudUploadImageDataStore(): CloudUploadImageDataSource {
        return CloudUploadImageDataSource(retrofit, uploadImageMapper)
    }

    fun createCloudUploadHostDataStore(): CloudGetUploadHostSource {
        return CloudGetUploadHostSource(retrofit, uploadHostMapper)
    }

}
