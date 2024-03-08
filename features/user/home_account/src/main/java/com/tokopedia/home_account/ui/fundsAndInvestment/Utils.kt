package com.tokopedia.home_account.ui.fundsAndInvestment

import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel

fun FundsAndInvestmentResult?.isRefreshData(): Boolean {
    return when(val state = this) {
        is FundsAndInvestmentResult.Loading -> {
            state.isRefreshData
        }
        else -> false
    }
}

fun List<WalletUiModel>.getIndexFromId(id: String): Int {
    return this.indexOfFirst {
        it.id == id
    }
}

fun AssetConfig.getTitle(): String {
    return if (this.id == AccountConstants.WALLET.SALDO) {
        this.subtitle
    } else {
        this.title
    }
}

fun getIndexFromId(item: WalletUiModel, verticalList: List<WalletUiModel>, horizontalList: List<WalletUiModel>): Int {
    return if (item.isVertical) {
        verticalList.getIndexFromId(id = item.id)
    } else {
        horizontalList.getIndexFromId(id = item.id)
    }
}

fun setItemLoadingState(item: WalletUiModel, index: Int, verticalList: List<WalletUiModel>, horizontalList: List<WalletUiModel>): WalletUiModel {
    return if (item.isVertical) {
        verticalList[index].copy(
            isLoading = true
        )
    } else {
        horizontalList[index].copy(
            isLoading = true
        )
    }
}
