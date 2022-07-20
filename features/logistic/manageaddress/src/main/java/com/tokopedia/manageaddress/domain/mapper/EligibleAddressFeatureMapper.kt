package com.tokopedia.manageaddress.domain.mapper

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.manageaddress.domain.model.EligibleForAddressFeatureModel

object EligibleAddressFeatureMapper {
    fun mapResponseToModel(response: KeroAddrIsEligibleForAddressFeatureData, featureId: Int, data: RecipientAddressModel?) : EligibleForAddressFeatureModel {
        return EligibleForAddressFeatureModel(
            featureId = featureId,
            error = response.keroAddrError.detail,
            eligible = response.eligibleForRevampAna.eligible,
            data = data)
    }
}