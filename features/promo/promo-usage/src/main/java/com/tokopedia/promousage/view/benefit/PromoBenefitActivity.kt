package com.tokopedia.promousage.view.benefit

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity

class PromoBenefitActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PromoBenefitBottomSheet.newInstance(
            PromoBenefitBottomSheet.Param(
                "#FFF5F6",
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
                    ),
                ),
                listOf(
                    "Nominal promo bisa berubah dikarenakan waktu pembelian, ketersediaan produk, periode promosi, ketentuan promo.",
                    "Harga akhir akan ditampilkan di halaman “Pengiriman / Checkout”. Perhatikan sebelum mengkonfirmasi pesanan."
                )
            )
        ).show(supportFragmentManager, null)
    }

}
