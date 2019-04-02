package com.tokopedia.expresscheckout.domain.model.atc;

import com.tokopedia.expresscheckout.domain.model.profile.ProfileModel

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
        var userProfileModelDefaultModel: ProfileModel? = null,
        var messagesModel: HashMap<String, String>? = null,
        var maxQuantity: Int = 0,
        var maxCharNote: Int = 0
)