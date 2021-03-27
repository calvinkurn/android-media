package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPMShopInfoDataModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class PMShopInfoMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(response: GoldGetPMShopInfoDataModel?): PMShopInfoUiModel {
        return PMShopInfoUiModel(
                shopId = response?.shopId.orZero().toString(),
                isNewSeller = response?.isNewSeller ?: true,
                shopAge = response?.shopAge.orZero(),
                isKyc = response?.isKyc ?: false,
                kycStatusId = response?.kycStatusId ?: KYCStatusId.NOT_VERIFIED,
                shopScore = response?.shopScore.orZero(),
                shopScoreThreshold = response?.shopScoreThreshold.orZero(),
                isEligibleShopScore = response?.isEligibleShopScore ?: false,
                hasActiveProduct = response?.hasActiveProduct ?: false,
                isEligiblePm = response?.isEligiblePm ?: false,
                shopLevel = response?.shopLevel.orZero()
        )
    }
}