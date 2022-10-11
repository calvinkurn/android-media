package com.tokopedia.common.topupbills.response

import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.data.TopupBillsCatalog
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryAttribute
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.catalog_plugin.RechargeCatalogPlugin
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckout
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumber
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberTrackingData
import com.tokopedia.common.topupbills.favoritepage.data.TopupBillsSeamlessFavNumberModData
import com.tokopedia.common.topupbills.favoritepage.data.UpdateFavoriteDetail
import com.tokopedia.common_digital.atc.data.response.AtcErrorButton
import com.tokopedia.common_digital.atc.data.response.AtcErrorPage
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
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
                                id = "1",
                                name = "telco",
                                label = "Telco",
                                icon = ""
                        )
                )
        )
    }

    fun getFavoriteNumberSuccess(): TopupBillsPersoFavNumberData {
        return TopupBillsPersoFavNumberData(
                persoFavoriteNumber = TopupBillsPersoFavNumber(
                        items = listOf(
                                    TopupBillsPersoFavNumberItem(
                                        title = "081288888888",
                                        subtitle = "",
                                        id = "578",
                                        trackingData = TopupBillsPersoFavNumberTrackingData(
                                            operatorId = "123"
                                        )
                                )
                        )
                )
        )
    }

    fun getSeamlessFavoriteNumberSuccess(): TopupBillsSeamlessFavNumberData {
        return TopupBillsSeamlessFavNumberData(
                seamlessFavoriteNumber = TopupBillsSeamlessFavNumber(
                        categoryId = "578",
                        clientNumber = "081288888888",
                        favoriteNumbers = listOf(
                                TopupBillsSeamlessFavNumberItem(
                                        clientNumber = "081288888888",
                                        clientName = ""
                                )
                        ),
                        operatorId = "123",
                        productId = "5"
                )
        )
    }


    fun modifySeamlessFavoriteNumberSuccess(): TopupBillsSeamlessFavNumberModData {
        return TopupBillsSeamlessFavNumberModData(
                updateFavoriteDetail = UpdateFavoriteDetail(
                        categoryID = 578,
                        clientNumber = "081208120812",
                        hashedClientNumber = "tokopedia",
                        label = "Misael Jonathan",
                        lastOrderDate = "",
                        lastUpdated = "",
                        operatorID = 123,
                        productID = 123,
                        subscribed = false,
                        totalTransaction = 0,
                        wishlist = false
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

    fun getDummyCartDataWithErrors(): ErrorAtc{
        return ErrorAtc(
            status = 400,
            title = "this is an error",
            atcErrorPage = AtcErrorPage(
                isShowErrorPage = true,
                title = "Waduh Ada Error",
                subTitle = "Hayolo Ada Error",
                imageUrl = "https://images.tokopedia.net/img/verify_account.png",
                buttons = listOf(
                    AtcErrorButton(
                        label = "Tambah Nomor HP",
                        url = "https://tokopedia.com",
                        appLinkUrl = "tokopedia://home",
                        type = "primary"
                    )
                )
            )
        )
    }

    fun getRawErrors() = """
        {
          "errors": [
            {
              "id": "1104",
              "status": 400,
              "title": "this is an error",
              "error_page": {
                "show_error_page": true,
                "title": "Waduh Ada Error",
                "sub_title": "Hayolo Ada Error",
                "image_url": "https://images.tokopedia.net/img/verify_account.png",
                "buttons": [
                  {
                    "label" : "Tambah Nomor HP",
                    "url": "https://tokopedia.com",
                    "applink_url": "tokopedia://home",
                    "type": "primary"
                  }
                ]
              }
            }
          ]
        }
    """.trimIndent()
}
