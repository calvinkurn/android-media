package com.tokopedia.purchase_platform.express_checkout.domain.mapper.profile

import com.tokopedia.purchase_platform.express_checkout.domain.model.profile.ProfileResponseModel
import com.tokopedia.transactiondata.entity.response.expresscheckout.profile.ProfileResponse

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface ProfileDataMapper {

    fun convertToDomainModel(profileResponse: ProfileResponse): ProfileResponseModel

}