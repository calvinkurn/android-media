package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListHeaderListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocAddonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductListHeaderViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductListToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocThickDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocThickDividerUiModel

class OwocProductListTypeFactoryImpl(
    private val navigator: BuyerOrderDetailNavigator?,
    private val owocSectionGroupListener: OwocProductListListener,
    private val owocProductListHeaderListener: OwocProductListHeaderListener,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : BaseAdapterTypeFactory(), OwocProductListTypeFactory {

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
            OwocProductListHeaderViewHolder.LAYOUT -> OwocProductListHeaderViewHolder(parent, owocProductListHeaderListener)
            OwocProductViewHolder.LAYOUT -> OwocProductViewHolder(parent)
            OwocProductBundlingViewHolder.LAYOUT -> OwocProductBundlingViewHolder(parent, navigator, recyclerviewPoolListener)
            OwocProductListToggleViewHolder.LAYOUT -> OwocProductListToggleViewHolder(parent, owocSectionGroupListener)
            OwocAddonsViewHolder.LAYOUT -> OwocAddonsViewHolder(parent)
            OwocThickDividerViewHolder.LAYOUT -> OwocThickDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
