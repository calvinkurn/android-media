package com.tokopedia.home_account.view.mapper

import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel

object UiModelMapper {

    fun getBalanceAndPointUiModel(walletappGetAccountBalance: WalletappGetAccountBalance): BalanceAndPointUiModel {
        var titleWallet = walletappGetAccountBalance.title
        var subTitleWallet = walletappGetAccountBalance.subtitle
        if (walletappGetAccountBalance.id == AccountConstants.WALLET.CO_BRAND_CC) {
            subTitleWallet = walletappGetAccountBalance.titleAsset
            if (walletappGetAccountBalance.hideTitle || walletappGetAccountBalance.subtitle.isEmpty()) {
                titleWallet = walletappGetAccountBalance.title
            }
        }
        return BalanceAndPointUiModel(
            id = walletappGetAccountBalance.id,
            title = titleWallet,
            subtitle = subTitleWallet,
            urlImage = walletappGetAccountBalance.icon,
            applink = walletappGetAccountBalance.applink,
            isActive = walletappGetAccountBalance.isActive,
            hideTitle = walletappGetAccountBalance.hideTitle,
            type = walletappGetAccountBalance.type,
            statusName = walletappGetAccountBalance.statusName
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
            isFailed = isFailed,
            type = shimmerUiModel.type
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
            isActive = balanceAndPointUiModel.isActive,
            type = balanceAndPointUiModel.type
        )
    }

    fun getWalletUiModel(walletappGetAccountBalance: WalletappGetAccountBalance): WalletUiModel {
        val subTitleWallet = if (walletappGetAccountBalance.hideTitle || walletappGetAccountBalance.subtitle.isEmpty()) {
                walletappGetAccountBalance.title
            } else {
                if (walletappGetAccountBalance.id == AccountConstants.WALLET.TOKOPOINT) {
                    "${walletappGetAccountBalance.title} ${walletappGetAccountBalance.subtitle}"
                } else {
                    "${walletappGetAccountBalance.title} • ${walletappGetAccountBalance.subtitle}"
                }
            }
        val titleWallet = if (walletappGetAccountBalance.id == AccountConstants.WALLET.CO_BRAND_CC) {
            walletappGetAccountBalance.titleAsset
        } else {
            walletappGetAccountBalance.title
        }

        return WalletUiModel(
            id = walletappGetAccountBalance.id,
            title = titleWallet,
            subtitle = subTitleWallet,
            urlImage = walletappGetAccountBalance.icon,
            applink = walletappGetAccountBalance.applink,
            isActive = walletappGetAccountBalance.isActive,
            statusName = walletappGetAccountBalance.statusName
        )
    }
}
