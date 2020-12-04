package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.view.fragment.ProductVideoDetailInterface
import com.tokopedia.product.detail.view.viewholder.ProductVideoDetailViewHolder
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

/**
 * Created by Yehezkiel on 01/12/20
 */
class ProductVideoDetailAdapter(private val productVideoCoordinator: ProductVideoCoordinator?,
                                private val listener: ProductVideoDetailInterface) : RecyclerView.Adapter<AbstractViewHolder<ProductVideoDataModel>>() {

    var mediaData: List<ProductVideoDataModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<ProductVideoDataModel> {
        return ProductVideoDetailViewHolder(LayoutInflater.from(parent.context)
                .inflate(ProductVideoDetailViewHolder.LAYOUT, parent, false), productVideoCoordinator, listener)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<ProductVideoDataModel>, position: Int) {
        holder.bind(mediaData[position])
    }

    override fun getItemCount(): Int = mediaData.size
}