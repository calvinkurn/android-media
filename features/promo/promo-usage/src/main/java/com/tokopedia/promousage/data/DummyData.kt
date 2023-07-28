package com.tokopedia.promousage.data

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
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

object DummyData {

    val promoPageTickerInfo: PromoPageTickerInfo
        get() = PromoPageTickerInfo(
            message = "Kini Bebas Ongkir tersedia saat pilih pengiriman, ya.",
            iconUrl = "",
            backgroundUrl = ""
        )

    val attemptedPromo: PromoItem
        get() = PromoItem(
            id = "1",
            index = 1,
            code = "ATTEMPTED01",
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
            state = PromoItemState.Normal(
                PromoItemCardDetail(
                    state = PromoItemCardDetail.TYPE_INITIAL,
                    color = "",
                    iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                    backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                )
            ),
            isAttempted = true
        )

    val cashbackPromos: List<PromoItem>
        get() = listOf(
            PromoItem(
                id = "2",
                index = 2,
                code = "CASHBACK1",
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
                state = PromoItemState.Normal(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = false,
                isVisible = true
            ),
            PromoItem(
                id = "3",
                index = 3,
                code = "CASHBACK2",
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
                state = PromoItemState.Normal(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = true,
                isVisible = false
            ),
            PromoItem(
                id = "5",
                index = 5,
                code = "CASHBACK3",
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
                isVisible = false
            ),
            PromoItem(
                id = "6",
                index = 6,
                code = "CASHBACK4",
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
                state = PromoItemState.Normal(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = false,
                isVisible = true
            ),
            PromoItem(
                id = "9",
                index = 9,
                code = "FREESHIPPING2",
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
                state = PromoItemState.Selected(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "10",
                index = 10,
                code = "FREESHIPPING3",
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
                state = PromoItemState.Normal(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = false,
                isVisible = true
            ),
            PromoItem(
                id = "14",
                index = 14,
                code = "DISCOUNT2",
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
                state = PromoItemState.Selected(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_SELECTED,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = false,
                isVisible = false
            ),
            PromoItem(
                id = "15",
                index = 15,
                code = "DISCOUNT3",
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
                state = PromoItemState.Normal(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = true,
                isVisible = true
            ),
            PromoItem(
                headerId = PromoPageSection.SECTION_RECOMMENDATION,
                id = "19",
                index = 19,
                code = "RECOMCASHBACK",
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
                state = PromoItemState.Normal(
                    PromoItemCardDetail(
                        state = PromoItemCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                isAttempted = false,
                isExpanded = true,
                isVisible = true
            )
        )

    fun dummySections(): List<DelegateAdapterItem> {
        val items = mutableListOf<DelegateAdapterItem>()
        items.add(
            PromoRecommendationItem(
                id = "recommendation_coupons",
                title = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                codes = listOf(recommendedPromos[0].code, recommendedPromos[1].code),
                message = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                messageSelected = "Kamu hemat Rp30.000 dari 2 promo!",
                backgroundUrl = "",
                animationUrl = "",
                promos = recommendedPromos
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
        items.add(PromoAccordionViewAllItem(
            headerId = "payment_coupons",
            visiblePromoCount = discountPromos.size - discountPromos.count { it.isVisible },
            isExpanded = false,
            isVisible = true
        ))
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
        items.add(PromoAccordionViewAllItem(
            headerId = "shipping_coupons",
            visiblePromoCount = freeShippingPromos.size - freeShippingPromos.count { it.isVisible },
            isExpanded = false,
            isVisible = true
        ))
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
        items.add(PromoAccordionViewAllItem(
            headerId = "other_coupons",
            visiblePromoCount = cashbackPromos.size - cashbackPromos.count { it.isVisible },
            isExpanded = false,
            isVisible = true
        ))
        items.add(PromoInputItem())
        val selectedPromoCodes = items.filterIsInstance<PromoItem>()
            .filter { it.state is PromoItemState.Selected }
            .map { it.code }
        if (selectedPromoCodes.isNotEmpty()) {
            items.add(PromoTncItem(selectedPromoCodes = selectedPromoCodes))
        }
        return items
    }
}
