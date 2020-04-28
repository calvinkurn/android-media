package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetAllShopInfoUseCase @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getSettingShopInfoUseCase: GetSettingShopInfoUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
        private val shopStatusTypeUseCase: ShopStatusTypeUseCase,
        private val topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
        private val topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase) : UseCase<SettingShopInfoUiModel>(){

    override suspend fun executeOnBackground(): SettingShopInfoUiModel = coroutineScope {
        userSession.run {
            val shopInfo = async { getSuspendSettingShopInfo(userId.toIntOrZero()) }
            val shopType = async { getSuspendShopType(shopId.toIntOrZero()) }
            val totalFollowers = async { getSuspendShopTotalFollowers(shopId.toIntOrZero()) }
            val shopBadgeUrl = async { getSuspendShopBadge(shopId.toIntOrZero()) }
            val topAdsDeposit = async { getSuspendTopAdsDeposit(shopId.toIntOrZero()) }
            val isTopAdsAutoTopup = async { getSuspendTopAdsAutoTopup(shopId) }
            return@coroutineScope mapToSettingShopInfo(
                    shopInfo.await(),
                    shopType.await(),
                    topAdsDeposit.await(),
                    isTopAdsAutoTopup.await(),
                    totalFollowers.await(),
                    shopBadgeUrl.await()
            )
        }
    }

    private suspend fun getSuspendSettingShopInfo(userId: Int): ShopInfo {
        getSettingShopInfoUseCase.params = GetSettingShopInfoUseCase.createRequestParams(userId)
        return getSettingShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopType(shopId: Int): ShopType {
        shopStatusTypeUseCase.params = ShopStatusTypeUseCase.createRequestParams(shopId)
        return shopStatusTypeUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopTotalFollowers(shopId: Int): Int {
        getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
        return getShopTotalFollowersUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopBadge(shopId: Int): String {
        getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
        return getShopBadgeUseCase.executeOnBackground()
    }

    private suspend fun getSuspendTopAdsDeposit(shopId: Int): Float {
        topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(shopId)
        return topAdsDashboardDepositUseCase.executeOnBackground()
    }

    private suspend fun getSuspendTopAdsAutoTopup(shopId: String): Boolean {
        topAdsAutoTopupUseCase.params = TopAdsAutoTopupUseCase.createRequestParams(shopId)
        return topAdsAutoTopupUseCase.executeOnBackground()
    }

    private fun mapToSettingShopInfo(shopInfo: ShopInfo,
                             shopStatusType: ShopType,
                             topAdsBalance: Float,
                             isTopAdsAutoTopup: Boolean,
                             totalFollowers: Int,
                             shopBadge: String): SettingShopInfoUiModel {
        shopInfo.shopInfoMoengage?.run {
            return SettingShopInfoUiModel(
                    info?.shopName.toEmptyStringIfNull(),
                    info?.shopAvatar.toEmptyStringIfNull(),
                    shopStatusType,
                    shopInfo.balance?.totalBalance ?: "",
                    topAdsBalance.getCurrencyFormatted(),
                    isTopAdsAutoTopup,
                    shopBadge,
                    totalFollowers)
        }
        return SettingShopInfoUiModel()
    }
}