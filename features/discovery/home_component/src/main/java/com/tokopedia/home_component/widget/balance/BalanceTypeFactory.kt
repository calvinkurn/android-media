package com.tokopedia.home_component.widget.balance

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by frenzel
 */
interface BalanceTypeFactory {
    fun type(visitable: BalanceItemUiModel): Int
    fun type(visitable: BalanceItemErrorUiModel): Int
    fun type(visitable: BalanceItemLoadingUiModel): Int
    fun type(visitable: AddressUiModel): Int
    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbstractViewHolder<BalanceItemVisitable>
}
