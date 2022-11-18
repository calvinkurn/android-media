package com.tokopedia.product.detail.view.viewholder.product_detail_info.nested_adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.product_detail_info.nested_adapter.view_holder.ItemProductDetailInfoViewHolder

/**
 * Created by Yehezkiel on 01/02/21
 */
class ProductDetailInfoAdapter(
    private val listener: DynamicProductDetailListener
) : ListAdapter<ProductDetailInfoContent, ItemProductDetailInfoViewHolder>(DIFF_CALLBACK) {

    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private var isProductCatalog = false

    fun setAnnotationData(
        data: List<ProductDetailInfoContent>,
        isProductCatalog: Boolean,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        this.componentTrackDataModel = componentTrackDataModel
        this.isProductCatalog = isProductCatalog
        submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemProductDetailInfoViewHolder {
        return ItemProductDetailInfoViewHolder.create(
            parent = parent,
            listener = listener,
            isProductCatalog = isProductCatalog
        )
    }

    override fun onBindViewHolder(holder: ItemProductDetailInfoViewHolder, position: Int) {
        holder.bind(
            data = getItem(position),
            trackData = componentTrackDataModel,
            itemCount = itemCount
        )
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductDetailInfoContent>() {
            override fun areItemsTheSame(
                oldItem: ProductDetailInfoContent,
                newItem: ProductDetailInfoContent
            ): Boolean = oldItem.title == newItem.title && oldItem.subtitle == newItem.subtitle

            override fun areContentsTheSame(
                oldItem: ProductDetailInfoContent,
                newItem: ProductDetailInfoContent
            ): Boolean = oldItem == newItem
        }
    }
}