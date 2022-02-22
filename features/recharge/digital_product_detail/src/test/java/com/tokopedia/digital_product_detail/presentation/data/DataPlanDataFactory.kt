package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.digital_product_detail.presentation.util.JsonToString

class DataPlanDataFactory {

    private val gson = Gson()

    fun getFavoriteNumberData(): TopupBillsPersoFavNumberData {
        return gson.fromJson(
            gson.JsonToString(PulsaDataFactory.GET_FAVORITE_NUMBER),
            TopupBillsPersoFavNumberData::class.java
        )
    }

    fun getPrefixOperatorData(): TelcoCatalogPrefixSelect {
        return gson.fromJson(
            gson.JsonToString(PulsaDataFactory.GET_PREFIX_OPERATOR),
            TelcoCatalogPrefixSelect::class.java
        )
    }

    fun getPrefixOperatorEmptyValData(): TelcoCatalogPrefixSelect {
        return gson.fromJson(
            gson.JsonToString(PulsaDataFactory.GET_PREFIX_OPERATOR_EMPTY_VALIDATION),
            TelcoCatalogPrefixSelect::class.java
        )
    }
}