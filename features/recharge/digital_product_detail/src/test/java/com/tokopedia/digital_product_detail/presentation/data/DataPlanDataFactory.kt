package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.digital_product_detail.presentation.util.JsonToString

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

    companion object {
        const val GET_FAVORITE_NUMBER = "common_telco/get_favorite_number_mock.json"
        const val GET_PREFIX_OPERATOR = "common_telco/get_prefix_operator_mock.json"
        const val GET_PREFIX_OPERATOR_EMPTY_VALIDATION = "common_telco/get_prefix_operator_empty_validation_mock.json"
    }
}