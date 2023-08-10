package com.tokopedia.shop_nib.data.mapper

import com.tokopedia.shop_nib.data.response.UploadFileResponse
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import javax.inject.Inject

class UploadFileMapper @Inject constructor() {

    fun map(response: UploadFileResponse) : UploadFileResult {
        val success = response.header.errorCode == "200"
        val errorMessage = response.header.reason
        return UploadFileResult(success, errorMessage)
    }
}
