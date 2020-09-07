package com.tokopedia.sellerorder.waitingpaymentorder.presentation.provider

import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderTipsUiModel

class WaitingPaymentOrderTipsDataProvider {
    fun provideData(): List<WaitingPaymentOrderTipsUiModel> {
        return listOf(
                WaitingPaymentOrderTipsUiModel(
                        icon = R.drawable.ic_waiting_payment_order_tips,
                        description = "Barang yang sering dicari pembeli cenderung cepat laku dan akan hilang dari daftar tunggu saat pembayaran terverifikasi."
                ),
                WaitingPaymentOrderTipsUiModel(
                        icon = R.drawable.ic_waiting_payment_order_tips_empty_stock,
                        description = "Jika pembayaran terverifikasi saat stok habis, pesanan akan otomatis dibatalkan."
                )
        )
    }
}