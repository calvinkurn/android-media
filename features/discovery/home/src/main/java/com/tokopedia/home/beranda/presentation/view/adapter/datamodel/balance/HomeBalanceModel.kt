package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.*
import com.tokopedia.home.beranda.domain.model.walletapp.WalletAppData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_LOADING
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_UNKNOWN
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException

data class HomeBalanceModel(
    var balanceDrawerItemModels: MutableMap<Int, BalanceDrawerItemModel> = mutableMapOf(),
    var balanceType: Int? = null,
    var isTokopointsOrOvoFailed: Boolean = false,
    var isGopayEligible: Boolean = false
) {
    companion object {
        // State 1: Ovo, Coupon, Bebas Ongkir
        const val TYPE_STATE_1 = 1

        // State 2: Tokopoints, Ovo, Bebas Ongkir
        const val TYPE_STATE_2 = 2

        // State 3: Tokopoints, Coupon, Bebas Ongkir
        const val TYPE_STATE_3 = 3

        // State 4: Non login, will not rendered
        const val TYPE_STATE_4 = 4

        const val OVO_WALLET_TYPE = "OVO"
        const val OVO_TITLE = "OVO"
        const val OVO_TOP_UP = "Top-up OVO"
        const val OVO_POINTS_BALANCE = "%s Points"

        private const val HASH_CODE = 39

        const val ERROR_TITLE = "Gagal Memuat"
        const val ERROR_SUBTITLE = "Coba Lagi"

        const val error_no_supported_wallet =
            "Unable to find wallet balance, possibly empty wallet app balance list or no supported wallet partner found in response"

        const val BALANCE_POSITION_FIRST = 0
        const val BALANCE_POSITION_SECOND = 1
        const val BALANCE_POSITION_THIRD = 2
        const val BALANCE_POSITION_FOURTH = 3
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
        balanceDrawerItemModels.clear()
        when (balanceType) {
            TYPE_STATE_1 -> {
                balanceDrawerItemModels[BALANCE_POSITION_FIRST] = BalanceDrawerItemModel()
                balanceDrawerItemModels[BALANCE_POSITION_SECOND] = BalanceDrawerItemModel()
                balanceDrawerItemModels[BALANCE_POSITION_THIRD] = BalanceDrawerItemModel()
            }
            TYPE_STATE_2 -> {
                if (isGopayEligible) {
                    balanceDrawerItemModels[BALANCE_POSITION_FIRST] = BalanceDrawerItemModel()
                    balanceDrawerItemModels[BALANCE_POSITION_SECOND] = BalanceDrawerItemModel()
                } else {
                    balanceDrawerItemModels[BALANCE_POSITION_FIRST] = BalanceDrawerItemModel()
                    balanceDrawerItemModels[BALANCE_POSITION_SECOND] = BalanceDrawerItemModel()
                    balanceDrawerItemModels[BALANCE_POSITION_THIRD] = BalanceDrawerItemModel()
                    balanceDrawerItemModels[BALANCE_POSITION_FOURTH] = BalanceDrawerItemModel()
                }
            }
            TYPE_STATE_3 -> {
                balanceDrawerItemModels[BALANCE_POSITION_FIRST] = BalanceDrawerItemModel()
                balanceDrawerItemModels[BALANCE_POSITION_SECOND] = BalanceDrawerItemModel()
                balanceDrawerItemModels[BALANCE_POSITION_THIRD] = BalanceDrawerItemModel()
            }
        }
    }

    fun mapBalanceData(
        homeHeaderWalletAction: HomeHeaderWalletAction? = null,
        tokopointDrawerListHomeData: TokopointsDrawerListHomeData? = null,
        pendingCashBackData: PendingCashbackModel? = null,
        walletAppData: WalletAppData? = null
    ) {
        tokopointDrawerListHomeData?.let { mapTokopoint(tokopointDrawerListHomeData) }
        homeHeaderWalletAction?.let { mapWallet(homeHeaderWalletAction) }
        pendingCashBackData?.let { mapPendingCashback(homeHeaderWalletAction, pendingCashBackData) }
        walletAppData?.let { mapWalletApp(walletAppData) }
    }

    fun mapErrorTokopoints() {
        when (balanceType) {
            TYPE_STATE_1 -> {
                balanceDrawerItemModels[BALANCE_POSITION_SECOND] = getDefaultTokopointsErrorState()
                balanceDrawerItemModels[BALANCE_POSITION_THIRD] = getDefaultCouponsRewardsErrorState()
            }
            TYPE_STATE_2 -> {
                balanceDrawerItemModels[BALANCE_POSITION_SECOND] = getDefaultBBOErrorState()
                balanceDrawerItemModels[BALANCE_POSITION_THIRD] = getDefaultTokopointsErrorState()
                balanceDrawerItemModels[BALANCE_POSITION_FOURTH] = getDefaultCouponsRewardsErrorState()
            }
            TYPE_STATE_3 -> {
                balanceDrawerItemModels[BALANCE_POSITION_FIRST] = getDefaultTokopointsErrorState()
                balanceDrawerItemModels[BALANCE_POSITION_SECOND] = getDefaultCouponsRewardsErrorState()
                balanceDrawerItemModels[BALANCE_POSITION_THIRD] = getDefaultBBOErrorState()
            }
        }
    }

    fun mapErrorWallet(isWalletApp: Boolean) {
        when (balanceType) {
            TYPE_STATE_1 -> {
                balanceDrawerItemModels[BALANCE_POSITION_FIRST] =
                    if (isWalletApp) getDefaultGopayErrorState() else getDefaultOvoErrorState()
            }
            TYPE_STATE_2 -> {
                balanceDrawerItemModels[BALANCE_POSITION_FIRST] =
                    if (isWalletApp) getDefaultGopayErrorState() else getDefaultOvoErrorState()
            }
        }
    }

    private fun getDefaultGopayErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_WALLET_APP_LINKED,
            defaultIconRes = R.drawable.ic_gopay_default,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute()
        )
    }

    private fun getDefaultOvoErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_WALLET_OVO,
            defaultIconRes = R.drawable.wallet_ic_ovo_home,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute()
        )
    }

    private fun getDefaultBBOErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_FREE_ONGKIR,
            defaultIconRes = R.drawable.ic_new_bbo,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute()
        )
    }

    private fun getDefaultTokopointsErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_TOKOPOINT,
            defaultIconRes = R.drawable.ic_new_tokopoints,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute()
        )
    }

    private fun getDefaultCouponsRewardsErrorState(): BalanceDrawerItemModel {
        return BalanceDrawerItemModel(
            drawerItemType = TYPE_REWARDS,
            defaultIconRes = R.drawable.ic_new_points,
            balanceTitleTextAttribute = getDefaultErrorTitleTextAttribute(),
            balanceSubTitleTextAttribute = getDefaultErrorSubTItleTextAttribute()
        )
    }

    private fun getDefaultErrorTitleTextAttribute(): BalanceTextAttribute {
        return BalanceTextAttribute(
            text = ERROR_TITLE,
            isBold = true,
            colourRef = R.color.Unify_N700
        )
    }

    private fun getDefaultErrorSubTItleTextAttribute(): BalanceTextAttribute {
        return BalanceTextAttribute(
            text = ERROR_SUBTITLE,
            isBold = true,
            colourRef = R.color.Unify_G500
        )
    }

    fun setWalletBalanceState(state: Int): HomeBalanceModel {
        setBalanceState(TYPE_WALLET_OVO, state)
        setBalanceState(TYPE_WALLET_WITH_TOPUP, state)
        setBalanceState(TYPE_WALLET_PENDING_CASHBACK, state)
        setBalanceState(TYPE_WALLET_OTHER, state)
        return this
    }

    fun setTokopointBalanceState(state: Int): HomeBalanceModel {
        setBalanceState(TYPE_TOKOPOINT, state)
        setBalanceState(TYPE_COUPON, state)
        setBalanceState(TYPE_REWARDS, state)
        setBalanceState(TYPE_FREE_ONGKIR, state)
        return this
    }

    private fun setBalanceState(type: Int, state: Int): HomeBalanceModel {
        flagStateCondition(
            itemType = type,
            action = { balanceDrawerItemModels[it]?.state = state }
        )
        return this
    }

    private fun mapPendingCashback(
        homeHeaderWalletAction: HomeHeaderWalletAction?,
        pendingCashBackData: PendingCashbackModel?
    ) {
        pendingCashBackData?.let { pendingCashBackData ->
            val type = TYPE_WALLET_PENDING_CASHBACK
            flagStateCondition(
                itemType = type,
                action = {
                    if (pendingCashBackData.walletType == OVO_WALLET_TYPE) {
                        balanceDrawerItemModels[it] =
                            BalanceDrawerItemModel(
                                applinkActionText = homeHeaderWalletAction?.appLinkActionButton
                                    ?: "",
                                iconImageUrl = "",
                                defaultIconRes = R.drawable.wallet_ic_ovo_home,
                                balanceTitleTextAttribute = BalanceTextAttribute(
                                    text = "(+ ${pendingCashBackData.pendingCashback.amountText} )",
                                    isBold = true,
                                    colourRef = R.color.Unify_N700
                                ),
                                balanceSubTitleTextAttribute = BalanceTextAttribute(
                                    text = pendingCashBackData.labelActionButton,
                                    isBold = true,
                                    colourRef = R.color.Unify_G500
                                ),
                                balanceTitleTagAttribute = null,
                                balanceSubTitleTagAttribute = null,
                                drawerItemType = type,
                                state = STATE_SUCCESS
                            )
                    }
                }
            )
        }
    }

    private fun mapWallet(homeHeaderWalletAction: HomeHeaderWalletAction?) {
        homeHeaderWalletAction?.let { homeHeaderWalletAction ->
            val type = when (homeHeaderWalletAction.walletType) {
                OVO_WALLET_TYPE -> {
                    if (homeHeaderWalletAction.isShowTopup) {
                        TYPE_WALLET_WITH_TOPUP
                    } else {
                        TYPE_WALLET_OVO
                    }
                }
                else -> TYPE_WALLET_OTHER
            }

            flagStateCondition(
                itemType = type,
                action = {
                    balanceDrawerItemModels[it] = homeHeaderWalletAction.mapToHomeBalanceItemModel(
                        itemType = type,
                        state = STATE_SUCCESS
                    )
                }
            )
        }
    }

    private fun mapTokopoint(tokopointDrawerListHomeData: TokopointsDrawerListHomeData?) {
        if (isGopayEligible) {
            val tokopointMapData = tokopointDrawerListHomeData?.tokopointsDrawerList?.drawerList?.map {
                val type = getDrawerType(it.type)
                it.mapToHomeBalanceItemModel(
                    drawerItemType = type,
                    state = STATE_SUCCESS,
                    defaultIconRes = mapTokopointDefaultIconRes(type)
                )
            }
            val tokopointAnimDrawerContent = tokopointMapData?.find { it.drawerItemType == TYPE_TOKOPOINT }
            val alternateAnimDrawerContent = tokopointMapData?.toMutableList()?.apply {
                remove(tokopointAnimDrawerContent)
            }
            tokopointAnimDrawerContent?.alternateBalanceDrawerItem = alternateAnimDrawerContent
            if (tokopointAnimDrawerContent != null) {
                flagStateCondition(
                    itemType = TYPE_TOKOPOINT,
                    action = {
                        balanceDrawerItemModels[it] = tokopointAnimDrawerContent
                    }
                )
            } else {
                flagStateCondition(itemType = TYPE_TOKOPOINT,
                action = {
                    balanceDrawerItemModels[it] = getDefaultTokopointsErrorState().apply {
                        state = STATE_ERROR
                    }
                })
            }

        } else {
            tokopointDrawerListHomeData?.tokopointsDrawerList?.drawerList?.forEach { drawerContent ->
                val type = getDrawerType(drawerContent.type)
                flagStateCondition(
                    itemType = type,
                    action = {
                        balanceDrawerItemModels[it] = drawerContent.mapToHomeBalanceItemModel(
                            drawerItemType = type,
                            state = STATE_SUCCESS,
                            defaultIconRes = mapTokopointDefaultIconRes(type)
                        )
                    }
                )
            }
            balanceDrawerItemModels.forEach {
                if (it.value.state == STATE_LOADING) {
                    balanceDrawerItemModels[it.key] = it.value.copy(state = STATE_ERROR)
                }
            }
        }
    }

    private fun mapTokopointDefaultIconRes(type: Int) = when (type) {
        TYPE_TOKOPOINT -> R.drawable.ic_new_tokopoints
        TYPE_COUPON -> R.drawable.ic_new_coupon
        TYPE_REWARDS -> R.drawable.ic_new_points
        TYPE_FREE_ONGKIR -> R.drawable.ic_new_bbo
        else -> null
    }

    private fun mapWalletApp(walletAppData: WalletAppData?) {
        walletAppData?.let { walletApp ->
            val selectedBalance =
                walletApp.mapToHomeBalanceItemModel(state = STATE_SUCCESS).getOrNull(0)
            if (selectedBalance != null) {
                selectedBalance.let { balance ->
                    flagStateCondition(
                        itemType = balance.drawerItemType,
                        action = { balanceDrawerItemModels[it] = balance }
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

    private fun getDrawerType(type: String) = when (type) {
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
        when (balanceType) {
            TYPE_STATE_1 -> {
                itemTypeCondition(
                    itemType,
                    typeWalletCondition = { action.invoke(BALANCE_POSITION_FIRST) },
                    typeCouponCondition = { action.invoke(BALANCE_POSITION_SECOND) },
                    typeRewardsCondition = { action.invoke(BALANCE_POSITION_SECOND) },
                    typeFreeOngkirCondition = { action.invoke(BALANCE_POSITION_THIRD) }
                )
            }
            TYPE_STATE_2 -> {
                if (isGopayEligible) {
                    itemTypeCondition(
                        itemType,
                        typeWalletCondition = { action.invoke(BALANCE_POSITION_FIRST) },
                        typeTokopointCondition = { action.invoke(BALANCE_POSITION_SECOND) }
                    )
                } else {
                    itemTypeCondition(
                        itemType,
                        typeWalletCondition = { action.invoke(BALANCE_POSITION_FIRST) },
                        typeFreeOngkirCondition = { action.invoke(BALANCE_POSITION_SECOND) },
                        typeTokopointCondition = { action.invoke(BALANCE_POSITION_THIRD) },
                        typeCouponCondition = { action.invoke(BALANCE_POSITION_FOURTH) },
                        typeRewardsCondition = { action.invoke(BALANCE_POSITION_FOURTH) }
                    )
                }
            }
            TYPE_STATE_3 -> {
                itemTypeCondition(
                    itemType,
                    typeTokopointCondition = { action.invoke(BALANCE_POSITION_FIRST) },
                    typeCouponCondition = { action.invoke(BALANCE_POSITION_SECOND) },
                    typeRewardsCondition = { action.invoke(BALANCE_POSITION_SECOND) },
                    typeFreeOngkirCondition = { action.invoke(BALANCE_POSITION_THIRD) }
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
        when (type) {
            TYPE_TOKOPOINT -> typeTokopointCondition.invoke()
            TYPE_WALLET_OVO,
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


