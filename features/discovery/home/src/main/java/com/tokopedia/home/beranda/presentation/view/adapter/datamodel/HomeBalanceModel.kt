package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable

import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.TYPE_UNKNOWN
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel.Companion.TYPE_WALLET
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction


data class HomeBalanceModel (
        val balanceDrawerItemModels: MutableMap<Int, BalanceDrawerItemModel> = mutableMapOf(),
        val balanceType: Int = TYPE_STATE_1
): HomeVisitable {
    companion object {
        // State 1: Ovo, Coupon, Bebas Ongkir
        const val TYPE_STATE_1 = 1

        // State 2: Tokopoints, Ovo, Bebas Ongkir
        const val TYPE_STATE_2 = 2

        // State 3: Tokopoints, Coupon, Bebas Ongkir
        const val TYPE_STATE_3 = 3

        private const val HASH_CODE = 39
    }
    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun setTrackingData(trackingData: Map<String, Any>) {

    }

    override fun getTrackingData(): Map<String, Any>? {
        return null
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return null
    }

    override fun setTrackingDataForCombination(`object`: List<Any>) {

    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {

    }

    override fun isCache(): Boolean {
        return false
    }

    override fun visitableId(): String {
        return "HomeBalanceModel"
    }

    override fun equalsWith(b: Any?): Boolean {
        return equals(b)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeBalanceModel

        if (balanceDrawerItemModels != other.balanceDrawerItemModels) return false

        return true
    }

    override fun hashCode(): Int {
        var result = balanceDrawerItemModels?.hashCode() ?: 0
        return result
    }

    fun mapBalanceData(
            homeHeaderWalletAction: HomeHeaderWalletAction? = null,
            tokopointDrawerListHomeData: TokopointsDrawerListHomeData? = null,
            pendingCashBackData: PendingCashback? = null
    ) {
        tokopointDrawerListHomeData?.tokopointsDrawerList?.drawerList?.forEach { drawerContent ->
            val type = getDrawerType(drawerContent.type)
            flagStateCondition(
                    itemType = type,
                    action = {
                        balanceDrawerItemModels[it] = drawerContent.mapToHomeBalanceItemModel(
                                drawerItemType = type,
                                state = STATE_SUCCESS
                        )
                    }
            )
        }

        homeHeaderWalletAction?.let { homeHeaderWalletAction ->
            val type = TYPE_WALLET
            flagStateCondition(
                    itemType = type,
                    action = {
                        balanceDrawerItemModels[it] = homeHeaderWalletAction.mapToHomeBalanceItemModel(state = STATE_SUCCESS)
                    }
            )
        }

        pendingCashBackData?.let {
            val type = TYPE_WALLET
            flagStateCondition(
                    itemType = type,
                    action = {
                        balanceDrawerItemModels[it] =
                                BalanceDrawerItemModel(
                                        applink = "",
                                        iconImageUrl = "",
                                        defaultIconRes = R.drawable.ic_tokocash,
                                        balanceTitleTextAttribute = null,
                                        balanceSubTitleTextAttribute = null,
                                        balanceTitleTagAttribute = null,
                                        balanceSubTitleTagAttribute = null,
                                        drawerItemType = type,
                                        state = STATE_SUCCESS
                                )
                    }
            )
        }
    }

    fun setBalanceState(type: Int, state: Int): HomeBalanceModel {
        flagStateCondition(
                itemType = type,
                action = { balanceDrawerItemModels[it]?.state = state }
        )
        return this
    }

    private fun getDrawerType(type: String) = when(type) {
        "TokoPoints" -> TYPE_TOKOPOINT
        "Rewards" -> TYPE_REWARDS
        "Coupon" -> TYPE_COUPON
        "BBO" -> TYPE_FREE_ONGKIR
        else -> TYPE_UNKNOWN
    }

    private fun flagStateCondition(
            itemType: Int,
            action: (pos: Int) -> Unit
    ) {
        when(balanceType) {
            TYPE_STATE_1 -> {
                itemTypeCondition(
                        itemType,
                        typeWalletCondition = { action.invoke(0) },
                        typeCouponCondition = { action.invoke(1) },
                        typeFreeOngkirCondition = { action.invoke(2) }
                )
            }
            TYPE_STATE_2 -> {
                itemTypeCondition(
                        itemType,
                        typeTokopointCondition = { action.invoke(0) },
                        typeWalletCondition = { action.invoke(1) },
                        typeCouponCondition = { action.invoke(2) },
                        typeFreeOngkirCondition = { action.invoke(3) }
                )
            }
            TYPE_STATE_3 -> {
                itemTypeCondition(
                        itemType,
                        typeTokopointCondition = { action.invoke(0) },
                        typeCouponCondition = { action.invoke(2) },
                        typeFreeOngkirCondition = { action.invoke(3) }
                )
            }
        }
    }

    private fun itemTypeCondition(
            type: Int,
            typeTokopointCondition: () -> Unit = {},
            typeWalletCondition: () -> Unit = {},
            typeCouponCondition: () -> Unit = {},
            typeFreeOngkirCondition: () -> Unit = {},
            typeRewardsCondition: () -> Unit = {}
    ) {
        when(type) {
            TYPE_TOKOPOINT -> typeTokopointCondition.invoke()
            TYPE_WALLET -> typeWalletCondition.invoke()
            TYPE_COUPON -> typeCouponCondition.invoke()
            TYPE_FREE_ONGKIR -> typeFreeOngkirCondition.invoke()
            TYPE_REWARDS -> typeRewardsCondition.invoke()
        }
    }
}

fun HomeHeaderWalletAction.mapToHomeBalanceItemModel(state: Int): BalanceDrawerItemModel {
    return BalanceDrawerItemModel(
            applink = appLinkActionButton,
            iconImageUrl = "",
            defaultIconRes = R.drawable.wallet_ic_ovo_home,
            balanceTitleTextAttribute = null,
            balanceSubTitleTextAttribute = null,
            balanceTitleTagAttribute = null,
            balanceSubTitleTagAttribute = null,
            drawerItemType = TYPE_WALLET,
            state = state
    )
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

// free ongkir, tokopoint, coupon

//simple logic :
//title : check tag title, then check text title
//description : check tag description, then check text description
data class BalanceDrawerItemModel(
        val applink: String = "",
        val iconImageUrl: String = "",
        val defaultIconRes: Int? = null,
        val balanceTitleTextAttribute: BalanceTextAttribute? = null,
        val balanceSubTitleTextAttribute: BalanceTextAttribute? = null,
        val balanceTitleTagAttribute: BalanceTagAttribute? = null,
        val balanceSubTitleTagAttribute: BalanceTagAttribute? = null,
        val drawerItemType: Int = TYPE_TOKOPOINT,
        var state: Int = STATE_LOADING
) {
    companion object {
        const val TYPE_UNKNOWN = 0

        const val TYPE_TOKOPOINT = 1

        const val TYPE_FREE_ONGKIR = 2

        const val TYPE_COUPON = 3

        const val TYPE_REWARDS = 4

        const val TYPE_WALLET = 5

        const val STATE_SUCCESS = 0
        const val STATE_LOADING = 1
        const val STATE_ERROR = 2
    }
}

data class BalanceTextAttribute(
        val colour: String = "",
        val text: String = "",
        val isBold: Boolean = false
)

data class BalanceTagAttribute(
        val text: String = "",
        val backgroundColour: String = ""
)