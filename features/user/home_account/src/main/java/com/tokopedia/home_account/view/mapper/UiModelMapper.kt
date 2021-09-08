package com.tokopedia.home_account.view.mapper

import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel

object UiModelMapper {

    fun getBalanceAndPointUiModel(walletappGetAccountBalance: WalletappGetAccountBalance): BalanceAndPointUiModel {
        return BalanceAndPointUiModel(
            id = walletappGetAccountBalance.id,
            title = walletappGetAccountBalance.title,
            subtitle = walletappGetAccountBalance.subtitle,
            urlImage = walletappGetAccountBalance.icon,
            applink = walletappGetAccountBalance.applink,
            weblink = walletappGetAccountBalance.weblink,
            isActive = walletappGetAccountBalance.isActive
        )
    }

    fun getBalanceAndPointUiModel(assetConfig: AssetConfig): BalanceAndPointUiModel {
        return BalanceAndPointUiModel(
            id = assetConfig.id,
            title = assetConfig.title,
            subtitle = assetConfig.subtitle,
            urlImage = assetConfig.icon,
            applink = assetConfig.applink,
            weblink = assetConfig.weblink,
            isActive = assetConfig.isActive
        )
    }

    fun getWalletUiModel(walletappGetAccountBalance: WalletappGetAccountBalance): WalletUiModel {
        return WalletUiModel(
            id = walletappGetAccountBalance.id,
            title = walletappGetAccountBalance.title,
            subtitle = walletappGetAccountBalance.subtitle,
            urlImage = walletappGetAccountBalance.icon,
            applink = walletappGetAccountBalance.applink,
            weblink = walletappGetAccountBalance.weblink,
            isActive = walletappGetAccountBalance.isActive
        )
    }

    fun getWalletUiModel(assetConfig: AssetConfig): WalletUiModel {
        return WalletUiModel(
            id = assetConfig.id,
            title = assetConfig.title,
            subtitle = assetConfig.subtitle,
            urlImage = assetConfig.icon,
            applink = assetConfig.applink,
            weblink = assetConfig.weblink,
            isActive = assetConfig.isActive
        )
    }
}