package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.view.util.OnImageClick
import com.tokopedia.product.detail.view.viewholder.ProductSnapshotViewHolder

class DynamicProductDetailAdapterFactoryImpl(private val onImagePdpClick: OnImageClick,
                                             private val childFragmentManager: FragmentManager) : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {

    override fun type(data: ProductSnapshotDataModel): Int = ProductSnapshotDataModel.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductSnapshotDataModel.LAYOUT -> ProductSnapshotViewHolder(view, onImagePdpClick, childFragmentManager)
            else -> super.createViewHolder(view, type)
        }
    }

}