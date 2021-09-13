package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.uploadsecure.UploadSecureResponse
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
private const val PARAMS_PAYMENT_ID = "payment_id"
private const val PARAMS_UPLOAD_SIGNATURE = "signature"
private const val PARAMS_MERCHANT_CODE = "merchant_code"
private const val PARAM_IS_TEST = "is_test"

private const val MEDIA_TYPE_TEXT = "text/plain"
private const val MEDIA_TYPE_IMAGE = "image/*"
private const val ENCODING_UTF_8 = "UTF-8"

class UploadPaymentProofUseCase2 @Inject constructor() : RestRequestUseCase() {

    private lateinit var imageFilePath: String
    private lateinit var messageId: String


    private fun generateRequestParams(): HashMap<String, Any>? {
        val requestBodyMap = HashMap<String, Any>()


        val reqImgFile: RequestBody =
            RequestBody.create(MediaType.parse(MEDIA_TYPE_IMAGE), File(imageFilePath))

        requestBodyMap["msg_id"] = RequestBody.create(MediaType.parse(MEDIA_TYPE_TEXT), messageId)
        requestBodyMap["file"] = reqImgFile

        return requestBodyMap
    }

    fun setRequestParams(messageId: String, imagePath: String) {
        imageFilePath = imagePath
        this.messageId = messageId
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest: MutableList<RestRequest> = ArrayList()
        val restRequest = RestRequest.Builder(
            "https://chat-staging.tokopedia.com/tc/v1/upload_secure",
            UploadSecureResponse::class.java
        )
            .setBody(generateRequestParams())
            .setRequestType(RequestType.POST_MULTIPART)
            .build()
        tempRequest.add(restRequest)
        return tempRequest
    }
}
