package com.tokopedia.updateinactivephone.data.repository

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam
import com.tokopedia.updateinactivephone.data.factory.UploadImageSourceFactory
import com.tokopedia.updateinactivephone.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.model.request.UploadImageModel

import javax.inject.Inject

import okhttp3.RequestBody
import rx.Observable

class UploadImageRepositoryImpl @Inject
constructor(private val uploadImageSourceFactory: UploadImageSourceFactory) : UploadImageRepository {

    override fun uploadImage(url: String, params: Map<String, String>, imageFile: RequestBody): Observable<UploadImageModel> {
        return uploadImageSourceFactory
                .createCloudUploadImageDataStore()
                .uploadImage(
                        url,
                        params,
                        imageFile)
    }

    override fun getUploadHost(parameters: TKPDMapParam<String, Any>): Observable<UploadHostModel> {
        return uploadImageSourceFactory
                .createCloudUploadHostDataStore()
                .getUploadHost(parameters)
    }

}
