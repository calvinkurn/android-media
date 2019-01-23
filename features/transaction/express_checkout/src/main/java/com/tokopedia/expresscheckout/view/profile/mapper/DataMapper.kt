package com.tokopedia.expresscheckout.view.profile.mapper

import com.tokopedia.expresscheckout.domain.model.profile.ProfileResponseModel
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

interface DataMapper {

    fun convertToViewModels(profileResponseModel: ProfileResponseModel): ArrayList<ProfileViewModel>
}