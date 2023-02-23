package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageTextComponentImageViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageTextComponentImageViewHolder.Companion.LAYOUT_RES
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageTextComponentUiModel

class ShopImageTextComponentImagesAdapter : RecyclerView.Adapter<ShopPageHeaderPerformanceWidgetImageTextComponentImageViewHolder>() {
    private var listImageData: List<ShopHeaderImageTextComponentUiModel.Images.Data> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPageHeaderPerformanceWidgetImageTextComponentImageViewHolder {
        return ShopPageHeaderPerformanceWidgetImageTextComponentImageViewHolder(parent.inflateLayout(LAYOUT_RES))
    }

    override fun onBindViewHolder(holderPageHeader: ShopPageHeaderPerformanceWidgetImageTextComponentImageViewHolder, position: Int) {
        holderPageHeader.bind(listImageData.getOrNull(position))
    }

    override fun getItemCount(): Int = listImageData.size

    fun setImagesData(listImages: List<ShopHeaderImageTextComponentUiModel.Images.Data>) {
        listImageData = listImages
        notifyDataSetChanged()
    }
}
