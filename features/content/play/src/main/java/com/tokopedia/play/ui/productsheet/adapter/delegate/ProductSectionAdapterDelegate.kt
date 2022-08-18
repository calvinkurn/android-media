package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.ProductSectionViewHolder
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.R as commonR

/**
 * @author by astidhiyaa on 02/02/22
 */
class ProductSectionAdapterDelegate(
    listener: ProductSectionViewHolder.Listener
) : TypedAdapterDelegate<ProductSectionUiModel.Section, ProductSectionUiModel, ProductSectionViewHolder>(
    commonR.layout.view_play_empty
), ProductSectionViewHolder.Listener by listener {

    override fun onBindViewHolder(
        item: ProductSectionUiModel.Section,
        holder: ProductSectionViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductSectionViewHolder =
        ProductSectionViewHolder.create(
            parent = parent,
            listener = this,
        )
}