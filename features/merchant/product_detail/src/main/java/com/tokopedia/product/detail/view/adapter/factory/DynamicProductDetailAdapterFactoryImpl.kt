package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ProductShopInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.util.OnImageClick
import com.tokopedia.product.detail.view.viewholder.ProductShopInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductSnapshotViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductSocialProofViewHolder

class DynamicProductDetailAdapterFactoryImpl(private val onImagePdpClick: OnImageClick,
                                             private val childFragmentManager: FragmentManager,
                                             private val onViewClickListener: View.OnClickListener) : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {

    override fun type(data: ProductSocialProofDataModel): Int {
        return ProductSocialProofDataModel.LAYOUT
    }

    override fun type(data: ProductShopInfoDataModel): Int {
        return ProductShopInfoDataModel.LAYOUT
    }

//    override fun type(data: ProductShippingInfoDataModel): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun type(data: ProductShopVoucherDataModel): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    override fun type(data: ProductSnapshotDataModel): Int = ProductSnapshotDataModel.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductSnapshotDataModel.LAYOUT -> ProductSnapshotViewHolder(view, onImagePdpClick, childFragmentManager)
            ProductShopInfoDataModel.LAYOUT -> ProductShopInfoViewHolder(view, onViewClickListener)
            ProductSocialProofDataModel.LAYOUT -> ProductSocialProofViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

}