package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.SubscriptionsCoachMarkData
import com.tokopedia.home.beranda.data.model.SubscriptionsData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_LOADING
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_SUBSCRIPTION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetVisitable
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException

data class HomeBalanceModel(
    var balanceDrawerItemModels: MutableList<BalanceDrawerItemModel> = mutableListOf(),
    var isTokopointsOrOvoFailed: Boolean = false,
    var status: Int = STATUS_LOADING,
    var balancePositionGopay: Int = 0,
    var balancePositionRewards: Int = 0,
    var balancePositionSubscriptions: Int = -1
) : BalanceWidgetVisitable {
    companion object {
        const val ERROR_TITLE = "Gagal Memuat"
        const val ERROR_SUBTITLE = "Coba Lagi"

        const val error_no_supported_wallet =
            "Unable to find wallet balance, possibly empty wallet app balance list or no supported wallet partner found in response"

        const val STATUS_LOADING = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_ERROR = 2

        const val DEFAULT_BALANCE_POSITION = -1

        private const val FIRST_DATA_POSITION = 0
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
        subscriptionsData: SubscriptionsData? = null,
        position: Int = DEFAULT_BALANCE_POSITION
    ) {
        tokopointDrawerListHomeData?.let { mapTokopoint(tokopointDrawerListHomeData, headerTitle, position) }
        walletAppData?.let { mapWalletApp(walletAppData, headerTitle, position) }
        subscriptionsData?.let { mapSubscriptions(subscriptionsData, headerTitle) }
    }

    fun mapErrorTokopoints(headerTitle: String, position: Int = DEFAULT_BALANCE_POSITION) {
        if (position == DEFAULT_BALANCE_POSITION) {
            balanceDrawerItemModels.add(getDefaultTokopointsErrorState(headerTitle))
        } else if (balanceDrawerItemModels.size > position) {
            balanceDrawerItemModels[position] = getDefaultTokopointsErrorState(headerTitle)
        }
    }

    fun mapErrorWallet(headerTitle: String, position: Int = DEFAULT_BALANCE_POSITION) {
        if (position == DEFAULT_BALANCE_POSITION) {
            balanceDrawerItemModels.add(getDefaultGopayErrorState(headerTitle))
        } else if (balanceDrawerItemModels.size > position) {
            balanceDrawerItemModels[position] = getDefaultGopayErrorState(headerTitle)
        }
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

    fun containsSubscription(): Boolean {
        val isContainsSubscription = if (balanceDrawerItemModels.size > balancePositionSubscriptions)
            (balanceDrawerItemModels[balancePositionSubscriptions].drawerItemType == TYPE_SUBSCRIPTION &&
                    balanceDrawerItemModels[balancePositionSubscriptions].state == STATE_SUCCESS)
            else false
        return isContainsSubscription
    }

    fun getSubscriptionBalanceCoachmark(): BalanceCoachmark? {
        if (balancePositionSubscriptions != DEFAULT_BALANCE_POSITION &&
            balanceDrawerItemModels.size > balancePositionSubscriptions) {
            val balanceItem = balanceDrawerItemModels[balancePositionSubscriptions]
            val isContainsGotoPlus = balanceItem.state == STATE_SUCCESS
            if (isContainsGotoPlus) {
                return balanceItem.balanceCoachmark
            }
        }
        return null
    }

    private fun mapTokopoint(
        tokopointDrawerListHomeData: TokopointsDrawerListHomeData?,
        headerTitle: String,
        position: Int = DEFAULT_BALANCE_POSITION
    ) {
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
                        if (position == DEFAULT_BALANCE_POSITION) {
                            balanceDrawerItemModels.add(tokopointAnimDrawerContent)
                        }
                        else if (balanceDrawerItemModels.size > position) {
                            balanceDrawerItemModels[position] = tokopointAnimDrawerContent
                        }
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
                drawerItemType = TYPE_SUBSCRIPTION)
            val coachMarkData =
                if (subscriptionData.subscriptionsCoachMarkList.isNotEmpty() && subscriptionData.subscriptionsCoachMarkList[FIRST_DATA_POSITION].coachMark.isNotEmpty()) {
                    val subscriptionCoachmarkData =
                        subscriptionData.subscriptionsCoachMarkList[FIRST_DATA_POSITION].coachMark[FIRST_DATA_POSITION]
                    BalanceCoachmark(
                        description = subscriptionCoachmarkData.content,
                        title = subscriptionCoachmarkData.title,
                        isShown = subscriptionCoachmarkData.isShown
                    )
                } else BalanceCoachmark()
            drawerSubscription.balanceCoachmark = coachMarkData
            balanceDrawerItemModels.add(drawerSubscription)
        }
    }

    private fun mapTokopointDefaultIconRes(type: Int) = when (type) {
        TYPE_REWARDS -> R.drawable.ic_new_points
        else -> null
    }

    private fun mapWalletApp(
        walletAppData: WalletAppData?,
        headerTitle: String,
        position: Int = DEFAULT_BALANCE_POSITION
    ) {
        walletAppData?.let { walletApp ->
            val selectedBalance =
                walletApp.mapToHomeBalanceItemModel(state = STATE_SUCCESS, headerTitle = headerTitle)
            if (selectedBalance != null) {
                selectedBalance.let { balance ->
                    flagStateCondition(
                        itemType = balance.drawerItemType,
                        action = {
                            if (position == DEFAULT_BALANCE_POSITION) {
                                balanceDrawerItemModels.add(balance)
                            } else if (balanceDrawerItemModels.size > position) {
                                balanceDrawerItemModels[position] = balance
                            }
                        }
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
        itemTypeCondition(
            itemType,
            typeWalletCondition = { action.invoke() },
            typeRewardsCondition = { action.invoke() },
            typeSubscriptionCondition = { action.invoke() }
        )
    }

    private fun itemTypeCondition(
        type: Int,
        typeWalletCondition: () -> Unit = {},
        typeRewardsCondition: () -> Unit = {},
        typeSubscriptionCondition: () -> Unit = {}
    ) {
        when (type) {
            TYPE_WALLET_APP_LINKED,
            TYPE_WALLET_APP_NOT_LINKED -> typeWalletCondition.invoke()
            TYPE_REWARDS -> typeRewardsCondition.invoke()
            TYPE_SUBSCRIPTION -> typeSubscriptionCondition.invoke()
        }
    }
}


