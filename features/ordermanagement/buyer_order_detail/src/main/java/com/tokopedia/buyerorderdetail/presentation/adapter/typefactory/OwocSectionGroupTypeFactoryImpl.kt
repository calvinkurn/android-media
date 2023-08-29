package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListHeaderListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocSectionGroupListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocErrorStateViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocSectionGroupViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocShimmerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocThickDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocTickerViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.OwocErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocSectionGroupUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel

class OwocSectionGroupTypeFactoryImpl(
    private val navigator: BuyerOrderDetailNavigator?,
    private val owocSectionGroupListener: OwocSectionGroupListener,
    private val owocProductListHeaderListener: OwocProductListHeaderListener,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
): BaseAdapterTypeFactory(), OwocSectionGroupTypeFactory {

    override fun type(owocTickerUiModel: OwocTickerUiModel): Int {
        return OwocTickerViewHolder.LAYOUT
    }

    override fun type(owocSectionGroupUiModel: OwocSectionGroupUiModel): Int {
        return OwocSectionGroupViewHolder.LAYOUT
    }

    override fun type(owocShimmerUiModel: OwocShimmerUiModel): Int {
        return OwocShimmerViewHolder.LAYOUT
    }

    override fun type(owocErrorUiModel: OwocErrorUiModel): Int {
        return OwocErrorStateViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OwocSectionGroupViewHolder.LAYOUT -> OwocSectionGroupViewHolder(parent, navigator, owocProductListHeaderListener, recyclerviewPoolListener)
            OwocErrorStateViewHolder.LAYOUT -> OwocErrorStateViewHolder(parent, owocSectionGroupListener)
            OwocShimmerViewHolder.LAYOUT -> OwocShimmerViewHolder(parent)
            OwocTickerViewHolder.LAYOUT -> OwocTickerViewHolder(parent, navigator)
            OwocThickDividerViewHolder.LAYOUT -> OwocThickDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}
