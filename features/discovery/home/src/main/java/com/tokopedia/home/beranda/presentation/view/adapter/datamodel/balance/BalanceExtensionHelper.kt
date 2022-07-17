package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.SubscriptionsDrawerList
import com.tokopedia.home.beranda.data.model.TextAttributes
import com.tokopedia.home.beranda.data.model.SubscriptionsTextAttributes
import com.tokopedia.home.beranda.data.model.TagAttributes
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException

const val WALLET_CODE_GOPAY = "PEMUDA"
const val WALLET_CODE_GOPAY_POINTS = "PEMUDAPOINTS"
const val defaultActivationCta = "Sambungkan"
private const val ERROR_GOPAY_EMPTY = "Wallet app is linked but gopay balance return empty"
private const val ERROR_GOPAY_POINTS_EMPTY = "Wallet app is linked but gopay points return empty"
private const val WALLET_PEMUDA_POINTS_THRESHOLD = 10000

fun TokopointsDrawer.mapToHomeBalanceItemModel(drawerItemType: Int, defaultIconRes: Int? = null, state: Int, headerTitle: String): BalanceDrawerItemModel {
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
            headerTitle = headerTitle
    )
}

fun WalletAppData.mapToHomeBalanceItemModel(state: Int, headerTitle: String): BalanceDrawerItemModel? {
    val selectedBalance = walletappGetBalance.balances.getOrNull(0)
    var pemudaPointsReserveBalance = ""

    selectedBalance?.let { balances ->
        var balanceTitle = BalanceTextAttribute()
        var balanceSubtitle = BalanceTextAttribute()
        if (selectedBalance.isLinked) {
            val gopayBalance = balances.balance.find { it.walletCode == WALLET_CODE_GOPAY }
            val gopayPointsBalance = balances.balance.find { it.walletCode == WALLET_CODE_GOPAY_POINTS }

            if (gopayBalance?.amountFmt?.isEmpty() == true) {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_BALANCE_EMPTY,
                    throwable = MessageErrorException(ERROR_GOPAY_EMPTY),
                    reason = ERROR_GOPAY_EMPTY,
                    data = selectedBalance.walletName
                )
                return null
            } else if (gopayPointsBalance?.amountFmt?.isEmpty() == true) {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_POINTS_EMPTY,
                    throwable = MessageErrorException(ERROR_GOPAY_POINTS_EMPTY),
                    reason = ERROR_GOPAY_POINTS_EMPTY,
                    data = selectedBalance.walletName
                )
                return null
            }
            balanceTitle = BalanceTextAttribute(
                    text = gopayBalance?.amountFmt?:"",
                    isBold = true,
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700_96
            )
            balanceSubtitle = BalanceTextAttribute(
                    text = gopayPointsBalance?.amountFmt?:"",
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
            )
        } else {
            balanceTitle = BalanceTextAttribute(
                text = balances.walletName,
                isBold = true,
                colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700_96
            )
            balanceSubtitle = BalanceTextAttribute(
                text = if (selectedBalance.activationCta.isNotEmpty()) selectedBalance.activationCta else defaultActivationCta,
                colourRef = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                isBold = true
            )

            val pemudaReserveBalance = balances.reserveBalance.find { it.walletCode == WALLET_CODE_GOPAY_POINTS }
            if (pemudaReserveBalance?.amount?:0 >= WALLET_PEMUDA_POINTS_THRESHOLD) {
                pemudaPointsReserveBalance = pemudaReserveBalance?.amountFmt?:""
                pemudaPointsReserveBalance+=" "
            }
        }

        return buildWalletAppBalanceDrawerModel(
            selectedBalance = balances,
            balanceTitleTextAttribute = balanceTitle,
            balanceSubTitleTextAttribute = balanceSubtitle,
            state = state,
            pemudaPointsReserveBalance = pemudaPointsReserveBalance,
            headerTitle = headerTitle
        )
    }
    return null
}

fun SubscriptionsDrawerList.mapToHomeBalanceItemModel(state: Int, headerTitle: String, isSubscriber: Boolean, drawerItemType: Int): BalanceDrawerItemModel {
    val balanceTitleTextAttribute = sectionContent.getOrNull(0)?.subscriptionsTextAttributes?.mapToBalanceTextAttributes()
    val balanceSubTitleTextAttribute = sectionContent.getOrNull(1)?.subscriptionsTextAttributes?.mapToBalanceTextAttributes()

    val balanceTitleTagAttribute = sectionContent.getOrNull(0)?.tagAttributes?.mapToBalanceTagAttributes()
    val balanceSubTitleTagAttribute = sectionContent.getOrNull(1)?.tagAttributes?.mapToBalanceTagAttributes()

    return BalanceDrawerItemModel(
        applinkContainer = redirectAppLink,
        applinkActionText = redirectAppLink,
        redirectUrl = redirectURL,
        iconImageUrl = iconImageURL,
        balanceTitleTextAttribute = balanceTitleTextAttribute,
        balanceSubTitleTextAttribute = balanceSubTitleTextAttribute,
        balanceTitleTagAttribute = balanceTitleTagAttribute,
        balanceSubTitleTagAttribute = balanceSubTitleTagAttribute,
        state = state,
        headerTitle = headerTitle,
        isSubscriberGoToPlus = isSubscriber,
        drawerItemType = drawerItemType
    )
}

private fun WalletAppData.buildWalletAppBalanceDrawerModel(
    selectedBalance: Balances,
    balanceTitleTextAttribute: BalanceTextAttribute,
    balanceSubTitleTextAttribute: BalanceTextAttribute,
    state: Int,
    walletCode: String = "",
    pemudaPointsReserveBalance: String = "",
    headerTitle: String
) = BalanceDrawerItemModel(
    applinkContainer = selectedBalance.redirectUrl,
    applinkActionText = selectedBalance.redirectUrl,
    iconImageUrl = selectedBalance.iconUrl,
    redirectUrl = selectedBalance.redirectUrl,
    balanceTitleTextAttribute = balanceTitleTextAttribute,
    balanceSubTitleTextAttribute = balanceSubTitleTextAttribute,
    drawerItemType = if (selectedBalance.isLinked) BalanceDrawerItemModel.TYPE_WALLET_APP_LINKED else BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED,
    state = state,
    trackingAttribute = walletCode,
    reserveBalance = pemudaPointsReserveBalance,
    headerTitle = headerTitle
)

fun TextAttributes.mapToBalanceTextAttributes(): BalanceTextAttribute {
    when {
        //subtitle green color from backend, use g500
        colour.contains("03AC0E") || colour.contains("03ac0e") -> {
            return BalanceTextAttribute(
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                    text = text,
                    isBold = true)
        }
        //title color from backend, use n700
        colour.contains("31353B") || colour.contains("31353b")-> {
            return BalanceTextAttribute(
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700,
                    text = text,
                    isBold = true)
        }
        //subtitle other than green color from backend (most likely adadad color)
        //hardcoded to n700 96%
        else -> {
            return BalanceTextAttribute(
                    colour = "",
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                    text = text,
                    isBold = false)
        }
    }
}

fun SubscriptionsTextAttributes.mapToBalanceTextAttributes(): BalanceTextAttribute {
    when {
        //subtitle green color from backend, use g500
        color.contains("03AC0E") || color.contains("03ac0e") -> {
            return BalanceTextAttribute(
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                    text = text,
                    isBold = true)
        }
        //title color from backend, use n700
        color.contains("31353B") || color.contains("31353b")-> {
            return BalanceTextAttribute(
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700,
                    text = text,
                    isBold = true)
        }
        //subtitle other than green color from backend (most likely adadad color)
        //hardcoded to n700 96%
        else -> {
            return BalanceTextAttribute(
                    colour = "",
                    colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
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