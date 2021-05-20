package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProductTitleValidationResponse (
    @SerializedName("getProductTitleValidation")
    @Expose
    var getProductTitleValidation: GetProductTitleValidation = GetProductTitleValidation()
)

data class GetProductTitleValidation (
    @SerializedName("is_success")
    @Expose
    var isSuccess: Boolean = false,

    @SerializedName("blacklist_keyword")
    @Expose
    var blacklistKeyword: List<BlacklistKeyword> = emptyList(),

    @SerializedName("negative_keyword")
    @Expose
    var negativeKeyword: List<NegativeKeyword> = emptyList(),

    @SerializedName("typo_detection")
    @Expose
    var typoDetection: List<TypoDetection> = emptyList()
)

data class BlacklistKeyword (
    @SerializedName("keyword")
    @Expose
    var keyword: String = "",

    @SerializedName("status")
    @Expose
    var status: String = ""
)

class NegativeKeyword (
    @SerializedName("keyword")
    @Expose
    var keyword: String = "",

    @SerializedName("negative")
    @Expose
    var negative: List<String> = emptyList()
)

class TypoDetection (
    @SerializedName("incorrect")
    @Expose
    var incorrect: String = "",

    @SerializedName("correct")
    @Expose
    var correct: String = ""
)