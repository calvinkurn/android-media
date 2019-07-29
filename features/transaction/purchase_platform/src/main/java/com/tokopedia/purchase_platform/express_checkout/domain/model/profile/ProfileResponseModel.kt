package com.tokopedia.purchase_platform.express_checkout.domain.model.profile

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ProfileResponseModel(
        var status: String? = null,
        var errorMessage: ArrayList<String>? = null,
        var profileDataModel: ProfileDataModel? = null
)