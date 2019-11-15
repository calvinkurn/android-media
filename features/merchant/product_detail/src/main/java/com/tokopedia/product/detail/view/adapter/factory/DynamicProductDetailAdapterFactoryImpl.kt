package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.OnImageClick
import com.tokopedia.product.detail.view.viewholder.*

class DynamicProductDetailAdapterFactoryImpl(private val onImagePdpClick: OnImageClick,
                                             private val childFragmentManager: FragmentManager,
                                             private val listener: DynamicProductDetailListener) : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {

    override fun type(data: ProductDiscussionDataModel): Int {
        return ProductDiscussionViewHolder.LAYOUT
    }

    override fun type(data: ProductInfoDataModel): Int {
        return ProductInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductSocialProofDataModel): Int {
        return ProductSocialProofDataModel.LAYOUT
    }

    override fun type(data: ProductShopInfoDataModel): Int {
        return ProductShopInfoDataModel.LAYOUT
    }

    override fun type(data: ProductSnapshotDataModel): Int = ProductSnapshotDataModel.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductSnapshotDataModel.LAYOUT -> ProductSnapshotViewHolder(view, onImagePdpClick, childFragmentManager)
            ProductShopInfoDataModel.LAYOUT -> ProductShopInfoViewHolder(view, listener)
            ProductSocialProofDataModel.LAYOUT -> ProductSocialProofViewHolder(view)
            ProductInfoViewHolder.LAYOUT -> ProductInfoViewHolder(view, listener)
            ProductDiscussionViewHolder.LAYOUT -> ProductDiscussionViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }

}