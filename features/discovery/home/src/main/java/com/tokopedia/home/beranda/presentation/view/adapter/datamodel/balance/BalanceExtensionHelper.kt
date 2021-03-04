package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.TagAttributes
import com.tokopedia.home.beranda.data.model.TextAttributes
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction

fun HomeHeaderWalletAction.mapToHomeBalanceItemModel(state: Int): BalanceDrawerItemModel {
    val iconRes = if (walletType == HomeBalanceModel.OVO_WALLET_TYPE) R.drawable.wallet_ic_ovo_home else R.drawable.ic_tokocash
    val drawerItemType = if (walletType == HomeBalanceModel.OVO_WALLET_TYPE) TYPE_WALLET_OVO else TYPE_WALLET_OTHER
    return BalanceDrawerItemModel(
            applink = appLinkActionButton,
            iconImageUrl = "",
            defaultIconRes = iconRes,
            balanceTitleTextAttribute = buildWalletTitleTextAttribute(),
            balanceSubTitleTextAttribute = buildWalletSubTitleTextAttribute(),
            balanceTitleTagAttribute = null,
            balanceSubTitleTagAttribute = null,
            drawerItemType = drawerItemType,
            state = state
    )
}

fun HomeHeaderWalletAction.buildWalletTitleTextAttribute(): BalanceTextAttribute? {
    var colourRef: Int? = null
    var colour: String = ""
    var text = ""
    var isBold = false
    var applink = ""

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
            isBold = isBold,
            applink = applink
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
                colourRef = R.color.Unify_G500
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
            applink = if (redirectAppLink.isNotEmpty()) redirectAppLink else redirectURL,
            iconImageUrl = iconImageURL,
            defaultIconRes = defaultIconRes,
            balanceTitleTextAttribute = balanceTitleTextAttribute,
            balanceSubTitleTextAttribute = balanceSubTitleTextAttribute,
            balanceTitleTagAttribute = balanceTitleTagAttribute,
            balanceSubTitleTagAttribute = balanceSubTitleTagAttribute,
            drawerItemType = drawerItemType,
            state = state
    )
}

fun TextAttributes.mapToBalanceTextAttributes(): BalanceTextAttribute {
    return BalanceTextAttribute(
            colour = colour,
            text = text,
            isBold = isBold
    )
}

fun TagAttributes.mapToBalanceTagAttributes(): BalanceTagAttribute {
    return BalanceTagAttribute(
            text = text,
            backgroundColour = backgroundColour
    )
}