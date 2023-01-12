package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.EmptyStateViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.TobaccoViewHolder

class ProductManageAdapterFactoryImpl(
    private val listener: ProductViewHolder.ProductViewHolderView,
    private val campaignListener: ProductCampaignInfoListener
) : BaseAdapterTypeFactory(), ProductManageAdapterFactory {

    override fun type(uiModel: ProductUiModel): Int {
        return if (uiModel.isTobacco) {
            TobaccoViewHolder.LAYOUT
        } else {
            ProductViewHolder.LAYOUT
        }
    }

    override fun type(viewModel: EmptyModel?): Int = EmptyStateViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel?): Int = LoadingViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductViewHolder.LAYOUT -> ProductViewHolder(
                view,
                listener,
                campaignListener
            )
            TobaccoViewHolder.LAYOUT -> TobaccoViewHolder(view)
            EmptyStateViewHolder.LAYOUT -> EmptyStateViewHolder(view)
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
