package com.tokopedia.purchase_platform.express_checkout.view.profile.mapper

import com.tokopedia.purchase_platform.express_checkout.domain.model.profile.ProfileResponseModel
import com.tokopedia.purchase_platform.express_checkout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

interface DataMapper {

    fun convertToViewModels(profileResponseModel: ProfileResponseModel): ArrayList<ProfileViewModel>
}