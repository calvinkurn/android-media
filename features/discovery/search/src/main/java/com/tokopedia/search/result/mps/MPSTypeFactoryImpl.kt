package com.tokopedia.search.result.mps

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressViewHolder
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetViewHolder
import com.tokopedia.search.utils.FragmentProvider

class MPSTypeFactoryImpl(
    private val recycledViewPool: RecycledViewPool,
    private val fragmentProvider: FragmentProvider,
    private val chooseAddressListener: ChooseAddressListener,
): BaseAdapterTypeFactory(), MPSTypeFactory {

    override fun type(mpsShopWidgetDataView: MPSShopWidgetDataView): Int =
        MPSShopWidgetViewHolder.LAYOUT

    override fun type(mpsChooseAddressDataView: ChooseAddressDataView): Int =
        ChooseAddressViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            MPSShopWidgetViewHolder.LAYOUT -> MPSShopWidgetViewHolder(view, recycledViewPool)
            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(
                view,
                fragmentProvider,
                chooseAddressListener,
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
