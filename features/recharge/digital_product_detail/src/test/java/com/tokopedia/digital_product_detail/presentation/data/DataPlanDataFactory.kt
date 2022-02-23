package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.util.JsonToString
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class DataPlanDataFactory {

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

    fun getCatalogInputMultiTabData(): DigitalCatalogProductInputMultiTab {
        return gson.fromJson(
            gson.JsonToString(GET_CATALOG_INPUT_MULTITAB),
            DigitalCatalogProductInputMultiTab::class.java
        )
    }

    fun getFilterTagListSelectedData(): List<TelcoFilterTagComponent> {
         return getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents.apply {
             this.first().filterTagDataCollections.first().isSelected = true
             this[1].filterTagDataCollections[1].isSelected = true
         }
    }

    fun getFilterParamsEmpty(): ArrayList<HashMap<String, Any>> {
        val valueItemsParams = ArrayList<HashMap<String, Any>>()

        val valueItemQuota = HashMap<String, Any>()
        valueItemQuota[FILTER_PARAM_NAME] = FILTER_QUOTA
        valueItemQuota[FILTER_VALUE] = emptyList<String>()
        valueItemsParams.add(valueItemQuota)

        val valueItemFeature = HashMap<String, Any>()
        valueItemFeature[FILTER_PARAM_NAME] = FILTER_FEATURE
        valueItemFeature[FILTER_VALUE] = emptyList<String>()
        valueItemsParams.add(valueItemFeature)
        return valueItemsParams
    }

    fun getFilterParams(): ArrayList<HashMap<String, Any>> {
        val valueItemsParams = ArrayList<HashMap<String, Any>>()

        val valueItemQuota = HashMap<String, Any>()
        valueItemQuota[FILTER_PARAM_NAME] = FILTER_QUOTA
        valueItemQuota[FILTER_VALUE] = listOf(FILTER_ID_QUOTA)
        valueItemsParams.add(valueItemQuota)

        val valueItemFeature = HashMap<String, Any>()
        valueItemFeature[FILTER_PARAM_NAME] = FILTER_FEATURE
        valueItemFeature[FILTER_VALUE] = listOf(FILTER_ID_FEATURE)
        valueItemsParams.add(valueItemFeature)
        return valueItemsParams
    }

    fun getMenuDetail(): MenuDetailModel {
        return gson.fromJson(
            gson.JsonToString(GET_MENU_DETAIL),
            MenuDetailModel::class.java
        )
    }

    fun getAddToCartData(): ResponseCartData {
        return gson.fromJson(
            gson.JsonToString(GET_ADD_TO_CART),
            ResponseCartData::class.java
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
            productId = PRODUCT_ID
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
            denomWidgetEnum = DenomWidgetEnum.FULL_TYPE,
            position = 0
        )
    }

    fun getInvalidPositionSelectedProduct(): SelectedProduct {
        return SelectedProduct(
            denomData = getDenomData(),
            denomWidgetEnum = DenomWidgetEnum.FULL_TYPE,
            position = -1
        )
    }

    fun getInvalidIdSelectedProduct(): SelectedProduct {
        return SelectedProduct(
            denomData = getInvalidIdDenomData(),
            denomWidgetEnum = DenomWidgetEnum.FULL_TYPE,
            position = 0
        )
    }

    companion object {
        const val GET_FAVORITE_NUMBER = "common_telco/get_favorite_number_mock.json"
        const val GET_PREFIX_OPERATOR = "common_telco/get_prefix_operator_mock.json"
        const val GET_PREFIX_OPERATOR_EMPTY_VALIDATION = "common_telco/get_prefix_operator_empty_validation_mock.json"
        const val GET_ADD_TO_CART = "common_telco/get_add_to_cart_mock.json"
        const val GET_CATALOG_INPUT_MULTITAB = "dataplan/get_catalog_input_multitab_mock.json"
        const val GET_MENU_DETAIL = "dataplan/get_menu_detail_mock.json"

        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"
        const val FILTER_QUOTA = "filter_tag_kuota"
        const val FILTER_FEATURE = "filter_tag_feature"
        const val FILTER_ID_QUOTA = "1157"
        const val FILTER_ID_FEATURE = "1131"
        const val CATEGORY_ID = "2"
        const val OPERATOR_ID = "5"
        const val PRODUCT_ID = "1136"
        const val VALID_CLIENT_NUMBER = "081208120812"
        const val INVALID_CLIENT_NUMBER = "080000"
        const val IS_NOT_PROMO = "0"
        const val UTM_CAMPAIGN = "1"
        const val IS_NOT_SPECIAL_PRODUCT = false
        const val IDEM_POTENCY_KEY = ""
    }
}