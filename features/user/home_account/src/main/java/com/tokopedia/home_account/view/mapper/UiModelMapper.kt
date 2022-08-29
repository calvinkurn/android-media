package com.tokopedia.home_account.view.mapper

import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
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
            isActive = walletappGetAccountBalance.isActive,
                hideTitle = walletappGetAccountBalance.hideTitle
        )
    }

    fun getBalanceAndPointUiModel(
        shimmerUiModel: BalanceAndPointShimmerUiModel,
        isFailed: Boolean
    ): BalanceAndPointUiModel {
        return BalanceAndPointUiModel(
            id = shimmerUiModel.id,
            title = shimmerUiModel.title,
            subtitle = shimmerUiModel.subtitle,
            urlImage = shimmerUiModel.urlImage,
            applink = shimmerUiModel.applink,
            isActive = shimmerUiModel.isActive,
            isFailed = isFailed
        )
    }

    fun getBalanceAndPointShimmerUiModel(assetConfig: AssetConfig): BalanceAndPointShimmerUiModel {
        return BalanceAndPointShimmerUiModel(
            id = assetConfig.id,
            title = assetConfig.title,
            subtitle = assetConfig.subtitle,
            urlImage = assetConfig.icon,
            applink = assetConfig.applink,
            isActive = assetConfig.isActive
        )
    }

    fun getBalanceAndPointShimmerUiModel(balanceAndPointUiModel: BalanceAndPointUiModel): BalanceAndPointShimmerUiModel {
        return BalanceAndPointShimmerUiModel(
            id = balanceAndPointUiModel.id,
            title = balanceAndPointUiModel.title,
            subtitle = balanceAndPointUiModel.subtitle,
            urlImage = balanceAndPointUiModel.urlImage,
            applink = balanceAndPointUiModel.applink,
            isActive = balanceAndPointUiModel.isActive
        )
    }

    fun getWalletUiModel(walletappGetAccountBalance: WalletappGetAccountBalance): WalletUiModel {
        val subTitle = if (walletappGetAccountBalance.hideTitle) {
                walletappGetAccountBalance.title
            } else {
                if (walletappGetAccountBalance.id == AccountConstants.WALLET.TOKOPOINT) {
                    "${walletappGetAccountBalance.title} ${walletappGetAccountBalance.subtitle}"
                } else {
                    "${walletappGetAccountBalance.title} • ${walletappGetAccountBalance.subtitle}"
                }
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
        val title = if (assetConfig.id == AccountConstants.WALLET.SALDO) assetConfig.subtitle else assetConfig.title
        val subtitle = if (assetConfig.id == AccountConstants.WALLET.SALDO) assetConfig.title else assetConfig.subtitle
        return WalletShimmeringUiModel(
            id = assetConfig.id,
            title = title,
            subtitle = subtitle,
            urlImage = assetConfig.icon,
            applink = assetConfig.applink,
            isActive = assetConfig.isActive
        )
    }

    fun getWalletShimmeringUiModel(walletUiModel: WalletUiModel): WalletShimmeringUiModel {
        return WalletShimmeringUiModel(
            id = walletUiModel.id,
            title = walletUiModel.title,
            subtitle = walletUiModel.subtitle,
            urlImage = walletUiModel.urlImage,
            applink = walletUiModel.applink,
            isActive = walletUiModel.isActive
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