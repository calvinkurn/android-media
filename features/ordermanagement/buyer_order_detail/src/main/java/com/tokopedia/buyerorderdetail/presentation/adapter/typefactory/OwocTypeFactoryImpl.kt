package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import android.widget.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocAddonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductListHeaderViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductListToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocSectionViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocShimmerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocThickDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocTickerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductListToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel

class OwocTypeFactoryImpl(
    private val navigator: BuyerOrderDetailNavigator,
    private val owocProductListToggleListener: OwocProductListToggleViewHolder.Listener
): BaseAdapterTypeFactory(), OwocTypeFactory {

    override fun type(owocTickerUiModel: OwocTickerUiModel): Int {
        return OwocTickerViewHolder.LAYOUT
    }

    override fun type(owocShimmerUiModel: OwocShimmerUiModel): Int {
        return OwocShimmerViewHolder.LAYOUT
    }

    override fun type(owocProductListHeaderUiModel: OwocProductListUiModel.ProductListHeaderUiModel): Int {
        return OwocProductListHeaderViewHolder.LAYOUT
    }

    override fun type(owocProductUiModel: OwocProductListUiModel.ProductUiModel): Int {
        return OwocProductViewHolder.LAYOUT
    }

    override fun type(owocProductBundlingUiModel: OwocProductListUiModel.ProductBundlingUiModel): Int {
        return OwocProductBundlingViewHolder.LAYOUT
    }

    override fun type(owocProductListToggleUiModel: OwocProductListUiModel.ProductListToggleUiModel): Int {
        return OwocProductListToggleViewHolder.LAYOUT
    }

    override fun type(owocAddonsListUiModel: OwocAddonsListUiModel): Int {
        return OwocAddonsViewHolder.LAYOUT
    }

    override fun type(owocThickDividerUiModel: OwocThickDividerUiModel): Int {
        return OwocThickDividerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OwocShimmerViewHolder.LAYOUT -> OwocShimmerViewHolder(parent)
            OwocTickerViewHolder.LAYOUT -> OwocTickerViewHolder(parent, navigator)
            OwocProductListHeaderViewHolder.LAYOUT -> OwocProductListHeaderViewHolder(parent, navigator)
            OwocProductViewHolder.LAYOUT -> OwocProductViewHolder(parent, navigator)
            OwocProductBundlingViewHolder.LAYOUT -> OwocProductBundlingViewHolder(parent, navigator)
            OwocProductListToggleViewHolder.LAYOUT -> OwocProductListToggleViewHolder(parent, owocProductListToggleListener)
            OwocAddonsViewHolder.LAYOUT -> OwocAddonsViewHolder(parent)
            OwocThickDividerViewHolder.LAYOUT -> OwocThickDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}
