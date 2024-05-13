package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.ERROR_GOPAY_EMPTY
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.RESERVE_BALANCE_FORMAT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.WALLET_CODE_GOPAY
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.WALLET_CODE_GOPAY_POINTS
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home_component.widget.balance.AddressUiModel
import com.tokopedia.home_component.widget.balance.BalanceItemErrorUiModel
import com.tokopedia.home_component.widget.balance.BalanceItemLoadingUiModel
import com.tokopedia.home_component.widget.balance.BalanceItemUiModel
import com.tokopedia.home_component.widget.balance.BalanceItemVisitable
import com.tokopedia.home_component.widget.balance.BalanceWidgetErrorUiModel
import com.tokopedia.home_component.widget.balance.BalanceWidgetUiModel
import com.tokopedia.home_component.widget.common.DataStatus
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class BalanceWidgetMapper @Inject constructor() {

    fun asVisitable(
        data: DynamicBalanceWidgetModel,
        atfData: AtfData,
    ): Visitable<*> {
        return if (atfData.atfStatus == AtfKey.STATUS_ERROR) {
            BalanceWidgetErrorUiModel()
        } else {
            val balanceItems = mutableListOf<BalanceItemVisitable>()
            data.balanceItems.forEachIndexed { idx, it ->
                when(it) {
                    is BalanceWalletModel -> balanceItems.addIfNotNull(it.mapToBalanceItemVisitable(idx))
                    is BalanceRewardsModel -> balanceItems.addIfNotNull(it.mapToBalanceItemVisitable(idx))
                }
            }
            balanceItems.add(AddressUiModel(balanceItems.size))
            BalanceWidgetUiModel(balanceItems)
        }
    }

    private fun BalanceWalletModel.mapToBalanceItemVisitable(position: Int): BalanceItemVisitable? {
        val contentType = BalanceItemVisitable.ContentType.GOPAY
        return when(state) {
            DataStatus.LOADING -> BalanceItemLoadingUiModel(contentType, position)
            DataStatus.ERROR -> BalanceItemErrorUiModel(contentType, position)
            DataStatus.SUCCESS -> data?.mapToBalanceItemUiModel(contentType, position)
            else -> null
        }
    }

    private fun Balances.mapToBalanceItemUiModel(
        contentType: BalanceItemVisitable.ContentType,
        position: Int
    ): BalanceItemUiModel? {
        val balanceTitle: String
        if (isLinked) {
            val gopayBalance = balance.find { it.walletCode == WALLET_CODE_GOPAY } ?: return null
            if (gopayBalance.amountFmt.isEmpty()) {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_BALANCE_EMPTY,
                    throwable = MessageErrorException(ERROR_GOPAY_EMPTY),
                    reason = ERROR_GOPAY_EMPTY,
                    data = walletName
                )
                return null
            } else {
                balanceTitle = gopayBalance.amountFmt
            }
        } else {
            val reserveBalance = reserveBalance.find { it.walletCode == WALLET_CODE_GOPAY_POINTS } ?: return null
            if (reserveBalance.amount.isMoreThanZero()) {
                balanceTitle = RESERVE_BALANCE_FORMAT.format(reserveBalance.amountFmt)
            } else {
                balanceTitle = walletName
            }
        }
        return BalanceItemUiModel(
            contentType = contentType,
            url = redirectUrl,
            imageUrl = iconUrl,
            text = balanceTitle,
            position = position,
        )
    }

    private fun BalanceRewardsModel.mapToBalanceItemVisitable(position: Int): BalanceItemVisitable? {
        val type = BalanceItemVisitable.Type.BALANCE
        val contentType = BalanceItemVisitable.ContentType.REWARDS
        return when(state) {
            DataStatus.LOADING -> BalanceItemLoadingUiModel(contentType, position)
            DataStatus.ERROR -> BalanceItemErrorUiModel(contentType, position)
            DataStatus.SUCCESS -> data?.mapToBalanceItemUiModel(type, contentType, position)
            else -> null
        }
    }

    private fun TokopointsDrawer.mapToBalanceItemUiModel(
        type: BalanceItemVisitable.Type,
        contentType: BalanceItemVisitable.ContentType,
        position: Int
    ): BalanceItemUiModel {
        return BalanceItemUiModel(
            contentType = contentType,
            applink = redirectAppLink,
            url = redirectURL,
            imageUrl = iconImageURL,
            text = label,
            position = position,
        )
    }

    private fun MutableList<BalanceItemVisitable>.addIfNotNull(item: BalanceItemVisitable?) {
        item ?: return
        this.add(item)
    }
}
