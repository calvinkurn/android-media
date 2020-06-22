package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductPreviewLoadingViewHolder

/**
 * Created by jegul on 19/06/20
 */
class PlayProductPreviewLoadingAdapterDelegate
    : TypedAdapterDelegate<ProductLoadingUiModel, ProductUiModel, ProductPreviewLoadingViewHolder>(ProductPreviewLoadingViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductLoadingUiModel, holder: ProductPreviewLoadingViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductPreviewLoadingViewHolder {
        val recyclerView = parent as RecyclerView
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
        return ProductPreviewLoadingViewHolder(basicView, gridLayoutManager)
    }

    override fun onBindViewHolder(itemList: List<ProductUiModel>, position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(itemList, position, Bundle.EMPTY, holder)
    }

    override fun onBindViewHolder(itemList: List<ProductUiModel>, position: Int, payloads: Bundle, holder: RecyclerView.ViewHolder) {
        require(holder is ProductPreviewLoadingViewHolder)
        holder.bind(itemList[position] as ProductLoadingUiModel, itemList.size)
    }
}