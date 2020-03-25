package com.tokopedia.updateinactivephone.data.source

import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel

class CloudGetUploadHostSource(private val uploadHostService: UploadImageService,
                               private val getUploadHostMapper: UploadHostMapper) {

    suspend fun getUploadHost(params: HashMap<String, Any>): UploadHostModel {
        return getUploadHostMapper.call(uploadHostService.api.getUploadHost(params))
    }
}
