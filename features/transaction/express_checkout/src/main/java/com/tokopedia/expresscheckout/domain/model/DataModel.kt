package com.tokopedia.expresscheckout.domain.model;

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class DataModel(
        var errors: ArrayList<String>? = null,
        var errorCode: Int = 0,
        var success: Int = 0,
        var cartModel: CartModel? = null,
        var isCouponActive: Int = 0,
        var keroToken: String? = null,
        var keroDiscomToken: String? = null,
        var keroUnixTime: Long = 0,
        var enablePartialCancel: Boolean? = null,
        var donationModel: DonationModel? = null,
        var promoSuggestionModel: PromoSuggestionModel? = null,
        var autoapply: AutoApplyModel? = null,
        var userProfileModelDefault: UserProfileModel? = null,
        var messagesModel: MessagesModel? = null,
        var maxQuantity: Int = 0,
        var maxCharNote: Int = 0
)