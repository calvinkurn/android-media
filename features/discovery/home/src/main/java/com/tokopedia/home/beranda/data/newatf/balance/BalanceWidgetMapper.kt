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
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.BalanceWidgetLoadingUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.BalanceWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.LoginWidgetUiModel
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.widget.common.DataStatus
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class BalanceWidgetMapper @Inject constructor() {

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
            balanceTitle = if (reserveBalance?.amount.isMoreThanZero()) {
                RESERVE_BALANCE_FORMAT.format(reserveBalance?.amountFmt)
            } else {
                data.walletName
            }
        }
        return BalanceItemModel(
            type = type,
            url = data.redirectUrl,
            imageUrl = data.iconUrl,
            text = balanceTitle,
            state = BalanceItemModel.STATE_SUCCESS,
            isLinked = data.isLinked
        )
    }

    fun mapToBalanceItemModel(
        data: TokopointsDrawer,
        type: String,
    ): BalanceItemModel? {
        val text = data.sectionContent.getOrNull(1)?.textAttributes?.text ?: return null
        return BalanceItemModel(
            type = type,
            applink = data.redirectAppLink,
            url = data.redirectURL,
            imageUrl = "https://images.tokopedia.net/img/tokopoints/benefit/kupon.png",
            text = text,
            state = BalanceItemModel.STATE_SUCCESS
        )
    }

    fun mapToBalanceItemModel(
        data: LocalCacheModel,
        type: String,
    ): BalanceItemModel {
        return BalanceItemModel(
            type = type,
            text = data.convertToLocationParams(),
            state = BalanceItemModel.STATE_SUCCESS
        )
    }

    fun asVisitable(
        data: DynamicBalanceWidgetModel,
        atfStatus: Int,
    ): Visitable<*> {
        return when(atfStatus) {
            AtfKey.STATUS_ERROR -> BalanceWidgetErrorUiModel()
            AtfKey.STATUS_LOADING -> BalanceWidgetLoadingUiModel()
            else -> {
                if(!data.isLoggedIn) {
                    LoginWidgetUiModel()
                } else {
                    val balanceItems = mutableListOf<BalanceItemVisitable>()
                    data.balanceItems.forEachIndexed { idx, it ->
                        balanceItems.addIfNotNull(it.mapToBalanceItemVisitable(idx))
                    }
                    BalanceWidgetUiModel(balanceItems)
                }
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun BalanceItemModel.mapToBalanceItemVisitable(position: Int): BalanceItemVisitable? {
        val contentType = when(type) {
            BalanceItemModel.GOPAY -> BalanceItemVisitable.ContentType.GoPay(isLinked)
            BalanceItemModel.REWARDS -> BalanceItemVisitable.ContentType.Rewards
            BalanceItemModel.ADDRESS -> return mapToAddressUiModel(position)
            else -> return null
        }
        return when(state) {
            BalanceItemModel.STATE_LOADING -> BalanceItemLoadingUiModel(contentType, position)
            BalanceItemModel.STATE_ERROR -> BalanceItemErrorUiModel(contentType, position)
            BalanceItemModel.STATE_SUCCESS -> mapToBalanceItemUiModel(contentType, position)
            else -> null
        }
    }

    private fun BalanceItemModel.mapToAddressUiModel(
        position: Int
    ): BalanceItemVisitable {
        return AddressUiModel(text, position)
    }

    private fun BalanceItemModel.mapToBalanceItemUiModel(
        contentType: BalanceItemVisitable.ContentType,
        position: Int
    ): BalanceItemVisitable {
        return BalanceItemUiModel(
            contentType = contentType,
            applink = applink,
            url = url,
            imageUrl = imageUrl,
            text = text,
            position = position,
        )
    }

    private fun MutableList<BalanceItemVisitable>.addIfNotNull(item: BalanceItemVisitable?) {
        item ?: return
        this.add(item)
    }
}
