package com.tokopedia.createpost.producttag.view.uimodel

import com.tokopedia.shopwidget.shopcard.ShopCardModel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class ShopUiModel(
    val shopId: String = "",
    val shopName: String = "",
    val shopDomain: String = "",
    val shopUrl: String = "",
    val shopImage: String = "",
    val shopImage300: String = "",
    val shopDescription: String = "",
    val shopTagline: String = "",
    val shopLocation: String = "",
    val shopTotalTransaction: String = "",
    val shopTotalFavorite: String = "",
    val shopGoldShop: Int = 0,
    val shopIsOwner: Int = 0,
    val shopRateSpeed: Int = 0,
    val shopRateAccuracy: Int = 0,
    val shopRateService: Int = 0,
    val shopStatus: Int = 0,
    val reputationImageUri: String = "",
    val isOfficial: Boolean = false,
) {

    fun toShopCard(): ShopCardModel {
        return ShopCardModel(
            id = shopId,
            name = shopName,
            domain = shopDomain,
            url = shopUrl,
            image = shopImage,
            image300 = shopImage300,
            description = shopDescription,
            tagLine = shopTagline,
            location = shopLocation,
            totalTransaction = shopTotalTransaction,
            totalFavorite = shopTotalFavorite,
            goldShop = shopGoldShop,
            isOwner = shopIsOwner,
            rateSpeed = shopRateSpeed,
            rateService = shopRateService,
            rateAccuracy = shopRateAccuracy,
            status = shopStatus,
            reputationImageUri = reputationImageUri,
            isOfficial = isOfficial,
        )
    }
}