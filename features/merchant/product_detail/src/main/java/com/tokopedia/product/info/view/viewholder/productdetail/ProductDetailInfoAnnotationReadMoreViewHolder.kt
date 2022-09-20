package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsItemProductDetailAnnotationReadmoreBinding
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.models.ProductDetailInfoAnnotationReadMoreDataModel

/**
 * Created by yovi.putra on 20/09/22"
 * Project name: android-tokopedia-core
 **/

class ProductDetailInfoAnnotationReadMoreViewHolder(
    view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoAnnotationReadMoreDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_annotation_readmore
    }

    private val binding = BsItemProductDetailAnnotationReadmoreBinding.bind(view)

    override fun bind(element: ProductDetailInfoAnnotationReadMoreDataModel) = with(binding) {
        root.setOnClickListener {
            listener.goToSpecification(element.data)
        }
    }
}
