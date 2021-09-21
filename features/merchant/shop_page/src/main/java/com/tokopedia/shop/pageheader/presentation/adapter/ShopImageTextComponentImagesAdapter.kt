package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetImageTextComponentImageViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetImageTextComponentImageViewHolder.Companion.LAYOUT_RES
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageTextComponentUiModel

class ShopImageTextComponentImagesAdapter : RecyclerView.Adapter<ShopPerformanceWidgetImageTextComponentImageViewHolder>() {
    private var listImageData: List<ShopHeaderImageTextComponentUiModel.Images.Data> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPerformanceWidgetImageTextComponentImageViewHolder {
        return ShopPerformanceWidgetImageTextComponentImageViewHolder(parent.inflateLayout(LAYOUT_RES))
    }

    override fun onBindViewHolder(holder: ShopPerformanceWidgetImageTextComponentImageViewHolder, position: Int) {
        holder.bind(listImageData.getOrNull(position))
    }

    override fun getItemCount(): Int = listImageData.size

    fun setImagesData(listImages: List<ShopHeaderImageTextComponentUiModel.Images.Data>) {
        listImageData = listImages
        notifyDataSetChanged()
    }

}