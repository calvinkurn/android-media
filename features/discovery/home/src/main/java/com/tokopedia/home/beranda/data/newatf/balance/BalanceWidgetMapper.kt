package com.tokopedia.home.beranda.data.newatf.balance

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.ERROR_GOPAY_EMPTY
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.RESERVE_BALANCE_FORMAT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.WALLET_CODE_GOPAY
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.WALLET_CODE_GOPAY_POINTS
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.AddressUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemErrorUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemLoadingUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.BalanceWidgetErrorUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.BalanceWidgetUiModel
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
                balanceItems.addIfNotNull(it.mapToBalanceItemVisitable(idx))
            }
            BalanceWidgetUiModel(balanceItems)
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun BalanceItemModel.mapToBalanceItemVisitable(position: Int): BalanceItemVisitable? {
        val contentType = when(type) {
            BalanceItemModel.GOPAY -> BalanceItemVisitable.ContentType.GoPay(isLinked)
            BalanceItemModel.REWARDS -> BalanceItemVisitable.ContentType.Rewards
            BalanceItemModel.ADDRESS -> BalanceItemVisitable.ContentType.Address
            else -> return null
        }
        return when(state) {
            DataStatus.LOADING -> BalanceItemLoadingUiModel(contentType, position)
            DataStatus.ERROR -> BalanceItemErrorUiModel(contentType, position)
            DataStatus.SUCCESS -> mapToBalanceItemUiModel(contentType, position)
            else -> null
        }
    }

    private fun BalanceItemModel.mapToBalanceItemUiModel(
        contentType: BalanceItemVisitable.ContentType,
        position: Int
    ): BalanceItemVisitable {
        return if(contentType is BalanceItemVisitable.ContentType.Address) {
            AddressUiModel(position)
        } else {
            BalanceItemUiModel(
                contentType = contentType,
                applink = applink,
                url = url,
                imageUrl = imageUrl,
                text = text,
                position = position,
            )
        }
    }

    fun mapToBalanceItemModel(
        data: Balances,
        type: String
    ): BalanceItemModel? {
        val balanceTitle: String
        if (data.isLinked) {
            val gopayBalance = data.balance.find { it.walletCode == WALLET_CODE_GOPAY }
            if (gopayBalance?.amountFmt.isNullOrEmpty()) {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_BALANCE_EMPTY,
                    throwable = MessageErrorException(ERROR_GOPAY_EMPTY),
                    reason = ERROR_GOPAY_EMPTY,
                    data = data.walletName
                )
                return null
            } else {
                balanceTitle = gopayBalance?.amountFmt.orEmpty()
            }
        } else {
            val reserveBalance = data.reserveBalance.find { it.walletCode == WALLET_CODE_GOPAY_POINTS }
            if (reserveBalance?.amount.isMoreThanZero()) {
                balanceTitle = RESERVE_BALANCE_FORMAT.format(reserveBalance?.amountFmt)
            } else {
                balanceTitle = data.walletName
            }
        }
        return BalanceItemModel(
            type = type,
            url = data.redirectUrl,
            imageUrl = data.iconUrl,
            text = balanceTitle,
            state = DataStatus.SUCCESS,
            isLinked = data.isLinked
        )
    }

    fun mapToBalanceItemModel(
        data: TokopointsDrawer,
        type: String,
    ): BalanceItemModel {
        return BalanceItemModel(
            type = type,
            applink = data.redirectAppLink,
            url = data.redirectURL,
            imageUrl = data.iconImageURL,
            text = data.label,
            state = DataStatus.SUCCESS
        )
    }

    private fun MutableList<BalanceItemVisitable>.addIfNotNull(item: BalanceItemVisitable?) {
        item ?: return
        this.add(item)
    }
}
