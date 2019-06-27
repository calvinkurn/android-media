package com.tokopedia.expresscheckout.view.profile.mapper

import com.tokopedia.expresscheckout.domain.model.profile.ProfileModel
import com.tokopedia.expresscheckout.domain.model.profile.ProfileResponseModel
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

class ViewModelMapper @Inject constructor() : DataMapper {

    override fun convertToViewModels(profileResponseModel: ProfileResponseModel): ArrayList<ProfileViewModel> {
        val profileViewModels = ArrayList<ProfileViewModel>()
        if (profileResponseModel.profileDataModel != null && profileResponseModel.profileDataModel?.profileModels?.isNotEmpty() == true) {
            val profileModels = profileResponseModel.profileDataModel?.profileModels
            if (profileModels != null) {
                for ((index, profileModel: ProfileModel) in profileModels.withIndex()) {
                    val profileViewModel = ProfileViewModel()
                    profileViewModel.profileId = profileModel.id
                    profileViewModel.addressId = profileModel.addressModel?.addressId ?: 0
                    profileViewModel.addressTitle = profileModel.addressModel?.addressName ?: ""
                    profileViewModel.addressDetail = profileModel.addressModel?.addressStreet ?: ""
                    profileViewModel.durationId = profileModel.shipmentModel?.serviceId ?: 0
                    profileViewModel.durationDetail = profileModel.shipmentModel?.serviceDuration ?: ""
                    profileViewModel.isMainTemplate = profileModel.id == profileResponseModel.profileDataModel?.defaultProfileId
                    profileViewModel.paymentImageUrl = profileModel.paymentModel?.image ?: ""
                    profileViewModel.paymentDetail = profileModel.paymentModel?.gatewayCode ?: ""
                    profileViewModel.templateTitle = (index + 1).toString()
                    profileViewModel.isSelected = false

                    profileViewModels.add(profileViewModel)
                }
            }
        }

        return profileViewModels
    }

}