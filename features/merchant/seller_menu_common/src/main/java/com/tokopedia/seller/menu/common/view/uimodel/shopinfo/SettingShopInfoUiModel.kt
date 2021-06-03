package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.seller.menu.common.view.uimodel.base.BalanceType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingSuccess
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingResponse
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.user.session.UserSessionInterface

data class SettingShopInfoUiModel(private val partialShopInfo: PartialSettingResponse,
                             private val partialTopAdsInfo: PartialSettingResponse,
                             private val userSession: UserSessionInterface): SettingSuccess() {

    val partialResponseStatus by lazy {
        Pair(
                partialShopInfo is PartialSettingSuccessInfoType,
                partialTopAdsInfo is PartialSettingSuccessInfoType
        )
    }

    val shopBadgeUiModel by lazy {
        (partialShopInfo as? PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo)?.let {
            ShopBadgeUiModel(it.shopBadgeUrl)
        }
    }
    val shopFollowersUiModel by lazy {
        (partialShopInfo as? PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo)?.let {
            ShopFollowersUiModel(it.totalFollowers)
        }
    }
    val shopStatusUiModel by lazy {
        (partialShopInfo as? PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo)?.let {
            ShopStatusUiModel(it.userShopInfoWrapper, userSession)
        }
    }

    val saldoBalanceUiModel by lazy {
        (partialTopAdsInfo as? PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo)?.let {
            BalanceUiModel(BalanceType.SALDO, it.othersBalance.totalBalance.orEmpty())
        }
    }

    val topadsBalanceUiModel by lazy {
        (partialTopAdsInfo as? PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo)?.let {
            TopadsBalanceUiModel(it.isTopAdsAutoTopup, it.topAdsBalance.getCurrencyFormatted())
        }
    }
}