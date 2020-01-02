package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlDetailsResponse(
        @SerializedName("is_eligible")
        var isEligible: Boolean = false,

        @SerializedName("status")
        var status: Int = 0,

        @SerializedName("is_enabled")
        var isEnabled: Boolean = false,

        @SerializedName("show_new_logo")
        var isShowNewLogo: Boolean = false,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("show_toggle")
        var isShowToggle: Boolean = false,

        @SerializedName("box_type")
        var boxType: String? = null,

        @SerializedName("description")
        var description: String? = null,

        @SerializedName("box_right_arrow")
        var isShowRightArrow: Boolean = false,

        @SerializedName("box_title")
        var boxTitle: String? = null,

        @SerializedName("box_desc")
        var boxDesc: String? = null,

        @SerializedName("box_show_popup")
        var isBoxShowPopup: Boolean = false,

        @SerializedName("popup_title")
        var popupTitle: String? = null,

        @SerializedName("popup_desc")
        var popupDesc: String? = null,

        @SerializedName("popup_button_text")
        var popupButtonText: String? = null,

        @SerializedName("infoList")
        var infoList: List<GqlInfoListResponse>? = null,

        @SerializedName("anchorList")
        var anchorList: List<GqlSpAnchorListResponse>? = null
)
