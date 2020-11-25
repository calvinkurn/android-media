package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductPreviewViewHolder

/**
 * Created by jegul on 26/05/20
 */
class PlayProductPreviewAdapterDelegate : TypedAdapterDelegate<ProductContentUiModel, ProductUiModel, ProductPreviewViewHolder>(ProductPreviewViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductContentUiModel, holder: ProductPreviewViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductPreviewViewHolder {
        val recyclerView = parent as RecyclerView
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
        return ProductPreviewViewHolder(basicView, gridLayoutManager)
    }

    override fun onBindViewHolder(itemList: List<ProductUiModel>, position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(itemList, position, Bundle.EMPTY, holder)
    }

    override fun onBindViewHolder(itemList: List<ProductUiModel>, position: Int, payloads: Bundle, holder: RecyclerView.ViewHolder) {
        require(holder is ProductPreviewViewHolder)
        holder.bind(itemList[position] as ProductContentUiModel, itemList.size)
    }
}