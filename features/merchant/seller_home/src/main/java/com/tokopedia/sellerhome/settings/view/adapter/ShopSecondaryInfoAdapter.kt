package com.tokopedia.sellerhome.settings.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopStatusUiModel
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.RmTransactionData
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.*
import com.tokopedia.shop.common.view.model.TokoPlusBadgeUiModel

class ShopSecondaryInfoAdapter(
    typeFactory: ShopSecondaryInfoAdapterTypeFactory,
    val context: Context
) : BaseListAdapter<Visitable<ShopSecondaryInfoAdapterFactory>, ShopSecondaryInfoAdapterTypeFactory>(
    typeFactory
) {

    companion object {
        private const val START_INDEX = 0
        private const val TOTAL_TOKOMEMBER_ZERO = "0"
    }

    fun showInitialInfo() {
        clearAllElements()
        addElement(getLoadingVisitableList())
    }

    fun setShopOperationalData(state: SettingResponseState<ShopOperationalData>) {
        visitables?.indexOfFirst { it is ShopOperationalWidgetUiModel }?.let { index ->
            if (index >= 0) {
                visitables[index] = ShopOperationalWidgetUiModel(state)
                notifyItemChanged(index)
            }
        }
    }

    fun setShopStatusData(state: SettingResponseState<ShopStatusUiModel>) {
        when (state) {
            is SettingResponseState.SettingSuccess -> {
                setShopStatusSuccess(state.data)
            }
            is SettingResponseState.SettingError -> {
                setShopStatusError(state.throwable)
            }
            else -> {
                setShopStatusLoading()
            }
        }
    }

    fun setReputationBadgeData(state: SettingResponseState<String>) {
        visitables?.indexOfFirst { it is ReputationBadgeWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                visitables[index] = ReputationBadgeWidgetUiModel(state)
                notifyItemChanged(index)
            }
        }
    }

    fun setTokoMemberData(state: SettingResponseState<String>) {
        visitables?.indexOfFirst { it is TokoMemberWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                when (state) {
                    is SettingResponseState.SettingSuccess -> {
                        if (state.data == TOTAL_TOKOMEMBER_ZERO) {
                            visitables.removeAt(index)
                            notifyItemRemoved(index)
                        } else {
                            visitables[index] = TokoMemberWidgetUiModel(state)
                            notifyItemChanged(index)
                        }
                    }
                    is SettingResponseState.SettingError -> {
                        visitables.removeAt(index)
                        notifyItemRemoved(index)
                    }
                    else -> {
                        visitables[index] = TokoMemberWidgetUiModel(state)
                        notifyItemChanged(index)
                    }
                }
            } else {
                visitables?.indexOfFirst { it is ShopFollowersWidgetUiModel }
                    ?.let { indexFollower ->
                        if (index >= START_INDEX) {
                            visitables[indexFollower - 1] = TokoMemberWidgetUiModel(state)
                            notifyItemInserted(indexFollower - 1)
                        }
                    }
            }
        }
    }

    fun setShopFollowersData(state: SettingResponseState<String>) {
        visitables?.indexOfFirst { it is ShopFollowersWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                visitables[index] = ShopFollowersWidgetUiModel(state)
                notifyItemChanged(index)
            }
        }
    }

    fun setFreeShippingData(state: SettingResponseState<TokoPlusBadgeUiModel>) {
        when (state) {
            is SettingResponseState.SettingSuccess -> {
                val freeShipping = state.data.freeShipping
                setOnFreeShippingPlusSuccess<FreeShippingWidgetUiModel>(freeShipping.status to freeShipping.badgeUrl)

                val tokoPlus = state.data.tokoPlus
                setOnFreeShippingPlusSuccess<TokoPlusWidgetUiModel>(tokoPlus.status to tokoPlus.badgeUrl)
            }
            is SettingResponseState.SettingError -> {
                setOnFreeShippingPlusError<FreeShippingWidgetUiModel>(state.throwable)
                setOnFreeShippingPlusError<TokoPlusWidgetUiModel>(state.throwable)
            }
            else -> {
                setOnFreeShippingPlusLoading<FreeShippingWidgetUiModel>()
                setOnFreeShippingPlusLoading<TokoPlusWidgetUiModel>()
            }
        }
    }

    private inline fun <reified T : ShopSecondaryInfoWidget<String>> setOnFreeShippingPlusSuccess(
        data: Pair<Boolean, String>
    ) {
        val (isActive, badgeUrl) = data
        visitables?.run {
            indexOfFirst { it is T }.let { index ->
                val model = T::class.java.newInstance()
                if (isActive) {
                    model.state = SettingResponseState.SettingSuccess(badgeUrl)
                }
                when {
                    index >= START_INDEX && isActive -> {
                        this[index] = model
                        notifyItemChanged(index)
                    }
                    index >= START_INDEX && !isActive -> {
                        removeAt(index)
                        notifyItemRemoved(index)
                    }
                    isActive -> {
                        addElement(model)
                        notifyItemInserted(lastIndex)
                    }
                }
            }
        }
    }

    private inline fun <reified T : ShopSecondaryInfoWidget<String>> setOnFreeShippingPlusError(
        throwable: Throwable
    ) {
        visitables?.indexOfFirst { it is T }?.let { index ->
            val model = T::class.java.newInstance()
            model.state = SettingResponseState.SettingError(throwable)
            if (index >= START_INDEX) {
                visitables[index] = model
                notifyItemChanged(index)
            } else {
                visitables?.add(model)
                notifyItemInserted(lastIndex)
            }
        }
    }

    private inline fun <reified T : ShopSecondaryInfoWidget<String>> setOnFreeShippingPlusLoading() {
        visitables?.indexOfFirst { it is T }?.let { index ->
            val model = T::class.java.newInstance()
            model.state = SettingResponseState.SettingLoading
            if (index >= START_INDEX) {
                visitables[index] = model
                notifyItemChanged(index)
            } else {
                visitables?.add(model)
                notifyItemInserted(lastIndex)
            }
        }
    }

    private fun setShopStatusSuccess(shopStatus: ShopStatusUiModel) {
        shopStatus.userShopInfoWrapper.shopType?.let { shopType ->
            visitables?.indexOfFirst { it is ShopStatusWidgetUiModel }?.let { index ->
                if (index >= START_INDEX) {
                    visitables[index] = ShopStatusWidgetUiModel(
                        SettingResponseState.SettingSuccess(shopType),
                        shopStatus.userShopInfoWrapper.userShopInfoUiModel
                    )
                    notifyItemChanged(index)

                    if (shopType is RegularMerchant) {
                        shopStatus.userShopInfoWrapper.userShopInfoUiModel?.let { userShopInfo ->
                            setRmTranscationWidget(
                                index,
                                SettingResponseState.SettingSuccess(userShopInfo)
                            )
                        }
                    } else {
                        removeRmTransactionWidget()
                    }
                }
            }
        }
    }

    private fun setShopStatusLoading() {
        visitables?.indexOfFirst { it is ShopStatusWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                val loadingState = SettingResponseState.SettingLoading
                visitables[index] = ShopStatusWidgetUiModel(loadingState)
                notifyItemChanged(index)
                setRmTranscationWidget(index, loadingState)
            }
        }
    }

    private fun setShopStatusError(throwable: Throwable) {
        visitables?.indexOfFirst { it is ShopStatusWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                val errorState = SettingResponseState.SettingError(throwable)
                visitables[index] = ShopStatusWidgetUiModel(errorState)
                notifyItemChanged(index)
                setRmTranscationWidget(index, errorState)
            }
        }
    }

    private fun setRmTranscationWidget(
        shopStatusIndex: Int,
        userShopInfoState: SettingResponseState<UserShopInfoWrapper.UserShopInfoUiModel>
    ) {
        val rmTransactionWidget =
            when (userShopInfoState) {
                is SettingResponseState.SettingSuccess -> {
                    val userShopInfo = userShopInfoState.data
                    val rmTransactionData = RmTransactionData(
                        totalTransaction = userShopInfo.totalTransaction,
                        dateCreated = userShopInfo.dateCreated,
                        isBeforeOnDate = userShopInfo.isBeforeOnDate
                    )
                    RMTransactionWidgetUiModel(SettingResponseState.SettingSuccess(rmTransactionData))
                }
                is SettingResponseState.SettingError ->
                    RMTransactionWidgetUiModel(SettingResponseState.SettingError(userShopInfoState.throwable))
                else ->
                    RMTransactionWidgetUiModel(SettingResponseState.SettingLoading)
            }
        val rmTransactionIndex = shopStatusIndex + 1
        if (visitables?.get(rmTransactionIndex) is RMTransactionWidgetUiModel) {
            val totalTransaction =
                (rmTransactionWidget.state as? SettingResponseState.SettingSuccess)?.data?.totalTransaction.orZero()
            if (totalTransaction > Constant.ShopStatus.THRESHOLD_TRANSACTION) {
                visitables.removeAt(rmTransactionIndex)
                notifyItemRemoved(rmTransactionIndex)
            } else {
                visitables[rmTransactionIndex] = rmTransactionWidget
                notifyItemChanged(rmTransactionIndex)
            }
        } else {
            visitables?.add(rmTransactionIndex, rmTransactionWidget)
            notifyItemInserted(rmTransactionIndex)
        }
    }

    private fun removeRmTransactionWidget() {
        visitables?.run {
            indexOfFirst { it is RMTransactionWidgetUiModel }.let { index ->
                if (index >= START_INDEX) {
                    removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
    }

    private fun getLoadingVisitableList(): List<Visitable<ShopSecondaryInfoAdapterFactory>> {
        return listOf(
            ShopOperationalWidgetUiModel(SettingResponseState.SettingLoading),
            ShopStatusWidgetUiModel(SettingResponseState.SettingLoading),
            RMTransactionWidgetUiModel(SettingResponseState.SettingLoading),
            ReputationBadgeWidgetUiModel(SettingResponseState.SettingLoading),
            TokoMemberWidgetUiModel(SettingResponseState.SettingLoading),
            ShopFollowersWidgetUiModel(SettingResponseState.SettingLoading),
            FreeShippingWidgetUiModel(SettingResponseState.SettingLoading),
            TokoPlusWidgetUiModel(SettingResponseState.SettingLoading)
        )
    }
}
