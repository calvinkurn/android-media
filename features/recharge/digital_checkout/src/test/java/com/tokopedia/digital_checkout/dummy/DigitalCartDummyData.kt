package com.tokopedia.digital_checkout.dummy

import com.tokopedia.common_digital.cart.data.entity.response.AttributesCheckout
import com.tokopedia.common_digital.cart.data.entity.response.Parameter
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.digital_checkout.data.response.atc.AttributesCart
import com.tokopedia.digital_checkout.data.response.atc.RelationshipsCart
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.AutoApplyVoucher
import com.tokopedia.digital_checkout.data.response.getcart.CrossSellingConfig
import com.tokopedia.digital_checkout.data.response.getcart.FintechProduct
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart

/**
 * @author by jessica on 26/01/21
 */
object DigitalCartDummyData {
    fun getDummyGetCartResponse(): RechargeGetCart {
        val openPaymentConfig = RechargeGetCart.OpenPaymentConfig(
                minPayment = 100,
                maxPayment = 12500,
                minPaymentText = "Rp 100",
                maxPaymentText = "Rp 12.500",
                minPaymentErrorText = "Nominal pembayaran di bawah batas pembayaran",
                maxPaymentErrorText = "Nominal pembayaran di atas batas pembayaran"
        )

        val mainInfo = mutableListOf<RechargeGetCart.Attribute>()
        mainInfo.add(RechargeGetCart.Attribute("Nama", "Tokopedia User"))

        val additionalInfo = mutableListOf<RechargeGetCart.AdditionalInfo>()
        additionalInfo.add(RechargeGetCart.AdditionalInfo(
                title = "Data Pelanggan",
                detail = listOf(RechargeGetCart.Attribute(label = "Nomor Polisi", value = "B1234ACD"))
        ))

        val autoApplyVoucher = AutoApplyVoucher(
                discountedPrice = "Rp 12.500",
                discountedAmount = 12500,
                discountPrice = "Rp 12.500"
        )

        val crossSellingConfig = CrossSellingConfig(
                canBeSkipped = true,
                isChecked = false,
                wording = CrossSellingConfig.CrossSellingWording(
                        headerTitle = "cross selling wording",
                        checkoutButtonText = "Bayar"
                ),
                wordingIsSubscribe = CrossSellingConfig.CrossSellingWording(
                        headerTitle = "cross selling wording subscribed",
                        checkoutButtonText = "Bayar"
                )
        )

        val fintechProduct = FintechProduct(
                transactionType = "egold",
                tierId = 1,
                optIn = true,
                checkBoxDisabled = false,
                allowOVOPoints = false,
                fintechAmount = 500,
                fintechPartnerAmount = 500,
                info = FintechProduct.FintechProductInfo(
                        title = "Yuk mulai nabung emas",
                        subtitle = "Rp  500",
                        tooltipText = "Nominal pembulatan disesuaikan dengan total tagihan setiap transaksi yang otomatis ditabung dan dapat di cek saldonya di Tokopedia Emas (berpartner dengan Pegadaian)"
                )
        )

        return RechargeGetCart(
                id = "8964392-7-aa86756c05ea3433a8ad6b6de34a4788",
                userId = 8964392,
                clientNumber = "191111410111",
                title = "Detail Pembayaran",
                categoryName = "Angsuran Kredit",
                operatorName = "JTrust Olympindo Multi Finance",
                icon = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/9/26/5511722/5511722_36540d36-688d-40db-b3aa-04114e598e14.png",
                priceText = "Rp 12.500",
                price = 12500,
                isInstantCheckout = false,
                isOtpRequired = false,
                sms_state = "",
                voucher = "",
                isOpenPayment = true,
                openPaymentConfig = openPaymentConfig,
                mainnInfo = mainInfo,
                additionalInfo = additionalInfo,
                enableVoucher = true,
                isCouponActive = true,
                autoApply = autoApplyVoucher,
                defaultPromo = "voucher",
                crossSellingType = 3,
                crossSellingConfig = crossSellingConfig,
                fintechProduct = listOf(fintechProduct)
        )
    }

    fun getDummyCartData(): ResponseCartData {
        val additionalInfo = AttributesCart.AdditionalInfo(
                title = "Detail Tagihan",
                detail = listOf(AttributesCart.InfoDetail("Admin Bank", "Rp 2.500"))
        )

        val autoApplyVoucher = com.tokopedia.digital_checkout.data.response.atc.AutoApplyVoucher(
                discountAmount = 2510,
                messageSuccess = "Success"
        )

        val crossSellingConfig = com.tokopedia.digital_checkout.data.response.atc.CrossSellingConfig(
                isSkipAble = true,
                isChecked = false,
                wording = com.tokopedia.digital_checkout.data.response.atc.CrossSellingConfig.CrossSellingWording(
                        headerTitle = "Checkout",
                        bodyTitle = "Aktifkan auto debit untuk tagihan ini",
                        bodyContentBefore = "Akitifkan auto debit",
                        bodyContentAfter = "Kamu harus memasukkan OVO PIN",
                        checkoutButtonText = "Bayar"
                ),
                wordingIsSubscribed = com.tokopedia.digital_checkout.data.response.atc.CrossSellingConfig.CrossSellingWording(
                        headerTitle = "Checkout",
                        bodyTitle = "Kamu memiliki auto debut yang belum aktif",
                        bodyContentBefore = "Akitifkan auto debit",
                        bodyContentAfter = "",
                        checkoutButtonText = "Bayar"
                )
        )

        val fintechProduct = FintechProduct(
                transactionType = "egold",
                tierId = 1,
                optIn = true,
                checkBoxDisabled = false,
                allowOVOPoints = false,
                fintechAmount = 500,
                fintechPartnerAmount = 500,
                info = FintechProduct.FintechProductInfo(
                        title = "Yuk mulai nabung emas",
                        subtitle = "Rp  500",
                        tooltipText = "Nominal pembulatan disesuaikan dengan total tagihan setiap transaksi yang otomatis ditabung dan dapat di cek saldonya di Tokopedia Emas (berpartner dengan Pegadaian)"
                )
        )

        val attributes = AttributesCart(
                userId = "17211378",
                clientNumber = "102111110611",
                title = "Detail Pembayaran",
                categoryName = "Listrik PLN",
                operatorName = "Tagihan Listrik",
                price = "Rp 2.510",
                pricePlain = 100000,
                isNeedOtp = false,
                mainInfo = listOf(AttributesCart.InfoDetail("ID Pelanggan",
                        "102111106111")),
                additionalInfo = listOf(additionalInfo),
                isEnableVoucher = true,
                isCouponActive = 1,
                autoApply = autoApplyVoucher,
                defaultPromoTab = "VOUCHER",
                crossSellingType = 3,
                crossSellingConfig = crossSellingConfig,
                postPaidPopUp = null,
                fintechProduct = listOf(fintechProduct)
                )
        val relationshipsCart = RelationshipsCart(
                category = RelationshipsCart.Category(RelationshipsCart.RelationData("category", "3")),
                operator = RelationshipsCart.Operator(RelationshipsCart.RelationData("operator", "18")),
                product = RelationshipsCart.Product(RelationshipsCart.RelationData("product", "291"))
        )
        return ResponseCartData(
                type = "cart",
                id = "17211378",
                attributes = attributes,
                relationships = relationshipsCart
        )
    }

    fun getAttributesCheckout(): AttributesCheckout {
        val parameter = Parameter()
        parameter.transactionId = "transactionId"
        return AttributesCheckout(
                redirectUrl = "www.tokopedia.com",
                callbackUrlSuccess = "successurl",
                callbackUrlFailed = "failedUrl",
                queryString = "this is query",
                parameter = parameter,
                thanksUrl = "thanksUrl"
        )
    }
}