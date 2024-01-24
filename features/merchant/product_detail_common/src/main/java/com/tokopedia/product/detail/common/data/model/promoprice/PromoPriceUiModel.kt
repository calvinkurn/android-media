package com.tokopedia.product.detail.common.data.model.promoprice

data class PromoPriceUiModel(
    //Harga Tanpa Promo : Rp.5.000.000
    val priceAdditionalFmt: String = "",
    val promoPriceFmt: String = "",
    val promoSubtitle: String = "",
    val slashPriceFmt: String = "",
    val separatorColor: String = "",
    val mainTextColor: String = "",
    val cardBackgroundColor: String = "",
    val mainIconUrl: String = "",
    val boIconUrl: String = "",
    val superGraphicIconUrl: String = "",
    val applink: String = "",
    val bottomSheetParam: String = ""
)
