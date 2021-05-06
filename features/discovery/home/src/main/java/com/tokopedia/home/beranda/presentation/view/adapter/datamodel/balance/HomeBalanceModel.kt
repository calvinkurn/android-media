package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_LOADING
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_UNKNOWN
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction

data class HomeBalanceModel (
        var balanceDrawerItemModels: MutableMap<Int, BalanceDrawerItemModel> = mutableMapOf(),
        var balanceType: Int? = null,
        var isTokopointsOrOvoFailed: Boolean = false
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
        when(balanceType) {
            TYPE_STATE_1 -> {
                balanceDrawerItemModels[0] = BalanceDrawerItemModel()
                balanceDrawerItemModels[1] = BalanceDrawerItemModel()
                balanceDrawerItemModels[2] = BalanceDrawerItemModel()
            }
            TYPE_STATE_2 -> {
                balanceDrawerItemModels[0] = BalanceDrawerItemModel()
                balanceDrawerItemModels[1] = BalanceDrawerItemModel()
                balanceDrawerItemModels[2] = BalanceDrawerItemModel()
                balanceDrawerItemModels[3] = BalanceDrawerItemModel()
            }
            TYPE_STATE_3 -> {
                balanceDrawerItemModels[0] = BalanceDrawerItemModel()
                balanceDrawerItemModels[1] = BalanceDrawerItemModel()
                balanceDrawerItemModels[2] = BalanceDrawerItemModel()
            }
        }
    }

    fun mapBalanceData(
            homeHeaderWalletAction: HomeHeaderWalletAction? = null,
            tokopointDrawerListHomeData: TokopointsDrawerListHomeData? = null,
            pendingCashBackData: PendingCashbackModel? = null
    ) {
        mapTokopoint(tokopointDrawerListHomeData)
        mapWallet(homeHeaderWalletAction)
        mapPendingCashback(homeHeaderWalletAction, pendingCashBackData)
    }

    fun mapErrorTokopoints() {
        when(balanceType) {
            TYPE_STATE_1 -> {
                balanceDrawerItemModels[1] = getDefaultTokopointsErrorState()
                balanceDrawerItemModels[2] = getDefaultCouponsRewardsErrorState()
            }
            TYPE_STATE_2 -> {
                balanceDrawerItemModels[1] = getDefaultBBOErrorState()
                balanceDrawerItemModels[2] = getDefaultTokopointsErrorState()
                balanceDrawerItemModels[3] = getDefaultCouponsRewardsErrorState()
            }
            TYPE_STATE_3 -> {
                balanceDrawerItemModels[0] = getDefaultTokopointsErrorState()
                balanceDrawerItemModels[1] = getDefaultCouponsRewardsErrorState()
                balanceDrawerItemModels[2] = getDefaultBBOErrorState()
            }
        }
    }

    fun mapErrorWallet() {
        when(balanceType) {
            TYPE_STATE_1 -> {
                balanceDrawerItemModels[0] = getDefaultOvoErrorState()
            }
            TYPE_STATE_2 -> {
                balanceDrawerItemModels[0] = getDefaultOvoErrorState()
            }
        }
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

    private fun mapPendingCashback(homeHeaderWalletAction: HomeHeaderWalletAction?, pendingCashBackData: PendingCashbackModel?) {
        pendingCashBackData?.let { pendingCashBackData ->
            val type = TYPE_WALLET_PENDING_CASHBACK
            flagStateCondition(
                    itemType = type,
                    action = {
                        if (pendingCashBackData.walletType == OVO_WALLET_TYPE) {
                            balanceDrawerItemModels[it] =
                                    BalanceDrawerItemModel(
                                            applinkActionText = homeHeaderWalletAction?.appLinkActionButton?:"",
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
            val type = when(homeHeaderWalletAction.walletType) {
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
                        balanceDrawerItemModels[it] = homeHeaderWalletAction.mapToHomeBalanceItemModel(itemType = type, state = STATE_SUCCESS)
                    }
            )
        }
    }

    private fun mapTokopoint(tokopointDrawerListHomeData: TokopointsDrawerListHomeData?) {
        tokopointDrawerListHomeData?.tokopointsDrawerList?.drawerList?.forEach { drawerContent ->
            val type = getDrawerType(drawerContent.type)
            flagStateCondition(
                    itemType = type,
                    action = {
                        balanceDrawerItemModels[it] = drawerContent.mapToHomeBalanceItemModel(
                                drawerItemType = type,
                                state = STATE_SUCCESS,
                                defaultIconRes = when (type) {
                                    TYPE_TOKOPOINT -> R.drawable.ic_new_tokopoints
                                    TYPE_COUPON -> R.drawable.ic_new_coupon
                                    TYPE_REWARDS -> R.drawable.ic_new_points
                                    TYPE_FREE_ONGKIR -> R.drawable.ic_new_bbo
                                    else -> null
                                }
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
                        typeRewardsCondition = { action.invoke(1) },
                        typeFreeOngkirCondition = { action.invoke(2) }
                )
            }
            TYPE_STATE_2 -> {
                itemTypeCondition(
                        itemType,
                        typeWalletCondition = { action.invoke(0) },
                        typeFreeOngkirCondition = { action.invoke(1) },
                        typeTokopointCondition = { action.invoke(2) },
                        typeCouponCondition = { action.invoke(3) },
                        typeRewardsCondition = { action.invoke(3) }
                )
            }
            TYPE_STATE_3 -> {
                itemTypeCondition(
                        itemType,
                        typeTokopointCondition = { action.invoke(0) },
                        typeCouponCondition = { action.invoke(1) },
                        typeRewardsCondition = { action.invoke(1) },
                        typeFreeOngkirCondition = { action.invoke(2) }
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
            TYPE_WALLET_OVO, TYPE_WALLET_OTHER, TYPE_WALLET_WITH_TOPUP, TYPE_WALLET_PENDING_CASHBACK -> typeWalletCondition.invoke()
            TYPE_COUPON -> typeCouponCondition.invoke()
            TYPE_FREE_ONGKIR -> typeFreeOngkirCondition.invoke()
            TYPE_REWARDS -> typeRewardsCondition.invoke()
        }
    }
}


