package com.tokopedia.topchat.chatroom.domain.pojo.voucher

import com.google.gson.annotations.SerializedName

data class TopChatRoomVoucherAttachmentDto(
    // Business related fields
    @SerializedName("voucher_id")
    val voucherId: String = "",
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("voucher_code")
    val voucherCode: String = "",
    @SerializedName("voucher_name")
    val voucherName: String = "",
    @SerializedName("minimum_spend")
    val minimumSpend: String = "",
    @SerializedName("valid_thru")
    val validThru: Long = 0,
    @SerializedName("valid_thru_str")
    val validThruStr: String = "",
    @SerializedName("desktop_url")
    val desktopUrl: String = "",
    @SerializedName("mobile_url")
    val mobileUrl: String = "",
    @SerializedName("amount")
    val amount: Float = 0f,
    @SerializedName("amount_type")
    val amountType: Int = 0,
    @SerializedName("identifier")
    val identifier: String = "",
    @SerializedName("voucher_type")
    val voucherType: Int = 0,
    @SerializedName("owner_id")
    val ownerId: String = "",
    @SerializedName("is_public")
    val isPublic: Int = 1,
    @SerializedName("is_lock_to_product")
    val isLockToProduct: Int? = 0,
    @SerializedName("applink")
    val appLink: String? = "",

    // UI related fields
    @SerializedName("voucher_amount_string")
    val voucherAmountString: String = "",
    @SerializedName("voucher_type_color")
    val voucherTypeColor: String = "",
    @SerializedName("voucher_type_color_dark")
    val voucherTypeColorDark: String = "",
    @SerializedName("voucher_type_string")
    val voucherTypeString: String = "",
    @SerializedName("voucher_minimum_string")
    val voucherMinimumString: String = "",
    @SerializedName("voucher_icon_url")
    val voucherIconUrl: String = "",
    @SerializedName("voucher_background_url")
    val voucherBackgroundUrl: String = "",
    @SerializedName("voucher_header")
    val voucherHeader: String = "",
    @SerializedName("voucher_description")
    val voucherDescription: String = ""
)
