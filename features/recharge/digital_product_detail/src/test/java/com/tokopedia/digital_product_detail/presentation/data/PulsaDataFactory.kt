package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.digital_product_detail.data.model.data.PersoRecommendationData
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.util.JsonToString
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class PulsaDataFactory {

    private val gson = Gson()

    fun getFavoriteNumberData(): TopupBillsPersoFavNumberData {
        return gson.fromJson(
            gson.JsonToString(GET_FAVORITE_NUMBER),
            TopupBillsPersoFavNumberData::class.java
        )
    }

    fun getPrefixOperatorData(): TelcoCatalogPrefixSelect {
        return gson.fromJson(
            gson.JsonToString(GET_PREFIX_OPERATOR),
            TelcoCatalogPrefixSelect::class.java
        )
    }

    fun getPrefixOperatorEmptyValData(): TelcoCatalogPrefixSelect {
        return gson.fromJson(
            gson.JsonToString(GET_PREFIX_OPERATOR_EMPTY_VALIDATION),
            TelcoCatalogPrefixSelect::class.java
        )
    }

    fun getRecommendationData(): DigitalDigiPersoGetPersonalizedItem {
        return gson.fromJson(
            gson.JsonToString(GET_RECOMMENDATION),
            DigitalDigiPersoGetPersonalizedItem::class.java
        )
    }

    fun getMenuDetail(): MenuDetailModel {
        return gson.fromJson(
            gson.JsonToString(GET_MENU_DETAIL),
            MenuDetailModel::class.java
        )
    }

    fun getCheckoutPassData(denomData: DenomData = getDenomData()): DigitalCheckoutPassData {
        return DigitalCheckoutPassData().apply {
            categoryId = denomData.categoryId
            clientNumber = VALID_CLIENT_NUMBER
            isPromo = denomData.promoStatus
            operatorId = OPERATOR_ID
            productId = denomData.id
            utmCampaign = UTM_CAMPAIGN
            isSpecialProduct = denomData.isSpecialPromo
            idemPotencyKey = IDEM_POTENCY_KEY
        }
    }

    fun getDenomData(): DenomData {
        return DenomData(
            categoryId = CATEGORY_ID,
            promoStatus = IS_NOT_PROMO,
            id = PRODUCT_ID,
            isSpecialPromo = IS_NOT_SPECIAL_PRODUCT
        )
    }

    fun getInvalidIdDenomData(): DenomData {
        return DenomData(
            categoryId = CATEGORY_ID,
            promoStatus = IS_NOT_PROMO,
            id = "",
            isSpecialPromo = IS_NOT_SPECIAL_PRODUCT
        )
    }

    fun getRecomCardWidgetModelData(): RecommendationCardWidgetModel {
        return RecommendationCardWidgetModel(
            categoryId = CATEGORY_ID,
            clientNumber = VALID_CLIENT_NUMBER,
            operatorId = OPERATOR_ID,
            productId = PRODUCT_ID,
            appUrl = RECOM_APP_URL
        )
    }

    fun getListDenomData(): List<DenomData> {
        return listOf(getDenomData())
    }

    fun getInvalidListDenomData(): List<DenomData> {
        return listOf(getInvalidIdDenomData())
    }

    fun getSelectedProduct(): SelectedProduct {
        return SelectedProduct(
            denomData = getDenomData(),
            position = 0
        )
    }

    fun getInvalidPositionSelectedProduct(): SelectedProduct {
        return SelectedProduct(
            denomData = getDenomData(),
            position = -1
        )
    }

    fun getInvalidIdSelectedProduct(): SelectedProduct {
        return SelectedProduct(
            denomData = getInvalidIdDenomData(),
            position = 0
        )
    }

    fun getCatalogInputMultiTabData(): DigitalCatalogProductInputMultiTab {
        return gson.fromJson(
            gson.JsonToString(GET_CATALOG_INPUT_MULTITAB),
            DigitalCatalogProductInputMultiTab::class.java
        )
    }

    fun getAddToCartData(): ResponseCartData {
        return gson.fromJson(
            gson.JsonToString(GET_ADD_TO_CART),
            ResponseCartData::class.java
        )
    }

    companion object {
        const val GET_FAVORITE_NUMBER = "common_telco/get_favorite_number_mock.json"
        const val GET_RECOMMENDATION = "common_telco/get_recommendation_mock.json"
        const val GET_PREFIX_OPERATOR = "common_telco/get_prefix_operator_mock.json"
        const val GET_CATALOG_INPUT_MULTITAB = "pulsa/get_catalog_input_multitab_mock.json"
        const val GET_ADD_TO_CART = "common_telco/get_add_to_cart_mock.json"
        const val GET_PREFIX_OPERATOR_EMPTY_VALIDATION = "common_telco/get_prefix_operator_empty_validation_mock.json"
        const val GET_MENU_DETAIL = "pulsa/get_menu_detail_mock.json"

        const val CATEGORY_ID = "1"
        const val OPERATOR_ID = "5"
        const val PRODUCT_ID = "1136"
        const val VALID_CLIENT_NUMBER = "081208120812"
        const val INVALID_CLIENT_NUMBER = "080000"
        const val IS_NOT_PROMO = "0"
        const val UTM_CAMPAIGN = "1"
        const val IS_NOT_SPECIAL_PRODUCT = false
        const val IDEM_POTENCY_KEY = ""
        const val RECOM_APP_URL = "tokopedia.com/test"
    }
}