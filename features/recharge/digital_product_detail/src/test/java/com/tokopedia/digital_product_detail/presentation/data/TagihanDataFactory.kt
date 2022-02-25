package com.tokopedia.digital_product_detail.presentation.data

import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.data.product.CatalogOperatorAttributes
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.presentation.util.JsonToString
import com.tokopedia.recharge_component.model.denom.MenuDetailModel

class TagihanDataFactory {

    private val gson = Gson()

    fun getFavoriteNumberData(): TopupBillsPersoFavNumberData {
        return gson.fromJson(
            gson.JsonToString(GET_FAVORITE_NUMBER),
            TopupBillsPersoFavNumberData::class.java
        )
    }

    fun getMenuDetailData(): MenuDetailModel {
        return gson.fromJson(
            gson.JsonToString(GET_MENU_DETAIL),
            MenuDetailModel::class.java
        )
    }

    fun getOperatorSelectGroupData(): DigitalCatalogOperatorSelectGroup {
        return gson.fromJson(
            gson.JsonToString(GET_OPERATOR_SELECT_GROUP),
            DigitalCatalogOperatorSelectGroup::class.java
        )
    }

    fun getOperatorSelectGroupEmptyValData(): DigitalCatalogOperatorSelectGroup {
        return gson.fromJson(
            gson.JsonToString(GET_OPERATOR_SELECT_GROUP_EMPTY_VAL),
            DigitalCatalogOperatorSelectGroup::class.java
        )
    }

    fun getTagihanProductData(): RechargeProduct {
        return gson.fromJson(
            gson.JsonToString(GET_TAGIHAN_PRODUCT),
            RechargeProduct::class.java
        )
    }

    fun getTagihanProductWithPromoData(): RechargeProduct {
        return gson.fromJson(
            gson.JsonToString(GET_TAGIHAN_PRODUCT_WITH_PROMO),
            RechargeProduct::class.java
        )
    }

    fun getAddToCartData(): ResponseCartData {
        return gson.fromJson(
            gson.JsonToString(GET_ADD_TO_CART),
            ResponseCartData::class.java
        )
    }

    fun getInquiry(): TopupBillsEnquiryData {
        return gson.fromJson(
            gson.JsonToString(GET_INQUIRY),
            TopupBillsEnquiryData::class.java
        )
    }

    /* mirror get_tagihan_product_mock.json */
    fun getCheckoutPassData(): DigitalCheckoutPassData {
        return DigitalCheckoutPassData().apply {
            categoryId = CATEGORY_ID
            clientNumber = VALID_CLIENT_NUMBER
            isPromo = IS_NOT_PROMO
            operatorId = OPERATOR_ID_TAGLIS
            productId = PRODUCT_ID
            utmCampaign = UTM_CAMPAIGN
            idemPotencyKey = IDEM_POTENCY_KEY
        }
    }

    fun getOperatorList(): List<CatalogOperator> {
        return listOf(
            getOperatorDataTaglis(),
            getOperatorDataNonTaglis()
        )
    }

    fun getOperatorDataTaglis(): CatalogOperator {
        return CatalogOperator(
            id = OPERATOR_ID_TAGLIS,
            attributes = CatalogOperatorAttributes(
                name = OPERATOR_NAME_TAGLIS,
                image = "",
                imageUrl = "",
                description = ""
            )
        )
    }

    fun getOperatorDataNonTaglis(): CatalogOperator {
        return CatalogOperator(
            id = OPERATOR_ID_NON_TAGLIS,
            attributes = CatalogOperatorAttributes(
                name = OPERATOR_NAME_NON_TAGLIS,
                image = "",
                imageUrl = "",
                description = ""
            )
        )
    }

    fun getOperatorDataInvalid(): CatalogOperator {
        return CatalogOperator(
            id = OPERATOR_ID_INVALID,
            attributes = CatalogOperatorAttributes(
                name = "",
                image = "",
                imageUrl = "",
                description = ""
            )
        )
    }


    companion object {
        const val GET_FAVORITE_NUMBER = "tagihan/get_favorite_number_mock.json"
        const val GET_MENU_DETAIL = "tagihan/get_menu_detail_mock.json"
        const val GET_OPERATOR_SELECT_GROUP = "tagihan/get_operator_select_group_mock.json"
        const val GET_OPERATOR_SELECT_GROUP_EMPTY_VAL = "tagihan/get_operator_select_group_empty_val_mock.json"
        const val GET_TAGIHAN_PRODUCT = "tagihan/get_tagihan_product_mock.json"
        const val GET_TAGIHAN_PRODUCT_WITH_PROMO = "tagihan/get_tagihan_product_with_promo_mock.json"
        const val GET_ADD_TO_CART = "tagihan/get_add_to_cart_mock.json"
        const val GET_INQUIRY = "tagihan/get_inquiry_mock.json"

        const val CATEGORY_ID = "3"
        const val PRODUCT_ID = "291"
        const val OPERATOR_ID_TAGLIS = "18"
        const val OPERATOR_ID_NON_TAGLIS = "2315"
        const val OPERATOR_ID_INVALID = "-1"
        const val OPERATOR_NAME_TAGLIS = "Tagihan Listrik"
        const val OPERATOR_NAME_NON_TAGLIS = "PLN Non-Taglis"
        const val IS_NOT_PROMO = "0"
        const val UTM_CAMPAIGN = "3"
        const val VALID_CLIENT_NUMBER = "111122223333"
        const val INVALID_CLIENT_NUMBER = "000000"
        const val IDEM_POTENCY_KEY = ""
    }
}