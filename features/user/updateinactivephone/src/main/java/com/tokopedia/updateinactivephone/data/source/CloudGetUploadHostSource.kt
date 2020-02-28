package com.tokopedia.updateinactivephone.data.source

import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel

import rx.Observable

class CloudGetUploadHostSource(private val uploadHostService: UploadImageService,
                               private val getUploadHostMapper: UploadHostMapper) {

    fun getUploadHost(params: HashMap<String, Any>): Observable<UploadHostModel> {
        return uploadHostService.api.getUploadHost(params)
                .map(getUploadHostMapper)
    }

}
