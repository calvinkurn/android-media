package com.tokopedia.updateinactivephone.data.repository

import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel

import okhttp3.RequestBody
import rx.Observable

interface UploadImageRepository {

    fun uploadImage(url: String, params: Map<String, String>, imageFile: RequestBody): Observable<UploadImageModel>

    fun getUploadHost(parameters: HashMap<String, Any>): Observable<UploadHostModel>

}
