package com.tokopedia.updateinactivephone.data.source

import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.network.api.UploadImageApi
import retrofit2.Retrofit

class CloudGetUploadHostSource(private val retrofit: Retrofit,
                               private val getUploadHostMapper: UploadHostMapper) {

    suspend fun getUploadHost(params: HashMap<String, Any>): UploadHostModel {
        return getUploadHostMapper.call(retrofit.create(UploadImageApi::class.java).getUploadHost(params))
    }
}
