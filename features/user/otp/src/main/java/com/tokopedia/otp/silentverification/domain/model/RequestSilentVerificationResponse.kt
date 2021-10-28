package com.tokopedia.otp.silentverification.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 17/10/21.
 */

data class RequestSilentVerificationResponse(
    @SerializedName("OTPRequest")
    @Expose
    var data: RequestSilentVerificationResult = RequestSilentVerificationResult()
)

data class RequestSilentVerificationResult(
    @SerializedName("success")
    @Expose
    var success: Boolean = false,
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("evurl")
    @Expose
    var evUrl: String = "",
    @SerializedName("token_id")
    @Expose
    var tokenId: String = "",
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String = "",
    @SerializedName("prefixMisscall")
    @Expose
    var prefixMisscall: String = "",
    @SerializedName("message_title")
    @Expose
    var messageTitle: String = "",
    @SerializedName("message_sub_title")
    @Expose
    var messageSubTitle: String = "",
    @SerializedName("message_img_link")
    @Expose
    var messageImgLink: String = "",
    @SerializedName("error_code")
    @Expose
    var errorCode: String = ""
)