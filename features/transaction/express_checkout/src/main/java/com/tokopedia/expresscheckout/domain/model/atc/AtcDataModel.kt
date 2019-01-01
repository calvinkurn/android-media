package com.tokopedia.expresscheckout.domain.model.atc;

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class AtcDataModel(
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
        var autoapplyModel: AutoApplyModel? = null,
        var userProfileModelDefaultModel: UserProfileModel? = null,
        var messagesModel: MessagesModel? = null,
        var maxQuantity: Int = 0,
        var maxCharNote: Int = 0
)