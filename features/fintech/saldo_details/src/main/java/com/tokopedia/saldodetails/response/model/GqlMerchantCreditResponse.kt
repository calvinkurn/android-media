package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class GqlMerchantCreditResponse(
        @SerializedName("is_eligible")
        var isEligible: Boolean = false,

        @SerializedName("status")
        var status: Int = 0,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("main_redirect_url")
        var mainRedirectUrl: String? = null,

        @SerializedName("image_url")
        var logoURL: String? = null,

        @SerializedName("body_description")
        var bodyDesc: String? = null,

        @SerializedName("anchor_list")
        var anchorList: GqlAnchorListResponse = GqlAnchorListResponse(),

        @SerializedName("info_list")
        var infoList: ArrayList<GqlInfoListResponse> = ArrayList(),

        @SerializedName("show_box")
        var isShowBox: Boolean = false,

        @SerializedName("box_info")
        var boxInfo: GqlBoxInfoResponse?
)
