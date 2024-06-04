package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory

/**
 * Created by frenzel
 */
interface BalanceTypeFactory: AdapterTypeFactory {
    fun type(visitable: BalanceItemUiModel): Int
    fun type(visitable: BalanceItemErrorUiModel): Int
    fun type(visitable: BalanceItemLoadingUiModel): Int
    fun type(visitable: AddressUiModel): Int
}
