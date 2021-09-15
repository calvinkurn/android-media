package com.tokopedia.gopay_kyc.domain.usecase

import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.usecase.RequestParams
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

private const val PARAMS_UPLOAD_FILE = "upl_file"
private const val PARAMS_UPLOAD_FILE_NAME = "\"; filename=\""
private const val PARAM_IS_TEST = "is_test"
private const val MEDIA_TYPE_TEXT = "text/plain"
private const val MEDIA_TYPE_IMAGE = "image/*"

class UploadKycUseCase @Inject constructor() : RestRequestUseCase() {

    private var imageFilePath: String = ""
    private var uploadUrl: String = ""

    private fun generateRequestParams(): RequestBody {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE_IMAGE), File(imageFilePath))
    }

    fun setRequestParams(documentUrl: String, imagePath: String) {
        uploadUrl = documentUrl
        imageFilePath = imagePath
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest: MutableList<RestRequest> = ArrayList()
        val restRequest = RestRequest.Builder("s3 url here", String::class.java)
                .setBody(generateRequestParams())
                .setRequestType(RequestType.PUT)
                .build()
        tempRequest.add(restRequest)
        return tempRequest
    }
}
