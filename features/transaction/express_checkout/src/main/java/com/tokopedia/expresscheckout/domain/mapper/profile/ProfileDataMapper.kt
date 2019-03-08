package com.tokopedia.expresscheckout.domain.mapper.profile

import com.tokopedia.transactiondata.entity.response.expresscheckout.profile.ProfileResponse
import com.tokopedia.expresscheckout.domain.model.profile.ProfileResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface ProfileDataMapper {

    fun convertToDomainModel(profileResponse: ProfileResponse): ProfileResponseModel

}