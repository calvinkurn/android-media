package com.tokopedia.home_account.view.mapper

import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointItemViewHolder.Companion.DEFAULT_TYPE

object UiModelMapper {

    fun getBalanceAndPointUiModel(walletappGetAccountBalance: WalletappGetAccountBalance): BalanceAndPointUiModel {
        return BalanceAndPointUiModel(
            id = walletappGetAccountBalance.id,
            title = walletappGetAccountBalance.title,
            subtitle = walletappGetAccountBalance.subtitle,
            urlImage = walletappGetAccountBalance.icon,
            type = DEFAULT_TYPE
        )
    }

    fun getBalanceAndPointUiModel(assetConfig: AssetConfig): BalanceAndPointUiModel {
        return BalanceAndPointUiModel(
            id = assetConfig.id,
            title = assetConfig.title,
            subtitle = assetConfig.subtitle,
            urlImage = assetConfig.icon,
            type = DEFAULT_TYPE
        )
    }
}