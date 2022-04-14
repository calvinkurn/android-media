package com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.explorepromo.ExplorePromo
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*

object TokoFoodPurchaseUiModelMapper {

    fun mapCheckoutResponseToUiModels(response: CheckoutTokoFoodResponse,
                                      isAvailable: Boolean,
                                      needPinpoint: Boolean): List<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()
        return visitables
    }

    fun mapGeneralTickerUiModel(isShippingAvailable: Boolean): TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel().apply {
            isErrorTicker = !isShippingAvailable
            message = "Thi will be note relevant to any info and error on checkout"
        }
    }

    fun mapAddressUiModel(): TokoFoodPurchaseAddressTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseAddressTokoFoodPurchaseUiModel().apply {
            addressName = "Rumah"
            isMainAddress = true
            receiverName = "Adrian"
            receiverPhone = "081234567890"
            cityName = "Jakarta Selatan"
            districtName = "Setiabudi"
            addressDetail = "Tokopedia Tower Ciputra World 2, Jl. Prof. DR. Satrio No.Kav. 11, Karet Semanggi, Setiabudi, Jakarta Selatan"
        }
    }

    fun mapShippingUiModel(needPinpoint: Boolean = true): TokoFoodPurchaseShippingTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseShippingTokoFoodPurchaseUiModel().apply {
            shippingCourierName = "Gojek Instan (Rp0)"
            shippingEta = "Tiba dalam 30-60 menit"
            shippingLogoUrl = "https://1000logos.net/wp-content/uploads/2020/11/Gojek-Logo-1024x640.png"
            shippingPrice = 0
            isNeedPinpoint = needPinpoint
            isShippingAvailable = true
            isEnabled = isShippingAvailable
        }
    }

    fun mapProductListHeaderUiModel(isShippingAvailable: Boolean, mIsAvailable: Boolean): TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel {
        return if (mIsAvailable) {
            TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel().apply {
                title = "Daftar Pesanan"
                action = "Tambah Pesanan"
                isAvailableHeader = mIsAvailable
                isEnabled = isShippingAvailable
            }
        } else {
            TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel().apply {
                title = "Tidak bisa diproses (3)"
                action = "Hapus"
                isAvailableHeader = mIsAvailable
                isEnabled = isShippingAvailable
            }
        }
    }

    fun mapProductUnavailableReasonUiModel(isShippingAvailable: Boolean): TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel().apply {
            reason = "Stok Habis"
            detail = ""
            isEnabled = isShippingAvailable
        }
    }

    fun mapTickerErrorShopLevelUiModel(isShippingAvailable: Boolean): TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel().apply {
            message = "Yah, ada 3 item tidak bisa diproses. Kamu bisa lanjut pesan yang lainnya, ya. <a href=\"\">Lihat</a>"
            isEnabled = isShippingAvailable
        }
    }

    fun mapProductUiModel(isShippingAvailable: Boolean, mIsAvailable: Boolean, id: String): TokoFoodPurchaseProductTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseProductTokoFoodPurchaseUiModel().apply {
            isAvailable = mIsAvailable
            this.id = id
            name = "Milo Macchiato $id"
            imageUrl = "https://img-global.cpcdn.com/recipes/1db6e302172f3f01/680x482cq70/es-milo-macchiato-janji-jiwa-foto-resep-utama.jpg"
            price = 25000
            quantity = 1
            minQuantity = 1
            maxQuantity = 10
            notes = if (id.toIntOrNull().isOdd()) "Pesanannya jangan sampai salah ya! udah haus bang. Pesanannya jangan sampai salah ya! udah haus bang..." else ""
            addOns = listOf("addOn1", "addon2", "addon3")
            originalPrice = 50000
            discountPercentage = "50%"
            isEnabled = isShippingAvailable
        }
    }

    fun mapPromoUiModel(): TokoFoodPurchasePromoTokoFoodPurchaseUiModel {
        return TokoFoodPurchasePromoTokoFoodPurchaseUiModel().apply {
            state = ExplorePromo.STATE_DEFAULT
            title = "Makin hemat pakai promo"
            description = ""
        }
    }

    fun mapSummaryTransactionUiModel(): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel()
    }

    fun mapTotalAmountUiModel(): TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel().apply {
            totalAmount = 0
            isEnabled = true
        }
    }

    fun mapAccordionUiModel(isShippingAvailable: Boolean): TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel().apply {
            isCollapsed = false
            showMoreWording = "Tampilkan Lebih Banyak"
            showLessWording = "Tampilkan Lebih Sedikit"
            isEnabled = isShippingAvailable
        }
    }

}