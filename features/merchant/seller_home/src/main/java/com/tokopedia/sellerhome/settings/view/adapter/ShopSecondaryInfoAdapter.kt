package com.tokopedia.sellerhome.settings.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopStatusUiModel
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.PMTransactionDataUiModel
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.FreeShippingWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.PMTransactionWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ReputationBadgeWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopFollowersWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopOperationalWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopSecondaryInfoWidget
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopStatusWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoMemberWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoPlusWidgetUiModel
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

    fun setShopTransactionData(transaction: PMTransactionDataUiModel) {
        visitables?.indexOfFirst { it is PMTransactionWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                visitables[index] = PMTransactionWidgetUiModel(
                    SettingResponseState.SettingSuccess(transaction)
                )
                notifyItemChanged(index)
            }
        }
    }

    fun removeShopTransactionData() {
        visitables?.indexOfFirst { it is PMTransactionWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                visitables.removeAt(index)
                notifyItemRemoved(index)
            }
        }
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
            }
        }
    }

    private fun setShopStatusError(throwable: Throwable) {
        visitables?.indexOfFirst { it is ShopStatusWidgetUiModel }?.let { index ->
            if (index >= START_INDEX) {
                val errorState = SettingResponseState.SettingError(throwable)
                visitables[index] = ShopStatusWidgetUiModel(errorState)
                notifyItemChanged(index)
            }
        }
    }

    private fun getLoadingVisitableList(): List<Visitable<ShopSecondaryInfoAdapterFactory>> {
        return listOf(
            ShopOperationalWidgetUiModel(SettingResponseState.SettingLoading),
            ShopStatusWidgetUiModel(SettingResponseState.SettingLoading),
            PMTransactionWidgetUiModel(SettingResponseState.SettingLoading),
            ReputationBadgeWidgetUiModel(SettingResponseState.SettingLoading),
            TokoMemberWidgetUiModel(SettingResponseState.SettingLoading),
            ShopFollowersWidgetUiModel(SettingResponseState.SettingLoading),
            FreeShippingWidgetUiModel(SettingResponseState.SettingLoading),
            TokoPlusWidgetUiModel(SettingResponseState.SettingLoading)
        )
    }
}
