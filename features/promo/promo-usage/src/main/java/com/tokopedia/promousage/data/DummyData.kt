package com.tokopedia.promousage.data

import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.domain.entity.PromoBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoCardDetail
import com.tokopedia.promousage.domain.entity.PromoClashingInfo
import com.tokopedia.promousage.domain.entity.PromoInfo
import com.tokopedia.promousage.domain.entity.PromoState
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.view.mapper.toCollapsibleList

object DummyData {

    val attemptedPromo: Promo
        get() = Promo(
            id = "1",
            index = 1,
            code = "ATTEMPTED01",
            benefitAmount = 30000.0,
            benefitAmountStr = "Rp30.000",
            benefitDetail = PromoBenefitDetail(
                amountIdr = 30000.0,
                benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
            ),
            benefitTypeStr = "Cashback",
            cardDetails = listOf(
                PromoCardDetail(
                    color = "",
                    iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                    backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                )
            ),
            clashingInfos = listOf(
                PromoClashingInfo(
                    code = "",
                    message = "Belum bisa dipakai barengan promo yang dipilih."
                )
            ),
            promoInfos = listOf(
                PromoInfo(
                    type = PromoInfo.TYPE_PROMO_INFO,
                    title = "Disesuaikan jika pilih Bebas Ongkir"
                )
            ),
            expiryInfo = "Berakhir dalam <a>3 jama</a>",
            expiryTimestamp = 1693451440,
            state = PromoState.Normal,
            isAttempted = true
        )

    val cashbackPromos: List<Promo>
        get() = listOf(
            Promo(
                id = "2",
                index = 2,
                code = "CASHBACK1",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Normal,
                isAttempted = false,
                isExpanded = true
            ),
            Promo(
                id = "3",
                index = 3,
                code = "CASHBACK2",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Selected,
                isAttempted = false
            ),
            Promo(
                id = "5",
                index = 5,
                code = "CASHBACK3",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Loading,
                isAttempted = false
            ),
            Promo(
                id = "6",
                index = 6,
                code = "CASHBACK4",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Disabled,
                isAttempted = false
            ),
            Promo(
                id = "7",
                index = 7,
                code = "CASHBACK5",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Cashback",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "CASHBACK4",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                currentClashingPromoCode = listOf("CASHBACK4"),
                state = PromoState.Ineligible,
                isAttempted = false
            )
        )

    val freeShippingPromos: List<Promo>
        get() = listOf(
            Promo(
                id = "8",
                index = 8,
                code = "FREESHIPPING1",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Normal,
                isAttempted = false,
                isExpanded = true
            ),
            Promo(
                id = "9",
                index = 9,
                code = "FREESHIPPING2",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Selected,
                isAttempted = false
            ),
            Promo(
                id = "10",
                index = 10,
                code = "FREESHIPPING3",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Loading,
                isAttempted = false
            ),
            Promo(
                id = "11",
                index = 11,
                code = "FREESHIPPING4",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Disabled,
                isAttempted = false
            ),
            Promo(
                id = "12",
                index = 12,
                code = "FREESHIPPING5",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING
                ),
                benefitTypeStr = "Gratis Ongkir",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "FREESHIPPING4",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                currentClashingPromoCode = listOf("FREESHIPPING4"),
                state = PromoState.Ineligible,
                isAttempted = false
            )
        )

    val discountPromos: List<Promo>
        get() = listOf(
            Promo(
                id = "13",
                index = 13,
                code = "DISCOUNT1",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        state = PromoCardDetail.TYPE_INITIAL,
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    ),
                    PromoCardDetail(

                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Normal,
                isAttempted = false,
                isExpanded = true
            ),
            Promo(
                id = "14",
                index = 14,
                code = "DISCOUNT2",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Selected,
                isAttempted = false
            ),
            Promo(
                id = "15",
                index = 15,
                code = "DISCOUNT3",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Loading,
                isAttempted = false
            ),
            Promo(
                id = "16",
                index = 16,
                code = "DISCOUNT4",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Disabled,
                isAttempted = false
            ),
            Promo(
                id = "17",
                index = 17,
                code = "DISCOUNT5",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "DISCOUNT4",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                currentClashingPromoCode = listOf("DISCOUNT4"),
                state = PromoState.Ineligible,
                isAttempted = false
            )
        )

    val recommendedPromos: List<Promo>
        get() = listOf(
            Promo(
                id = "18",
                index = 18,
                code = "RECOMDISCOUNT",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_DISCOUNT
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Normal,
                isAttempted = false
            ),
            Promo(
                id = "19",
                index = 19,
                code = "RECOMCASHBACK",
                benefitAmount = 30000.0,
                benefitAmountStr = "Rp30.000",
                benefitDetail = PromoBenefitDetail(
                    amountIdr = 30000.0,
                    benefitType = PromoBenefitDetail.BENEFIT_TYPE_CASHBACK
                ),
                benefitTypeStr = "Diskon",
                cardDetails = listOf(
                    PromoCardDetail(
                        color = "",
                        iconUrl = "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                        backgroundUrl = "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png"
                    )
                ),
                clashingInfos = listOf(
                    PromoClashingInfo(
                        code = "",
                        message = "Belum bisa dipakai barengan promo yang dipilih."
                    )
                ),
                promoInfos = listOf(
                    PromoInfo(
                        type = PromoInfo.TYPE_PROMO_INFO,
                        title = "Disesuaikan jika pilih Bebas Ongkir"
                    )
                ),
                expiryInfo = "Berakhir dalam <a>3 jama</a>",
                expiryTimestamp = 1693451440,
                state = PromoState.Selected,
                isAttempted = false
            )
        )

    val sections: List<DelegateAdapterItem>
        get() = listOf(
            PromoRecommendationItem(
                id = "recommendation_coupons",
                title = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                codes = listOf(recommendedPromos[0].code, recommendedPromos[1].code),
                message = "Kamu bisa hemat Rp30.000 dari 2 promo!",
                messageSelected = "Kamu hemat Rp30.000 dari 2 promo!",
                backgroundUrl = "",
                animationUrl = "",
                promos = recommendedPromos
            ),
            PromoAccordionItem(
                id = "payment_coupons",
                title = "1 promo buat pembayaran tertentu",
                isExpanded = false,
                sections = cashbackPromos.toCollapsibleList()
            ),
            PromoAccordionItem(
                id = "shipping_coupons",
                title = "5 promo buat pengiriman tertentu",
                isExpanded = false,
                sections = freeShippingPromos.toCollapsibleList()
            ),
            PromoAccordionItem(
                id = "other_coupons",
                title = "5 promo lainnya buat kamu",
                isExpanded = false,
                sections = discountPromos.toCollapsibleList()
            ),
            PromoInputItem(),
            PromoTncItem()
        )

    val tncPromoCodes: List<String>
        get() = listOf("TESTCODE01", "TESTCODE02")
}
