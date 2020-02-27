package com.tokopedia.updateinactivephone.data.repository

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam
import com.tokopedia.updateinactivephone.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.model.request.UploadImageModel

import okhttp3.RequestBody
import rx.Observable

interface UploadImageRepository {

    fun uploadImage(url: String, params: Map<String, String>, imageFile: RequestBody): Observable<UploadImageModel>

    fun getUploadHost(parameters: TKPDMapParam<String, Any>): Observable<UploadHostModel>

}
