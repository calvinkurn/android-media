package com.tokopedia.purchase_platform.express_checkout.domain.model.profile

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ProfileDataModel(
        var profileModels: ArrayList<ProfileModel>? = null,
        var defaultProfileId: Int = 0
)