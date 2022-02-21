package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.presentation.util.JsonToString

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
        const val GET_FAVORITE_NUMBER = "get_favorite_number_mock.json"
        const val GET_PREFIX_OPERATOR = "get_prefix_operator_mock.json"
        const val GET_CATALOG_INPUT_MULTITAB = "get_catalog_input_multitab_mock.json"
        const val GET_ADD_TO_CART = "get_add_to_cart_mock.json"
    }
}