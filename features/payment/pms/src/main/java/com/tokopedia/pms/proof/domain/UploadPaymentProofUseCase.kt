package com.tokopedia.pms.proof.domain

import android.util.Base64
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.pms.proof.model.PaymentProofResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.RequestParams
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
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

class UploadPaymentProofUseCase @Inject constructor() : RestRequestUseCase() {

    private lateinit var imageFilePath: String
    private lateinit var merchantCode: String
    private lateinit var paymentId: String

    private val PROOF_UPLOAD_END_POINT = "/scrooge/payment-proof/upload"

    private fun generateRequestParams(): HashMap<String, Any>? {
        val requestBodyMap = HashMap<String, Any>()

        val reqImgFile: RequestBody = File(imageFilePath)
            .asRequestBody(MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
        try {
            requestBodyMap[PARAMS_UPLOAD_FILE + PARAMS_UPLOAD_FILE_NAME + URLEncoder.encode(
                imageFilePath,
                ENCODING_UTF_8
            )] = reqImgFile
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        requestBodyMap[PARAMS_PAYMENT_ID] = paymentId
            .toRequestBody(MEDIA_TYPE_TEXT.toMediaTypeOrNull())
        requestBodyMap[PARAMS_UPLOAD_SIGNATURE] = getSignature()
            .toRequestBody(MEDIA_TYPE_TEXT.toMediaTypeOrNull())
        requestBodyMap[PARAMS_MERCHANT_CODE] = merchantCode
            .toRequestBody(MEDIA_TYPE_TEXT.toMediaTypeOrNull())
        requestBodyMap[PARAM_IS_TEST] = false.toString()
            .toRequestBody(MEDIA_TYPE_TEXT.toMediaTypeOrNull())
        return requestBodyMap
    }

    private fun getSignature(): String {
        return try {
            val str = "$paymentId$merchantCode".toLowerCase()
            val data: ByteArray = str.toByteArray(charset(ENCODING_UTF_8))
            Base64.encodeToString(data, Base64.NO_WRAP)
        } catch (e: Exception) {
            ""
        }
    }

    fun setRequestParams(paymentId: String, merchantCode: String, imagePath: String) {
        imageFilePath = imagePath
        this.merchantCode = merchantCode
        this.paymentId = paymentId
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest: MutableList<RestRequest> = ArrayList()
        val restRequest = RestRequest.Builder(TokopediaUrl.getInstance().PAY
                + PROOF_UPLOAD_END_POINT, PaymentProofResponse::class.java)
                .setBody(generateRequestParams())
                .setRequestType(RequestType.POST_MULTIPART)
                .build()
        tempRequest.add(restRequest)
        return tempRequest
    }
}
