package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberGroup
import com.tokopedia.digital_product_detail.presentation.util.JsonToString
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.digital_product_detail.domain.model.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class TokenListrikDataFactory {
    private val gson = Gson()

    fun getFavoriteNumberData(withPrefill: Boolean): PersoFavNumberGroup {
        val responses = gson.fromJson(
            gson.JsonToString(GET_FAVORITE_NUMBER),
            Array<TopupBillsPersoFavNumberData>::class.java
        ).toList()
        return PersoFavNumberGroup(
            favoriteNumberChips = responses[0],
            favoriteNumberList = responses[1],
            favoriteNumberPrefill = if (withPrefill) responses[2] else TopupBillsPersoFavNumberData()
        )
    }

    fun getMenuDetail(): MenuDetailModel {
        return gson.fromJson(
            gson.JsonToString(GET_MENU_DETAIL),
            MenuDetailModel::class.java
        )
    }

    fun getRecommendationData(): DigitalDigiPersoGetPersonalizedItem {
        return gson.fromJson(
            gson.JsonToString(GET_RECOMMENDATION),
            DigitalDigiPersoGetPersonalizedItem::class.java
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

    fun getOperatorSelectGroup(): DigitalCatalogOperatorSelectGroup {
        return gson.fromJson(
            gson.JsonToString(GET_OPERATOR_SELECT_GROUP),
            DigitalCatalogOperatorSelectGroup::class.java
        )
    }

    fun getOperatorSelectGroupEmptyValidation(): DigitalCatalogOperatorSelectGroup {
        return gson.fromJson(
            gson.JsonToString(GET_EMPTY_VALIDATOR),
            DigitalCatalogOperatorSelectGroup::class.java
        )
    }


    fun getSelectedData(data: DenomData): SelectedProduct {
        return SelectedProduct(
            denomData = data,
            DenomWidgetEnum.GRID_TYPE,
            0
        )
    }

    companion object {
        const val GET_FAVORITE_NUMBER = "common_telco/get_favorite_number_mock.json"
        const val GET_ADD_TO_CART = "common_telco/get_add_to_cart_mock.json"
        const val GET_RECOMMENDATION = "token_listrik/get_recommendation_mock.json"
        const val GET_MENU_DETAIL = "token_listrik/get_menu_detail_mock.json"
        const val GET_CATALOG_INPUT_MULTITAB = "token_listrik/get_catalog_input_multitab_mock.json"
        const val GET_OPERATOR_SELECT_GROUP = "token_listrik/get_operator_select_group_mock.json"
        const val GET_EMPTY_VALIDATOR = "token_listrik/get_empty_validation_operator_select_group_mock.json"

        const val CATEGORY_ID = "1"
        const val OPERATOR_ID = "5"
        const val PRODUCT_ID = "1136"
        const val VALID_CLIENT_NUMBER = "121212121212"
        const val INVALID_CLIENT_NUMBER = "121212"
        const val IS_NOT_PROMO = "0"
        const val UTM_CAMPAIGN = "1"
        const val IS_NOT_SPECIAL_PRODUCT = false
        const val IDEM_POTENCY_KEY = ""
        const val RECOM_APP_URL = "tokopedia.com/test"
    }

}