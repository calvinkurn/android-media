package com.tokopedia.expresscheckout.domain.mapper.profile

import com.tokopedia.expresscheckout.data.entity.response.profile.ProfileResponse
import com.tokopedia.expresscheckout.domain.model.profile.ProfileResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface ProfileDataMapper {

    fun convertToDomainModel(profileResponse: ProfileResponse): ProfileResponseModel

}