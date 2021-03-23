package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback

data class PendingCashbackModel(
        val pendingCashback: PendingCashback,
        val labelActionButton: String,
        val labelTitle: String,
        val walletType: String
)