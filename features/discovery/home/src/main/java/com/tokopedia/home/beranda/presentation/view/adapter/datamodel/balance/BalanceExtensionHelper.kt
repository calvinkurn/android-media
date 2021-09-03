package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.TagAttributes
import com.tokopedia.home.beranda.data.model.TextAttributes
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balance
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException

const val selectedWallet = "PEMUDA"
const val WALLET_CODE_GOPAY = "PEMUDA"
const val WALLET_CODE_GOPAY_POINTS = "PEMUDAPOINTS"
const val defaultActivationCta = "Sambungkan"

fun HomeHeaderWalletAction.mapToHomeBalanceItemModel(itemType: Int, state: Int): BalanceDrawerItemModel {
    val iconRes = if (walletType == HomeBalanceModel.OVO_WALLET_TYPE) R.drawable.wallet_ic_ovo_home else R.drawable.ic_tokocash
    return BalanceDrawerItemModel(
            applinkContainer = if (itemType == TYPE_WALLET_WITH_TOPUP) topupUrl else appLinkBalance,
            applinkActionText = appLinkActionButton,
            iconImageUrl = "",
            defaultIconRes = iconRes,
            balanceTitleTextAttribute = buildWalletTitleTextAttribute(),
            balanceSubTitleTextAttribute = buildWalletSubTitleTextAttribute(),
            balanceTitleTagAttribute = null,
            balanceSubTitleTagAttribute = null,
            drawerItemType = itemType,
            state = state
    )
}

fun HomeHeaderWalletAction.buildWalletTitleTextAttribute(): BalanceTextAttribute? {
    var colourRef: Int? = null
    var colour: String = ""
    var text = ""
    var isBold = false
    var applink = ""

    colourRef = R.color.Unify_N700

    walletBalanceCondition (
            isNotLinkedCondition = {
                text = "(+ ${cashBalance} )"
                isBold = true
            },
            isOvoWalletTypeCondition = {
                text = cashBalance
                isBold = true
            },
            isNotOvoWalletTypeCondition = {
                text = labelTitle
                isBold = false
            }
    )

    return BalanceTextAttribute(
            colour = colour,
            colourRef = colourRef,
            text = text,
            isBold = isBold
    )
}

fun HomeHeaderWalletAction.buildWalletSubTitleTextAttribute(): BalanceTextAttribute? {
    var colourRef: Int? = null
    var colour: String = ""
    var text = ""
    var isBold = false
    var applink = ""

    walletBalanceCondition (
            isLinkedCondition = {
                text = labelActionButton
                colourRef = R.color.Unify_N700_96
            },
            isNotLinkedCondition = {
                text = labelActionButton
                colourRef = R.color.Unify_G500
            },
            isOvoWalletTypeCondition = {
                isBold = true
            },
            isNotOvoWalletTypeCondition = {
                text = labelActionButton
                colourRef = R.color.Unify_G500
                applink = appLinkActionButton
            },
            isShowTopUpCondition = {
                colourRef = R.color.Unify_G500
                text = HomeBalanceModel.OVO_TOP_UP
                isBold = true
            },
            isNotShowTopUpCondition = {
                isBold = false
                text = String.format(HomeBalanceModel.OVO_POINTS_BALANCE, pointBalance)
            }
    )

    return BalanceTextAttribute(
            colour = colour,
            colourRef = colourRef,
            text = text,
            isBold = isBold
    )
}

private fun HomeHeaderWalletAction.walletBalanceCondition(
        isLinkedCondition: () -> Unit = {},
        isNotLinkedCondition: () -> Unit = {},
        isOvoWalletTypeCondition: () -> Unit = {},
        isNotOvoWalletTypeCondition: () -> Unit = {},
        isShowTopUpCondition: () -> Unit = {},
        isNotShowTopUpCondition: () -> Unit = {}
) {
    if (isLinked) {
        isLinkedCondition.invoke()
        if (walletType == HomeBalanceModel.OVO_WALLET_TYPE) {
            isOvoWalletTypeCondition.invoke()

            if (isShowTopup) {
                isShowTopUpCondition.invoke()
            } else {
                isNotShowTopUpCondition.invoke()
            }
        } else {
            isNotOvoWalletTypeCondition.invoke()
        }
    } else {
        isNotLinkedCondition.invoke()
    }
}

fun TokopointsDrawer.mapToHomeBalanceItemModel(drawerItemType: Int, defaultIconRes: Int? = null, state: Int): BalanceDrawerItemModel {
    val balanceTitleTextAttribute = sectionContent.getOrNull(0)?.textAttributes?.mapToBalanceTextAttributes()
    val balanceSubTitleTextAttribute = sectionContent.getOrNull(1)?.textAttributes?.mapToBalanceTextAttributes()

    val balanceTitleTagAttribute = sectionContent.getOrNull(0)?.tagAttributes?.mapToBalanceTagAttributes()
    val balanceSubTitleTagAttribute = sectionContent.getOrNull(1)?.tagAttributes?.mapToBalanceTagAttributes()

    return BalanceDrawerItemModel(
            applinkContainer = redirectAppLink,
            applinkActionText = redirectAppLink,
            redirectUrl = redirectURL,
            iconImageUrl = iconImageURL,
            defaultIconRes = defaultIconRes,
            balanceTitleTextAttribute = balanceTitleTextAttribute,
            balanceSubTitleTextAttribute = balanceSubTitleTextAttribute,
            balanceTitleTagAttribute = balanceTitleTagAttribute,
            balanceSubTitleTagAttribute = balanceSubTitleTagAttribute,
            drawerItemType = drawerItemType,
            state = state,
            mainPageTitle = mainPageTitle,
            balanceCoachmark = BalanceCoachmark(
                title = "Title from response",
                description = "Description from response"
            )
    )
}

fun WalletAppData.mapToHomeBalanceItemModel(state: Int): BalanceDrawerItemModel? {
    val selectedBalance = walletappGetBalance.balances.getOrNull(0)
    selectedBalance?.let { balances ->
        var balanceTitle = BalanceTextAttribute()
        var balanceSubtitle = BalanceTextAttribute()
        if (selectedBalance.isLinked) {
            val gopayBalance = balances.balance.find { it.walletCode == WALLET_CODE_GOPAY }
            val gopayPointsBalance = balances.balance.find { it.walletCode == WALLET_CODE_GOPAY_POINTS }

            if (gopayBalance?.amountFmt?.isEmpty() == true) {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_BALANCE_EMPTY,
                    throwable = MessageErrorException("Wallet app is linked but gopay balance return empty"),
                    reason = "Wallet app is linked but gopay balance return empty",
                    data = selectedBalance.walletName
                )
                return null
            } else if (gopayPointsBalance?.amountFmt?.isEmpty() == true) {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_POINTS_EMPTY,
                    throwable = MessageErrorException("Wallet app is linked but gopay points return empty"),
                    reason = "Wallet app is linked but gopay points return empty",
                    data = selectedBalance.walletName
                )
                return null
            }
            balanceTitle = BalanceTextAttribute(
                text = gopayBalance?.amountFmt?:"",
                isBold = true,
                colourRef = R.color.Unify_N700_96
            )
            balanceSubtitle = BalanceTextAttribute(
                text = gopayPointsBalance?.amountFmt?:"",
                colourRef = R.color.Unify_N700_68
            )
        } else {
            balanceTitle = BalanceTextAttribute(
                text = balances.walletName,
                isBold = true,
                colourRef = R.color.Unify_N700_96
            )
            balanceSubtitle = BalanceTextAttribute(
                text = if (selectedBalance.activationCta.isNotEmpty()) selectedBalance.activationCta else defaultActivationCta,
                colourRef = R.color.Unify_G500,
                isBold = true
            )
        }

        return buildWalletAppBalanceDrawerModel(
            selectedBalance = balances,
            balanceTitleTextAttribute = balanceTitle,
            balanceSubTitleTextAttribute = balanceSubtitle,
            state = state
        )
    }
    return null
}

private fun WalletAppData.buildWalletAppBalanceDrawerModel(
    selectedBalance: Balances,
    balanceTitleTextAttribute: BalanceTextAttribute,
    balanceSubTitleTextAttribute: BalanceTextAttribute,
    state: Int,
    walletCode: String = ""
) = BalanceDrawerItemModel(
    applinkContainer = selectedBalance.redirectUrl,
    applinkActionText = selectedBalance.redirectUrl,
    iconImageUrl = selectedBalance.iconUrl,
    redirectUrl = selectedBalance.redirectUrl,
    balanceTitleTextAttribute = balanceTitleTextAttribute,
    balanceSubTitleTextAttribute = balanceSubTitleTextAttribute,
    drawerItemType = if (selectedBalance.isLinked) BalanceDrawerItemModel.TYPE_WALLET_APP_LINKED else BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED,
    state = state,
    trackingAttribute = walletCode
)

fun TextAttributes.mapToBalanceTextAttributes(): BalanceTextAttribute {
    when {
        //subtitle green color from backend, use g500
        colour.contains("03AC0E") || colour.contains("03ac0e") -> {
            return BalanceTextAttribute(
                    colourRef = R.color.Unify_G500,
                    text = text,
                    isBold = true)
        }
        //title color from backend, use n700
        colour.contains("31353B") || colour.contains("31353b")-> {
            return BalanceTextAttribute(
                    colourRef = R.color.Unify_N700,
                    text = text,
                    isBold = true)
        }
        //subtitle other than green color from backend (most likely adadad color)
        //hardcoded to n700 96%
        else -> {
            return BalanceTextAttribute(
                    colour = "",
                    colourRef = R.color.Unify_N700_96,
                    text = text,
                    isBold = false)
        }
    }
}

fun TagAttributes.mapToBalanceTagAttributes(): BalanceTagAttribute {
    return BalanceTagAttribute(
            text = text,
            backgroundColour = backgroundColour
    )
}