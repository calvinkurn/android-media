package com.tokopedia.expresscheckout.domain.model.profile

import com.tokopedia.expresscheckout.domain.model.HeaderModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ProfileResponseModel(
        var headerModel: HeaderModel? = null,
        var profileDataModel: ProfileDataModel? = null
)