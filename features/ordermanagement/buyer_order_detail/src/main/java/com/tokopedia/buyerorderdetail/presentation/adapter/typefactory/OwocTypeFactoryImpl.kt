package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import android.widget.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocSectionViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel

class OwocTypeFactoryImpl: BaseAdapterTypeFactory(), OwocTypeFactory {

    override fun type(owocTickerUiModel: OwocTickerUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(owocShimmerUiModel: OwocShimmerUiModel): Int {
        return OwocSectionViewHolder.LAYOUT
    }

    override fun type(owocProductListUiModel: OwocProductListUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OwocSectionViewHolder.LAYOUT -> OwocSectionViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}
