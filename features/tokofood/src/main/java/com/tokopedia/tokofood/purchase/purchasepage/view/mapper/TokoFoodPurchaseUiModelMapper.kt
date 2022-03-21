package com.tokopedia.tokofood.purchase.purchasepage.view.mapper

import com.tokopedia.explorepromo.ExplorePromo
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.*

object TokoFoodPurchaseUiModelMapper {

    fun mapGeneralTickerUiModel(): TokoFoodPurchaseGeneralTickerUiModel {
        return TokoFoodPurchaseGeneralTickerUiModel(
                message = "Thi will be note relevant to any info and error on checkout"
        )
    }

    fun mapAddressUiModel(): TokoFoodPurchaseAddressUiModel {
        return TokoFoodPurchaseAddressUiModel(
                addressName = "Rumah",
                isMainAddress = true,
                receiverName = "Adrian",
                receiverPhone = "081234567890",
                addressDetail = "Tokopedia Tower Ciputra World 2, Jl. Prof. DR. Satrio No.Kav. 11, Karet Semanggi, Setiabudi, Jakarta Selatan"
        )
    }

    fun mapShippingUiModel(): TokoFoodPurchaseShippingUiModel {
        return TokoFoodPurchaseShippingUiModel(
                shippingCourierName = "Gojek Instan (Rp0)",
                shippingEta = "Tiba dalam 30-60 menit",
                shippingLogoUrl = "https://1000logos.net/wp-content/uploads/2020/11/Gojek-Logo-1024x640.png"
        )
    }

    fun mapProductListHeaderUiModel(isUnavailable: Boolean): TokoFoodPurchaseProductListHeaderUiModel {
        if (isUnavailable) {
            return TokoFoodPurchaseProductListHeaderUiModel(
                    title = "Tidak bisa diproses (3)",
                    action = "Hapus",
                    state = TokoFoodPurchaseProductListHeaderUiModel.STATE_UNAVAILABLE
            )
        } else {
            return TokoFoodPurchaseProductListHeaderUiModel(
                    title = "Daftar Pesanan",
                    action = "Tambah Pesanan",
                    state = TokoFoodPurchaseProductListHeaderUiModel.STATE_AVAILABLE
            )
        }
    }

    fun mapProductUnavailableReasonUiModel(): TokoFoodPurchaseProductUnavailableReasonUiModel {
        return TokoFoodPurchaseProductUnavailableReasonUiModel(
                reason = "Stok Habis",
                detail = ""
        )
    }

    fun mapProductUiModel(isUnavailable: Boolean): TokoFoodPurchaseProductUiModel {
        return TokoFoodPurchaseProductUiModel(
                isDisabled = isUnavailable,
                name = "Milo Macchiato",
                imageUrl = "https://img-global.cpcdn.com/recipes/1db6e302172f3f01/680x482cq70/es-milo-macchiato-janji-jiwa-foto-resep-utama.jpg",
                price = 25000,
                quantity = 1
        )
    }

    fun mapPromoUiModel(): TokoFoodPurchasePromoUiModel {
        return TokoFoodPurchasePromoUiModel(
                state = ExplorePromo.STATE_DEFAULT,
                title = "Makin hemat pakai promo",
                description = ""
        )
    }

    fun mapSummaryTransactionUiModel(): TokoFoodPurchaseSummaryTransactionUiModel {
        return TokoFoodPurchaseSummaryTransactionUiModel(
                transactions = listOf(
                        TokoFoodPurchaseSummaryTransactionUiModel.Transaction(
                                title = "Total Harga (3 item)",
                                value = 75000,
                                defaultValueForZero = TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_ZERO
                        )
                )
        )
    }

    fun mapTotalAmountUiModel(): TokoFoodPurchaseTotalAmountUiModel {
        return TokoFoodPurchaseTotalAmountUiModel(
                totalAmount = 75000,
                isDisabled = false
        )
    }

}