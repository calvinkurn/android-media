package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
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

    private suspend fun getSuspendSettingShopInfo(userId: Int): ShopInfo = withContext(Dispatchers.IO) {
        getSettingShopInfoUseCase.params = GetSettingShopInfoUseCase.createRequestParams(userId)
        return@withContext getSettingShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopType(shopId: Int): ShopType = withContext(Dispatchers.IO) {
        shopStatusTypeUseCase.params = ShopStatusTypeUseCase.createRequestParams(shopId)
        return@withContext shopStatusTypeUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopTotalFollowers(shopId: Int): Int = withContext(Dispatchers.IO) {
        getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
        return@withContext getShopTotalFollowersUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopBadge(shopId: Int): String = withContext(Dispatchers.IO) {
        getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
        return@withContext getShopBadgeUseCase.executeOnBackground()
    }

    private suspend fun getSuspendTopAdsDeposit(shopId: Int): Float = withContext(Dispatchers.IO) {
        topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(shopId)
        return@withContext topAdsDashboardDepositUseCase.executeOnBackground()
    }

    private suspend fun getSuspendTopAdsAutoTopup(shopId: String): Boolean = withContext(Dispatchers.IO) {
        topAdsAutoTopupUseCase.params = TopAdsAutoTopupUseCase.createRequestParams(shopId)
        return@withContext topAdsAutoTopupUseCase.executeOnBackground()
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
                    totalFollowers,
                    userSession)
        }
        return SettingShopInfoUiModel()
    }
}