package com.tokopedia.power_merchant.subscribe.domain.usecase

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMShopInfoUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel
import com.tokopedia.power_merchant.subscribe.view.model.MembershipBasicInfoUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 25/05/22.
 */

class GetMembershipBasicInfoUseCase @Inject constructor(
    private val getPMShopInfoUseCase: GetPMShopInfoUseCase,
    private val getShopScoreUpdatePeriod: GetBenefitPackageUseCase
) {

    suspend fun execute(shopId: String): MembershipBasicInfoUiModel {
        return coroutineScope {
            val headerData = async { getHeaderData(shopId) }
            val shopInfoAsync = async { getShopInfo(shopId) }
            return@coroutineScope MembershipBasicInfoUiModel(
                headerData = headerData.await(),
                pmShopInfo = shopInfoAsync.await()
            )
        }
    }

    private suspend fun getHeaderData(shopId: String): BenefitPackageHeaderUiModel {
        getShopScoreUpdatePeriod.setParams(shopId.toLongOrZero())
        return getShopScoreUpdatePeriod.executeOnBackground()
    }

    private suspend fun getShopInfo(shopId: String): PMShopInfoUiModel {
        val params =
            GetPMShopInfoUseCase.createParams(shopId, PMConstant.PM_SETTING_INFO_SOURCE)
        getPMShopInfoUseCase.params = params
        return getPMShopInfoUseCase.executeOnBackground()
    }
}