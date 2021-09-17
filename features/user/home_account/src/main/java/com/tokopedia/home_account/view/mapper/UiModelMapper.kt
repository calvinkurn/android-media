package com.tokopedia.home_account.view.mapper

import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.uimodel.WalletShimmeringUiModel
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
        val subTitle = if (walletappGetAccountBalance.isActive) {
            when (walletappGetAccountBalance.id) {
                AccountConstants.WALLET.GOPAY -> {
                    if (walletappGetAccountBalance.subtitle.equals(
                            AccountConstants.WALLET.GOPAY,
                            ignoreCase = true
                        )
                    ) {
                        walletappGetAccountBalance.title
                    } else {
                        "${walletappGetAccountBalance.title} • ${walletappGetAccountBalance.subtitle}"
                    }
                }
                AccountConstants.WALLET.TOKOPOINT -> {
                    "${walletappGetAccountBalance.title} ${walletappGetAccountBalance.subtitle}"
                }
                else -> {
                    "${walletappGetAccountBalance.title} • ${walletappGetAccountBalance.subtitle}"
                }
            }
        } else {
            walletappGetAccountBalance.subtitle
        }

        return WalletUiModel(
            id = walletappGetAccountBalance.id,
            title = walletappGetAccountBalance.title,
            subtitle = subTitle,
            urlImage = walletappGetAccountBalance.icon,
            applink = walletappGetAccountBalance.applink,
            isActive = walletappGetAccountBalance.isActive
        )
    }

    fun getWalletShimmeringUiModel(assetConfig: AssetConfig): WalletShimmeringUiModel {
        return WalletShimmeringUiModel(
            id = assetConfig.id,
            title = assetConfig.title,
            subtitle = assetConfig.subtitle,
            urlImage = assetConfig.icon,
            applink = assetConfig.applink,
            weblink = assetConfig.weblink,
            isActive = assetConfig.isActive
        )
    }

    fun getWalletUiModel(assetConfig: AssetConfig): WalletUiModel {
        return WalletUiModel(
            id = assetConfig.id,
            title = assetConfig.title,
            subtitle = assetConfig.subtitle,
            urlImage = assetConfig.icon,
            applink = assetConfig.applink,
            isActive = assetConfig.isActive
        )
    }

    fun getWalletUiModel(
        walletShimmeringUiModel: WalletShimmeringUiModel,
        isFailed: Boolean
    ): WalletUiModel {
        return WalletUiModel(
            id = walletShimmeringUiModel.id,
            title = walletShimmeringUiModel.title,
            subtitle = walletShimmeringUiModel.subtitle,
            urlImage = walletShimmeringUiModel.urlImage,
            applink = walletShimmeringUiModel.applink,
            isActive = walletShimmeringUiModel.isActive,
            isFailed = isFailed
        )
    }
}