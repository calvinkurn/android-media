package com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper

import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.*

object TokoFoodPromoUiModelMapper {

    fun mapTickerUiModel(): TokoFoodPromoTickerUiModel {
        return TokoFoodPromoTickerUiModel(
                message = "Kupon dengan keuntungan terbaik otomatis terpasang dan tidak bisa diubah."
        )
    }

    fun mapHeaderUiModel(isUnavailable: Boolean): TokoFoodPromoHeaderUiModel {
        return TokoFoodPromoHeaderUiModel(
                title = "Kupon Otomatis",
                tabId = if (!isUnavailable) "0" else ""
        )
    }

    fun mapEligibilityUiModel(): TokoFoodPromoEligibilityHeaderUiModel {
        return TokoFoodPromoEligibilityHeaderUiModel(
                title = "Promo yang belum bisa dipakai",
                tabId = "1"
        )
    }

    fun mapPromoItemUiModel(isUnavailable: Boolean): TokoFoodPromoItemUiModel {
        return TokoFoodPromoItemUiModel(
                isUnavailable = isUnavailable,
                highlightWording = if (isUnavailable) "" else "Keuntungan Terbaik",
                title = "Diskon makanan Rp31.500 + Diskon ongkir Rp6.000",
                timeValidityWording = "Persedian terbatas",
                unavailableInformation = if (isUnavailable) "Tambah Rp60.000 untuk pakai promo ini." else ""
        )
    }
}