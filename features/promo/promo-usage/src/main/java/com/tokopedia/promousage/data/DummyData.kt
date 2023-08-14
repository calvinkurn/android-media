package com.tokopedia.promousage.data

import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.SecondaryPromoItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

object DummyData {

    val promoPageTickerInfo: PromoPageTickerInfo
        get() = PromoPageTickerInfo(
            message = "Kini Bebas Ongkir tersedia saat pilih pengiriman, ya.",
            iconUrl = "https://images.tokopedia.net/img/bundling/icons/product_bundling_dark.png",
            backgroundUrl = "https://images.tokopedia.net/img/State=bgBO.png"
        )

    val promoAttemptItemError: PromoAttemptItem
        get() = PromoAttemptItem(
            label = "Punya kode promo? Masukin di sini ✨",
            attemptedPromoCode = attemptedPromo.code,
            errorMessage = "Promo hanya berlaku di versi terbaru aplikasi Tokopedia. Update dulu, yuk!"
        )

    val promoAttemptItemSuccess: PromoAttemptItem
        get() = PromoAttemptItem(
            label = "Punya kode promo? Masukin di sini ✨",
            attemptedPromoCode = attemptedPromo.code,
            promo = attemptedPromo
        )

    val attemptedPromo: PromoItem
        get() = PromoItem(
            id = "1",
            index = 1,
            code = "ATTEMPT",
            shopId = 0,
            uniqueId = "",
            benefitAmount = 30000.0,
            benefitAmountStr = "Rp30.000",
            benefitDetail = PromoItemBenefitDetail(
                amountIdr = 30000.0,
                benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
            ),
            benefitTypeStr = "Cashback",
            cardDetails = listOf(
                PromoItemCardDetail(
                    state = PromoItemCardDetail.TYPE_INITIAL,
                    color = "",
                    iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                    backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                ),
                PromoItemCardDetail(
                    state = PromoItemCardDetail.TYPE_SELECTED,
                    color = "",
                    iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                    backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                ),
            ),
            clashingInfos = listOf(
                PromoItemClashingInfo(
                    code = "",
                    message = "Belum bisa dipakai barengan promo yang dipilih."
                )
            ),
            promoItemInfos = listOf(
                PromoItemInfo(
                    type = PromoItemInfo.TYPE_PROMO_INFO,
                    title = "Disesuaikan jika pilih Bebas Ongkir"
                )
            ),
            expiryInfo = "Berakhir dalam <a>3 jam</a>",
            expiryTimestamp = 1693451440,
            state = PromoItemState.Selected,
            isAttempted = true
        )

    val cashbackPromos: List<PromoItem>
        get() = listOf(
            PromoItem(
                id = "2",
                index = 2,
                code = "CASHBACK1",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Normal,
                isAttempted = false,
                isExpanded = false,
                isVisible = true,
                remainingPromoCount = 5
            ),
            PromoItem(
                id = "3",
                index = 3,
                code = "CASHBACK2",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Normal,
                isAttempted = false,
                isExpanded = true,
                isVisible = false
            ),
            PromoItem(
                id = "5",
                index = 5,
                code = "CASHBACK3",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Loading,
                isAttempted = false,
                isExpanded = true,
                isVisible = false,
                remainingPromoCount = 3
            ),
            PromoItem(
                id = "6",
                index = 6,
                code = "CASHBACK4",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Disabled("Belum bisa dipakai barengan promo yang dipilih."),
                isAttempted = false,
                isExpanded = true,
                isVisible = false
            ),
            PromoItem(
                id = "7",
                index = 7,
                code = "CASHBACK5",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "CASHBACK4",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                currentClashingPromoCodes = listOf("CASHBACK4"),
                state = PromoItemState.Ineligible("+Rp50.000 buat pakai promonya"),
                isAttempted = false,
                isExpanded = true,
                isVisible = false
            )
        )

    val freeShippingPromos: List<PromoItem>
        get() = listOf(
            PromoItem(
                id = "8",
                index = 8,
                code = "FREESHIPPING1",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Normal,
                isAttempted = false,
                isExpanded = false,
                isVisible = true
            ),
            PromoItem(
                id = "9",
                index = 9,
                code = "FREESHIPPING2",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Selected,
                isAttempted = false,
                isExpanded = false,
                isVisible = false,
                remainingPromoCount = 2
            ),
            PromoItem(
                id = "10",
                index = 10,
                code = "FREESHIPPING3",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Loading,
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "11",
                index = 11,
                code = "FREESHIPPING4",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Disabled("Belum bisa dipakai barengan promo yang dipilih."),
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "12",
                index = 12,
                code = "FREESHIPPING5",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "FREESHIPPING4",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                currentClashingPromoCodes = listOf("FREESHIPPING4"),
                state = PromoItemState.Ineligible("+Rp50.000 buat pakai promonya"),
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            )
        )

    val discountPromos: List<PromoItem>
        get() = listOf(
            PromoItem(
                id = "13",
                index = 13,
                code = "DISCOUNT1",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Normal,
                isAttempted = false,
                isExpanded = false,
                isVisible = true,
                cta = PromoCta(
                    type = "register_gpl_cicil",
                    text = "Registrasi GPL Cicil",
                    appLink = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fpaylater%2Fswitcher%3Ffrom%3Dpromo"
                ),
                couponType = listOf("gpl_cicil")
            ),
            PromoItem(
                id = "14",
                index = 14,
                code = "DISCOUNT2",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Selected,
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "15",
                index = 15,
                code = "DISCOUNT3",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Loading,
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "16",
                index = 16,
                code = "DISCOUNT4",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                state = PromoItemState.Disabled("Belum bisa dipakai barengan promo yang dipilih."),
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "17",
                index = 17,
                code = "DISCOUNT5",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "DISCOUNT4",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                currentClashingPromoCodes = listOf("DISCOUNT4"),
                state = PromoItemState.Ineligible("+Rp50.000 buat pakai promonya"),
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            )
        )

    val recommendedPromos: List<PromoItem>
        get() = listOf(
            PromoItem(
                headerId = PromoPageSection.SECTION_RECOMMENDATION,
                id = "18",
                index = 18,
                code = "RECOMDISCOUNT",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                secondaryPromo = SecondaryPromoItem(),
                state = PromoItemState.Normal,
                isAttempted = false,
                isExpanded = true,
                isVisible = true,
                isRecommended = true
            ),
            PromoItem(
                headerId = PromoPageSection.SECTION_RECOMMENDATION,
                id = "19",
                index = 19,
                code = "RECOMCASHBACK",
                shopId = 0,
                uniqueId = "",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoItemBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                ),
                clashingInfos = listOf(
                    PromoItemClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoItemInfos = listOf(
                    PromoItemInfo(
                        type = PromoItemInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jam</a>",
                expiryTimestamp = 1693451440,
                secondaryPromo = SecondaryPromoItem(),
                state = PromoItemState.Normal,
                isAttempted = false,
                isExpanded = true,
                isVisible = true,
                isRecommended = true,
                isLastRecommended = true
            )
        )

    fun dummySectionWithAttemptedCode(): List<DelegateAdapterItem> {
        val items = mutableListOf<DelegateAdapterItem>()
        items.add(
            PromoRecommendationItem(
                id = "recommendation_coupons",
                title = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                codes = listOf(recommendedPromos[0].code, recommendedPromos[1].code),
                message = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                messageSelected = "Kamu hemat Rp30.000 dari 2 promo!",
                backgroundUrl = "https://images.tokopedia.net/img/Promo%20Recom%20Section@3x.png",
                animationUrl = "https://assets.tokopedia.net/asts/android/shop_page/shop_campaign_tab_confetti.json",
            )
        )
        items.add(
            PromoAccordionHeaderItem(
                id = "payment_coupons",
                title = "1 promo buat pembayaran tertentu",
            )
        )
        items.addAll(
            discountPromos.map {
                it.copy(
                    headerId = "payment_coupons",
                    secondaryPromo = it.secondaryPromo.copy(headerId = "payment_coupons")
                )
            }
        )
        items.add(
            PromoAccordionViewAllItem(
                headerId = "payment_coupons",
                hiddenPromoCount = discountPromos.size - discountPromos.count { it.isVisible },
                isExpanded = false,
                isVisible = true
            )
        )
        items.add(
            PromoAccordionHeaderItem(
                id = "shipping_coupons",
                title = "5 promo buat pengiriman tertentu",
            )
        )
        items.addAll(
            freeShippingPromos.map {
                it.copy(
                    headerId = "shipping_coupons",
                    secondaryPromo = it.secondaryPromo.copy(headerId = "shipping_coupons")
                )
            }
        )
        items.add(
            PromoAccordionViewAllItem(
                headerId = "shipping_coupons",
                hiddenPromoCount = freeShippingPromos.size - freeShippingPromos.count { it.isVisible },
                isExpanded = false,
                isVisible = true
            )
        )
        items.add(
            PromoAccordionHeaderItem(
                id = "other_coupons",
                title = "5 promo lainnya buat kamu",
            )
        )
        items.addAll(
            cashbackPromos.map {
                it.copy(
                    headerId = "other_coupons",
                    secondaryPromo = it.secondaryPromo.copy(headerId = "other_coupons")
                )
            }
        )
        items.add(
            PromoAccordionViewAllItem(
                headerId = "other_coupons",
                hiddenPromoCount = cashbackPromos.size - cashbackPromos.count { it.isVisible },
                isExpanded = false,
                isVisible = true
            )
        )

        // attempt section
        // for normal attempt UI
//        items.add(PromoAttemptItem(label = "Punya kode promo? Masukin di sini ✨"))
        // success attempt UI
        items.add(promoAttemptItemSuccess)
        // failed attempt UI
//        items.add(promoAttemptItemError)

        // tnc section
        val selectedPromoCodes = items.filterIsInstance<PromoItem>()
            .filter { it.state is PromoItemState.Selected }
            .map { it.code }
        if (selectedPromoCodes.isNotEmpty()) {
            items.add(PromoTncItem(selectedPromoCodes = selectedPromoCodes))
        }
        return items
    }

    fun dummySections(): List<DelegateAdapterItem> {
        val items = mutableListOf<DelegateAdapterItem>()
        items.add(
            PromoRecommendationItem(
                id = "recommendation_coupons",
                title = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                codes = listOf(recommendedPromos[0].code, recommendedPromos[1].code),
                message = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                messageSelected = "Kamu hemat Rp30.000 dari 2 promo!",
                backgroundUrl = "https://images.tokopedia.net/img/Promo%20Recom%20Section@3x.png",
                animationUrl = "https://assets.tokopedia.net/asts/android/shop_page/shop_campaign_tab_confetti.json",
                selectedCodes = recommendedPromos.filter { it.isSelected }.map { it.code }
            )
        )
        items.addAll(recommendedPromos)
        items.add(
            PromoAccordionHeaderItem(
                id = "payment_coupons",
                title = "1 promo buat pembayaran tertentu",
            )
        )
        items.addAll(
            discountPromos.map {
                it.copy(
                    headerId = "payment_coupons",
                    secondaryPromo = it.secondaryPromo.copy(headerId = "payment_coupons")
                )
            }
        )
        items.add(
            PromoAccordionViewAllItem(
                headerId = "payment_coupons",
                hiddenPromoCount = discountPromos.size - discountPromos.count { it.isVisible },
                isExpanded = false,
                isVisible = true
            )
        )
        items.add(
            PromoAccordionHeaderItem(
                id = "shipping_coupons",
                title = "5 promo buat pengiriman tertentu",
            )
        )
        items.addAll(
            freeShippingPromos.map {
                it.copy(
                    headerId = "shipping_coupons",
                    secondaryPromo = it.secondaryPromo.copy(headerId = "shipping_coupons")
                )
            }
        )
        items.add(
            PromoAccordionViewAllItem(
                headerId = "shipping_coupons",
                hiddenPromoCount = freeShippingPromos.size - freeShippingPromos.count { it.isVisible },
                isExpanded = false,
                isVisible = true
            )
        )
        items.add(
            PromoAccordionHeaderItem(
                id = "other_coupons",
                title = "5 promo lainnya buat kamu",
            )
        )
        items.addAll(
            cashbackPromos.map {
                it.copy(
                    headerId = "other_coupons",
                    secondaryPromo = it.secondaryPromo.copy(headerId = "other_coupons")
                )
            }
        )
        items.add(
            PromoAccordionViewAllItem(
                headerId = "other_coupons",
                hiddenPromoCount = cashbackPromos.size - cashbackPromos.count { it.isVisible },
                isExpanded = false,
                isVisible = true
            )
        )

        // attempt section
        // for normal attempt UI
        items.add(PromoAttemptItem(label = "Punya kode promo? Masukin di sini ✨"))
        // success attempt UI
//        items.add(promoAttemptItemSuccess)
        // failed attempt UI
//        items.add(promoAttemptItemError)

        // tnc section
        val selectedPromoCodes = items.filterIsInstance<PromoItem>()
            .filter { it.state is PromoItemState.Selected }
            .map { it.code }
        if (selectedPromoCodes.isNotEmpty()) {
            items.add(PromoTncItem(selectedPromoCodes = selectedPromoCodes))
        }
        return items
    }
}
