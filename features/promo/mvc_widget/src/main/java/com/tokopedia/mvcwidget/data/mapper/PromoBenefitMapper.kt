package com.tokopedia.mvcwidget.data.mapper

import com.tokopedia.mvcwidget.data.entity.PromoCatalogResponse
import com.tokopedia.mvcwidget.views.benefit.UiModel
import com.tokopedia.mvcwidget.views.benefit.UsablePromoModel

enum class PdpComponent(val id: String) {
    FinalPrice("pdp_bs_final_price"),
    NetPrice("pdp_bs_nett_price"),
    Cashback("pdp_bs_benefit_cashback"),
    Discount("pdp_bs_benefit_discount"),
    TnC("pdp_bs_benefit_tnc"),
    Background("pdp_bs_background")
}

enum class Style(val id: String) {
    BgColor("background_color"),
    BgImage("background_image")
}

private fun List<PromoCatalogResponse.PromoCatalogGetPDEBottomSheet.Result.Widget.Component.Attribute>.attributeOf(style: Style) =
    first { it.type == style.id }.value

internal fun PromoCatalogResponse.toUiModel(): UiModel {
    val components =
        promoCatalogGetPDEBottomSheet.resultList.first().widgetList.first().componentList
    val headerComponent =
        components.first { it.componentType == PdpComponent.Background.id }.attributeList

    return UiModel(
        headerComponent.attributeOf(Style.BgImage),
        headerComponent.attributeOf(Style.BgColor),
        "Rp9,000,000",
        "Rp9,500,000",
        listOf(
            UsablePromoModel(
                "https://images.tokopedia.net/img/retention/gopaycoins/gopay.png",
                "Cashback GoPay Coins",
                "Rp300,000"
            ),
            UsablePromoModel(
                "https://images.tokopedia.net/img/newtkpd/powermerchant/ic-powermerchant-130px.png",
                "Diskon",
                "Rp200,000"
            )
        ),
        listOf(
            "Nominal promo bisa berubah dikarenakan waktu pembelian, ketersediaan produk, periode promosi, ketentuan promo.",
            "Harga akhir akan ditampilkan di halaman “Pengiriman / Checkout”. Perhatikan sebelum mengkonfirmasi pesanan."
        )
    )
}

