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
private const val FIRST_WALLET_DATA = 0
private const val TEXT_ATTRIBUTE_FIRST = 0
private const val TEXT_ATTRIBUTE_SECOND = 1
private const val TAG_ATTRIBUTE_FIRST = 0
private const val TAG_ATTRIBUTE_SECOND = 1
private const val EMPTY_AMOUNT_RESERVE_BALANCE = 0

fun TokopointsDrawer.mapToHomeBalanceItemModel(drawerItemType: Int, defaultIconRes: Int? = null, state: Int, headerTitle: String): BalanceDrawerItemModel {
    val balanceTitleTextAttribute = sectionContent.getOrNull(TEXT_ATTRIBUTE_FIRST)?.textAttributes?.mapToBalanceTextAttributes()
    val balanceSubTitleTextAttribute = sectionContent.getOrNull(TEXT_ATTRIBUTE_SECOND)?.textAttributes?.mapToBalanceTextAttributes()

    val balanceTitleTagAttribute = sectionContent.getOrNull(TAG_ATTRIBUTE_FIRST)?.tagAttributes?.mapToBalanceTagAttributes()
    val balanceSubTitleTagAttribute = sectionContent.getOrNull(TAG_ATTRIBUTE_SECOND)?.tagAttributes?.mapToBalanceTagAttributes()

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
    val selectedBalance = walletappGetBalance.balances.getOrNull(FIRST_WALLET_DATA)
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
            )
            balanceSubtitle = BalanceTextAttribute(
                    text = gopayPointsBalance?.amountFmt?:"",
            )
        } else {
            balanceTitle = BalanceTextAttribute(
                text = balances.walletName,
            )
            balanceSubtitle = BalanceTextAttribute(
                text = if (selectedBalance.activationCta.isNotEmpty()) selectedBalance.activationCta else defaultActivationCta,
            )
            val pemudaReserveBalance = balances.reserveBalance.find { it.walletCode == WALLET_CODE_GOPAY_POINTS }
            if (pemudaReserveBalance?.amount?: EMPTY_AMOUNT_RESERVE_BALANCE >= WALLET_PEMUDA_POINTS_THRESHOLD) {
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
    val balanceTitleTextAttribute = sectionContent.getOrNull(TEXT_ATTRIBUTE_FIRST)?.subscriptionsTextAttributes?.mapToBalanceTextAttributes()
    val balanceSubTitleTextAttribute = sectionContent.getOrNull(TEXT_ATTRIBUTE_SECOND)?.subscriptionsTextAttributes?.mapToBalanceTextAttributes()

    val balanceTitleTagAttribute = sectionContent.getOrNull(TAG_ATTRIBUTE_FIRST)?.tagAttributes?.mapToBalanceTagAttributes()
    val balanceSubTitleTagAttribute = sectionContent.getOrNull(TAG_ATTRIBUTE_SECOND)?.tagAttributes?.mapToBalanceTagAttributes()

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

private fun buildWalletAppBalanceDrawerModel(
    selectedBalance: Balances,
    balanceTitleTextAttribute: BalanceTextAttribute,
    balanceSubTitleTextAttribute: BalanceTextAttribute,
    state: Int,
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
    reserveBalance = pemudaPointsReserveBalance,
    headerTitle = headerTitle
)

fun TextAttributes.mapToBalanceTextAttributes(): BalanceTextAttribute {
    return BalanceTextAttribute(
        colour = this.colour,
        colourRef = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
        text = text,
        isBold = this.isBold
    )
}

fun SubscriptionsTextAttributes.mapToBalanceTextAttributes(): BalanceTextAttribute {
    return BalanceTextAttribute(
        colour = this.color,
        colourRef = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
        text = text,
        isBold = this.isBold)
}

fun TagAttributes.mapToBalanceTagAttributes(): BalanceTagAttribute {
    return BalanceTagAttribute(
        text = text,
        backgroundColour = backgroundColour
    )
}