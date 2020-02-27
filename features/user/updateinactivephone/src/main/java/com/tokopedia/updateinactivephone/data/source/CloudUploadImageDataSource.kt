package com.tokopedia.updateinactivephone.data.source

import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService
import com.tokopedia.updateinactivephone.model.request.UploadImageModel

import okhttp3.RequestBody
import rx.Observable

class CloudUploadImageDataSource(private val uploadImageService: UploadImageService,
                                 private val uploadImageMapper: UploadImageMapper) {

    fun uploadImage(url: String,
                    params: Map<String, String>,
                    imageFile: RequestBody): Observable<UploadImageModel> {
        return uploadImageService.api.uploadImage(
                url,
                params,
                imageFile)
                .map(uploadImageMapper)
    }
}
