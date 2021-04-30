package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPMShopInfoDataModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class PMShopInfoMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(response: GoldGetPMShopInfoDataModel?): PMShopInfoUiModel {
        return PMShopInfoUiModel(
                isNewSeller = response?.isNewSeller.orTrue(),
                shopAge = response?.shopAge.orZero(),
                isKyc = response?.isKyc.orFalse(),
                kycStatusId = response?.kycStatusId ?: KYCStatusId.NOT_VERIFIED,
                shopScore = response?.shopScore.orZero(),
                shopScoreThreshold = response?.shopScoreThreshold ?: PMShopInfoUiModel.DEFAULT_PM_SHOP_SCORE_THRESHOLD,
                shopScorePmProThreshold = response?.shopScorePmProThreshold ?: PMShopInfoUiModel.DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD,
                hasActiveProduct = response?.hasActiveProduct.orFalse(),
                isEligiblePm = response?.isEligiblePm.orFalse(),
                isEligiblePmPro = response?.isEligiblePmPro.orFalse(),
                shopLevel = response?.shopLevel.orZero(),
                itemSoldOneMonth = response?.itemSoldOneMonth.orZero(),
                itemSoldPmProThreshold = response?.itemSoldPmProThreshold ?: PMShopInfoUiModel.DEFAULT_ORDER_THRESHOLD,
                nivOneMonth = response?.nivOneMonth.orZero(),
                nivPmProThreshold = response?.nivPmProThreshold ?: PMShopInfoUiModel.DEFAULT_NIV_THRESHOLD
        )
    }
}