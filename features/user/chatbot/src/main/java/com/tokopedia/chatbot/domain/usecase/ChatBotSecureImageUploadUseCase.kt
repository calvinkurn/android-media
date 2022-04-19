package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.uploadsecure.UploadSecureResponse
import com.tokopedia.chatbot.util.SecureImageUploadUrl
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.usecase.RequestParams
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

private const val MEDIA_TYPE_TEXT = "text/plain"
private const val MEDIA_TYPE_IMAGE = "image/*"
private const val ENCODING_UTF_8 = "UTF-8"
private const val MSG_ID = "msg_id"

class ChatBotSecureImageUploadUseCase @Inject constructor() : RestRequestUseCase() {

    private lateinit var imageFilePath: String
    private lateinit var messageId: String


    private fun generateRequestParams(): HashMap<String, RequestBody>? {
        val requestBodyMap = HashMap<String, RequestBody>()


        val reqImgFile: RequestBody = File(imageFilePath)
            .asRequestBody(MEDIA_TYPE_IMAGE.toMediaTypeOrNull())

        try {
            requestBodyMap["file" + "\"; filename=\"" + URLEncoder.encode(
                imageFilePath,
                ENCODING_UTF_8
            )] =
                reqImgFile
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        requestBodyMap[MSG_ID] = messageId.toRequestBody(MEDIA_TYPE_TEXT.toMediaTypeOrNull())

        return requestBodyMap
    }

    fun setRequestParams(messageId: String, imagePath: String) {
        imageFilePath = imagePath
        this.messageId = messageId
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest: MutableList<RestRequest> = ArrayList()
        val restRequest = RestRequest.Builder(
                SecureImageUploadUrl.getUploadSecureUrl(),
                UploadSecureResponse::class.java
        )
                .setBody(generateRequestParams())
                .setRequestType(RequestType.POST_MULTIPART)
                .build()
        tempRequest.add(restRequest)
        return tempRequest
    }
}

