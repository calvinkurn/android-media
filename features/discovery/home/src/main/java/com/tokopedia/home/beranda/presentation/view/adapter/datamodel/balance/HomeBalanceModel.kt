package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.SubscriptionsData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_LOADING
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_SUBSCRIPTION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetVisitable
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException

data class HomeBalanceModel(
    var balanceDrawerItemModels: MutableList<BalanceDrawerItemModel> = mutableListOf(),
    var balanceType: Int? = TYPE_STATE_2,
    var isTokopointsOrOvoFailed: Boolean = false,
    var status: Int = STATUS_LOADING
) : BalanceWidgetVisitable {
    companion object {

        // State 2: Tokopoints, Ovo, Bebas Ongkir
        const val TYPE_STATE_2 = 2

        const val ERROR_TITLE = "Gagal Memuat"
        const val ERROR_SUBTITLE = "Coba Lagi"

        const val error_no_supported_wallet =
            "Unable to find wallet balance, possibly empty wallet app balance list or no supported wallet partner found in response"

        const val BALANCE_POSITION_FIRST = 0
        const val BALANCE_POSITION_SECOND = 1
        const val BALANCE_POSITION_THIRD = 2
        const val BALANCE_POSITION_FOURTH = 3

        const val STATUS_LOADING = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_ERROR = 2

        const val DEFAULT_BALANCE_POSITION = -1
    }

    override fun type(typeFactory: BalanceWidgetTypeFactory): Int {
        return typeFactory.type(this)
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

    //call to init balance widget data
    fun initBalanceModelByType() {
//        balanceDrawerItemModels.remove(BALANCE_POSITION_THIRD)
//        balanceDrawerItemModels.remove(BALANCE_POSITION_FOURTH)

//        balanceDrawerItemModels[BALANCE_POSITION_FIRST] = resetDrawerItem(BALANCE_POSITION_FIRST)
//        balanceDrawerItemModels[BALANCE_POSITION_SECOND] = resetDrawerItem(BALANCE_POSITION_SECOND)
    }

    fun resetDrawerItem(position: Int): BalanceDrawerItemModel {
        val balance =  balanceDrawerItemModels.getOrElse(
            position
        ) { BalanceDrawerItemModel() }.copy()
        balance.state = STATE_LOADING
        return balance
    }

    fun mapBalanceData(
        tokopointDrawerListHomeData: TokopointsDrawerListHomeData? = null,
        walletAppData: WalletAppData? = null,
        headerTitle: String,
        subscriptionsData: SubscriptionsData? = null
    ) {
        tokopointDrawerListHomeData?.let { mapTokopoint(tokopointDrawerListHomeData, headerTitle) }
        walletAppData?.let { mapWalletApp(walletAppData, headerTitle) }
        subscriptionsData?.let { mapSubscriptions(subscriptionsData, headerTitle) }
    }

    fun mapErrorTokopoints(headerTitle: String, position: Int = DEFAULT_BALANCE_POSITION) {
        if (position == DEFAULT_BALANCE_POSITION) {
            balanceDrawerItemModels.add(getDefaultTokopointsErrorState(headerTitle))
        } else if (balanceDrawerItemModels.size > position) {
            balanceDrawerItemModels[position]
        }
    }

    fun mapErrorWallet(headerTitle: String) {
        balanceDrawerItemModels.add(getDefaultGopayErrorState(headerTitle))
    }

    private fun getDefaultGopayErrorState(headerTitle: String): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_WALLET_APP_LINKED,
            defaultIconRes = R.drawable.ic_gopay_default,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute(),
            state = STATE_ERROR,
            headerTitle = headerTitle
        )
    }

    private fun getDefaultBBOErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_FREE_ONGKIR,
            defaultIconRes = R.drawable.ic_new_bbo,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute(),
            state = STATE_ERROR
        )
    }

    private fun getDefaultTokopointsErrorState(headerTitle: String): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_REWARDS,
            defaultIconRes = R.drawable.ic_new_tokopoints,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute(),
            state = STATE_ERROR,
            headerTitle = headerTitle
        )
    }

    private fun getDefaultCouponsRewardsErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_REWARDS,
            defaultIconRes = R.drawable.ic_new_points,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute(),
            state = STATE_ERROR
        )
    }

    private fun getDefaultErrorTitleTextAttribute(): BalanceTextAttribute {
        return BalanceTextAttribute(
            text = ERROR_TITLE,
            isBold = true,
            colourRef = com.tokopedia.unifyprinciples.R.color.Unify_N700
        )
    }

    private fun getDefaultErrorSubTItleTextAttribute(): BalanceTextAttribute {
        return BalanceTextAttribute(
            text = ERROR_SUBTITLE,
            isBold = true,
            colourRef = com.tokopedia.unifyprinciples.R.color.Unify_G500
        )
    }

    fun containsNewGopayAndTokopoints(): Boolean {
        val isContainsNewGopay = (balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.drawerItemType == TYPE_WALLET_APP_LINKED
                || balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.drawerItemType == TYPE_WALLET_APP_NOT_LINKED) &&
                balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.state == STATE_SUCCESS
        val isContainsNewTokopoint = balanceDrawerItemModels[BALANCE_POSITION_SECOND]?.state == STATE_SUCCESS
        return isContainsNewGopay && isContainsNewTokopoint
    }

    fun isGopayActive(): Boolean {
        val isGopayActive = (balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.drawerItemType == TYPE_WALLET_APP_LINKED &&
                balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.state == STATE_SUCCESS)
        return isGopayActive
    }

    fun containsGopay(): Boolean {
        val isContainsNewGopay = (balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.drawerItemType == TYPE_WALLET_APP_LINKED
                || balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.drawerItemType == TYPE_WALLET_APP_NOT_LINKED) &&
                balanceDrawerItemModels[BALANCE_POSITION_FIRST]?.state == STATE_SUCCESS
        return isContainsNewGopay
    }

    fun getTokopointsBalanceCoachmark(): BalanceCoachmark? {
        val balanceItem = balanceDrawerItemModels[BALANCE_POSITION_SECOND]
        val isContainsNewTokopoint = balanceItem?.state == STATE_SUCCESS
        if (isContainsNewTokopoint) {
            return balanceItem?.balanceCoachmark
        }
        return null
    }

    private fun mapTokopoint(tokopointDrawerListHomeData: TokopointsDrawerListHomeData?, headerTitle: String) {
        val tokopointMapData = tokopointDrawerListHomeData?.tokopointsDrawerList?.drawerList?.map {
            val type = TYPE_REWARDS
            it.mapToHomeBalanceItemModel(
                    drawerItemType = type,
                    state = STATE_SUCCESS,
                    defaultIconRes = mapTokopointDefaultIconRes(type),
                    headerTitle = headerTitle
            )
        }
        val tokopointAnimDrawerContent = tokopointMapData?.getOrNull(0)
        tokopointDrawerListHomeData?.tokopointsDrawerList?.coachmarkList?.getOrNull(0)?.coachmarkContent?.getOrNull(0)?.let {
            tokopointAnimDrawerContent?.balanceCoachmark = BalanceCoachmark(
                    title = it.title,
                    description = it.content
            )
        }
        val alternateAnimDrawerContent = tokopointMapData?.toMutableList()?.apply {
            remove(tokopointAnimDrawerContent)
        }
        tokopointAnimDrawerContent?.alternateBalanceDrawerItem = alternateAnimDrawerContent
        if (tokopointAnimDrawerContent != null) {
            flagStateCondition(
                    itemType = tokopointAnimDrawerContent.drawerItemType,
                    action = {
                        balanceDrawerItemModels.add(tokopointAnimDrawerContent)
                    }
            )
        } else {
            flagStateCondition(itemType = TYPE_REWARDS,
                    action = {
                        balanceDrawerItemModels.add( getDefaultTokopointsErrorState(headerTitle).apply {
                            state = STATE_ERROR
                        })
                    })
        }
    }

    private fun mapSubscriptions(subscriptionData: SubscriptionsData, headerTitle: String) {
        subscriptionData.drawerList.map {
            val drawerSubscription = it.mapToHomeBalanceItemModel(
                state = STATE_SUCCESS,
                headerTitle = headerTitle,
                isSubscriber = subscriptionData.isSubscriber,
                drawerItemType = TYPE_SUBSCRIPTION
                )
            balanceDrawerItemModels.add(drawerSubscription)
        }
    }

    private fun mapTokopointDefaultIconRes(type: Int) = when (type) {
        TYPE_COUPON -> R.drawable.ic_new_coupon
        TYPE_REWARDS -> R.drawable.ic_new_points
        TYPE_FREE_ONGKIR -> R.drawable.ic_new_bbo
        else -> null
    }

    private fun mapWalletApp(walletAppData: WalletAppData?, headerTitle: String) {
        walletAppData?.let { walletApp ->
            val selectedBalance =
                walletApp.mapToHomeBalanceItemModel(state = STATE_SUCCESS, headerTitle = headerTitle)
            if (selectedBalance != null) {
                selectedBalance.let { balance ->
                    flagStateCondition(
                        itemType = balance.drawerItemType,
                        action = { balanceDrawerItemModels.add(balance) }
                    )
                }
            } else {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_APP_ERROR,
                    throwable = MessageErrorException(error_no_supported_wallet),
                    reason = error_no_supported_wallet
                )
                throw IllegalStateException(error_no_supported_wallet)
            }
        }
    }

    private fun flagStateCondition(
        itemType: Int,
        action: () -> Unit
    ) {
        when (balanceType) {
            TYPE_STATE_2 -> {
                itemTypeCondition(
                        itemType,
                        typeWalletCondition = { action.invoke() },
                        typeFreeOngkirCondition = { action.invoke() },
                        typeCouponCondition = { action.invoke() },
                        typeRewardsCondition = { action.invoke() }
                )
            }
        }
    }

    private fun itemTypeCondition(
        type: Int,
        typeWalletCondition: () -> Unit = {},
        typeCouponCondition: () -> Unit = {},
        typeFreeOngkirCondition: () -> Unit = {},
        typeRewardsCondition: () -> Unit = {}
    ) {
        when (type) {
            TYPE_WALLET_OTHER,
            TYPE_WALLET_WITH_TOPUP,
            TYPE_WALLET_PENDING_CASHBACK,
            TYPE_WALLET_APP_LINKED,
            TYPE_WALLET_APP_NOT_LINKED -> typeWalletCondition.invoke()
            TYPE_COUPON -> typeCouponCondition.invoke()
            TYPE_FREE_ONGKIR -> typeFreeOngkirCondition.invoke()
            TYPE_REWARDS -> typeRewardsCondition.invoke()
        }
    }
}


