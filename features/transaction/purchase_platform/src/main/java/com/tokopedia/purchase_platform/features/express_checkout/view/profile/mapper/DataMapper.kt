package com.tokopedia.purchase_platform.features.express_checkout.view.profile.mapper

import com.tokopedia.purchase_platform.features.express_checkout.domain.model.profile.ProfileResponseModel
import com.tokopedia.purchase_platform.features.express_checkout.view.profile.uimodel.ProfileUiModel

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

interface DataMapper {

    fun convertToViewModels(profileResponseModel: ProfileResponseModel): ArrayList<ProfileUiModel>
}