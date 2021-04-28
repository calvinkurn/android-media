package com.tokopedia.common.topupbills.response

import com.tokopedia.common.topupbills.CommonTopupBillsViewModelTest
import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.data.TopupBillsCatalog
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryAttribute
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.catalog_plugin.RechargeCatalogPlugin
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckout
import com.tokopedia.common_digital.atc.data.response.AttributesCart
import com.tokopedia.common_digital.atc.data.response.AutoApplyVoucher
import com.tokopedia.common_digital.atc.data.response.CrossSellingConfig
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.atc.data.response.RelationshipsCart
import com.tokopedia.common_digital.atc.data.response.ResponseCartData

object CommonTopupbillsDummyData {

    private const val PRODUCT_ID = "559"
    private const val INFO_NO_TEL = "Nomor Telepon"
    private const val INFO_NAMA = "Tokopedia User"
    private const val INFO_TOTAL_BAYAR = "Total Bayar"
    private const val VAL_NO_TEL = "081288888888"
    private const val VAL_NAMA = "Tokopedia User"
    private const val VAL_TOTAL_BAYAR = "Rp2.000"

    fun getEnquiryDataSuccess(): TopupBillsEnquiryData {
        return TopupBillsEnquiryData(
                enquiry = TopupBillsEnquiry(
                        status = "DONE",
                        attributes = TopupBillsEnquiryAttribute(
                                userId = "17211378",
                                productId = PRODUCT_ID,
                                price = "Rp.800.000.000",
                                pricePlain = 800000000,
                                mainInfoList = listOf(
                                        TopupBillsEnquiryMainInfo(INFO_NO_TEL, VAL_NO_TEL),
                                        TopupBillsEnquiryMainInfo(INFO_NAMA, VAL_NAMA),
                                        TopupBillsEnquiryMainInfo(INFO_TOTAL_BAYAR, VAL_TOTAL_BAYAR)
                                )

                        )
                )
        )
    }

    fun getMenuDetailSuccess(): TelcoCatalogMenuDetailData {
        return TelcoCatalogMenuDetailData(
                catalogMenuDetailData = TopupBillsMenuDetail(
                        catalog = TopupBillsCatalog(
                                id = 1,
                                name = "telco",
                                label = "Telco",
                                icon = ""
                        )
                )
        )
    }

    fun getFavoriteNumberSuccess(): TopupBillsFavNumberData {
        return TopupBillsFavNumberData(
                favNumber = TopupBillsFavNumber(
                        favNumberList = listOf(
                                TopupBillsFavNumberItem(
                                        clientNumber = "081288888888",
                                        productId = "5",
                                        categoryId = "578",
                                        operatorId = "123"
                                )
                        )
                )
        )
    }

    fun getRechargeCatalogPluginSuccess(isNull: Boolean = false): RechargeCatalogPlugin.Response {
        return when (isNull) {
            true -> RechargeCatalogPlugin.Response()
            false -> RechargeCatalogPlugin.Response(response = RechargeCatalogPlugin())
        }
    }

    fun getRechargeExpressCheckoutSuccess(): RechargeExpressCheckout.Response {
        return RechargeExpressCheckout.Response(
                response = RechargeExpressCheckout()
        )
    }

    fun getRechargeExpressCheckoutNonNullError(): RechargeExpressCheckout.Response {
        return RechargeExpressCheckout.Response(
                response = RechargeExpressCheckout(
                        errors = listOf(
                                RechargeExpressCheckout.Error(
                                        title = "Testing Error"
                                )
                        )
                )
        )
    }
    fun getDummyCartData(isNull: Boolean): ResponseCartData {
        val id = if (isNull) null else "17211378"
        return ResponseCartData(
                type = "cart",
                id = id
        )
    }
}