package com.tokopedia.search.result.mps

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateFilterDataView
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateKeywordDataView
import com.tokopedia.search.result.mps.violationstate.ViolationStateDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView

interface MPSTypeFactory {

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>

    fun type(mpsShopWidgetDataView: MPSShopWidgetDataView): Int

    fun type(mpsChooseAddressDataView: ChooseAddressDataView): Int

    fun type(mpsEmptyStateKeywordDataView: MPSEmptyStateKeywordDataView): Int

    fun type(mpsEmptyStateFilterDataView: MPSEmptyStateFilterDataView): Int

    fun type(violationStateDataView: ViolationStateDataView): Int

}
