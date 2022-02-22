package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.presentation.util.JsonToString
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel

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
        valueItemQuota[FILTER_PARAM_NAME] = "filter_tag_kuota"
        valueItemQuota[FILTER_VALUE] = emptyList<String>()
        valueItemsParams.add(valueItemQuota)

        val valueItemFeature = HashMap<String, Any>()
        valueItemFeature[FILTER_PARAM_NAME] = "filter_tag_feature"
        valueItemFeature[FILTER_VALUE] = emptyList<String>()
        valueItemsParams.add(valueItemFeature)
        return valueItemsParams
    }

    fun getFilterParams(): ArrayList<HashMap<String, Any>> {
        val valueItemsParams = ArrayList<HashMap<String, Any>>()

        val valueItemQuota = HashMap<String, Any>()
        valueItemQuota[FILTER_PARAM_NAME] = "filter_tag_kuota"
        valueItemQuota[FILTER_VALUE] = listOf("1157")
        valueItemsParams.add(valueItemQuota)

        val valueItemFeature = HashMap<String, Any>()
        valueItemFeature[FILTER_PARAM_NAME] = "filter_tag_feature"
        valueItemFeature[FILTER_VALUE] = listOf("1131")
        valueItemsParams.add(valueItemFeature)
        return valueItemsParams
    }

    companion object {
        const val GET_FAVORITE_NUMBER = "common_telco/get_favorite_number_mock.json"
        const val GET_PREFIX_OPERATOR = "common_telco/get_prefix_operator_mock.json"
        const val GET_PREFIX_OPERATOR_EMPTY_VALIDATION = "common_telco/get_prefix_operator_empty_validation_mock.json"
        const val GET_CATALOG_INPUT_MULTITAB = "paket_data/get_catalog_input_multitab_mock.json"

        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"
    }
}