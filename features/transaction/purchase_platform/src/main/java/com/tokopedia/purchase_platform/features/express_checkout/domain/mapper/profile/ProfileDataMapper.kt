package com.tokopedia.purchase_platform.features.express_checkout.domain.mapper.profile

import com.tokopedia.purchase_platform.features.express_checkout.domain.model.profile.ProfileResponseModel
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.profile.ProfileResponse

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface ProfileDataMapper {

    fun convertToDomainModel(profileResponse: ProfileResponse): ProfileResponseModel

}