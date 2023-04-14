package com.tokopedia.shop_nib.data.mapper

import com.tokopedia.shop_nib.data.response.UploadFileResponse
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import javax.inject.Inject

class UploadFileMapper @Inject constructor() {

    fun map(response: UploadFileResponse) : UploadFileResult {
        val status = response.data?.resultStatus?.code == "200"
        val errorMessage = response.data?.resultStatus?.message?.firstOrNull().orEmpty()
        return UploadFileResult(status, errorMessage)
    }
}
