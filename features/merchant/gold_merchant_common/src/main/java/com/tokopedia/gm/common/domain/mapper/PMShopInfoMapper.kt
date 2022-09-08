package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.data.source.cloud.model.GMShopInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class PMShopInfoMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(
        data: GMShopInfoResponse
    ): PMShopInfoUiModel {
        val response = data.goldGetPMShopInfo
        val shopInfoByID = data.shopInfoByID
        val nextUpdateInfo = data.nextUpdateInfo
        return PMShopInfoUiModel(
            shopCreatedDate = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty(),
            isNewSeller = response?.isNewSeller.orTrue(),
            is30DaysFirstMonday = response?.is30DaysFirstMonday.orFalse(),
            isKyc = response?.isKyc.orFalse(),
            kycStatusId = response?.kycStatusId?.toIntOrNull() ?: KYCStatusId.NOT_VERIFIED,
            shopScoreThreshold = response?.shopScoreThreshold
                ?: PMShopInfoUiModel.DEFAULT_PM_SHOP_SCORE_THRESHOLD,
            shopScorePmProThreshold = response?.shopScorePmProThreshold
                ?: PMShopInfoUiModel.DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD,
            shopAge = response?.shopAge.orZero(),
            shopLevel = response?.shopLevel.orZero(),
            hasActiveProduct = response?.hasActiveProduct.orFalse(),
            isEligiblePm = response?.isEligiblePm.orFalse(),
            isEligiblePmPro = response?.isEligiblePmPro.orFalse(),
            itemSoldOneMonth = response?.itemSoldOneMonth.orZero(),
            itemSoldPmProThreshold = response?.itemSoldPmProThreshold
                ?: PMShopInfoUiModel.DEFAULT_ORDER_THRESHOLD,
            netItemValueOneMonth = response?.nivOneMonth.orZero(),
            netItemValuePmProThreshold = response?.nivPmProThreshold
                ?: PMShopInfoUiModel.DEFAULT_NIV_THRESHOLD,
            nextMonthlyRefreshDate = nextUpdateInfo.nextMonthlyRefreshDate.orEmpty()
        )
    }
}