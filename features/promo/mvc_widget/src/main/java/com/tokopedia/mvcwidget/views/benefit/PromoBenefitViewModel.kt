package com.tokopedia.mvcwidget.views.benefit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PromoBenefitViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<UiModel> = MutableStateFlow(UiModel())
    val state: StateFlow<UiModel> = _state

    fun setId(id: String) {
        _state.value = UiModel(
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
                )
            ),
            listOf(
                "Nominal promo bisa berubah dikarenakan waktu pembelian, ketersediaan produk, periode promosi, ketentuan promo.",
                "Harga akhir akan ditampilkan di halaman “Pengiriman / Checkout”. Perhatikan sebelum mengkonfirmasi pesanan."
            )
        )
    }
}
