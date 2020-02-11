package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.common.ImageAssets
import com.tokopedia.kotlin.extensions.view.inflateLayout

class BrandlistSearchNotFoundAdapter(): RecyclerView.Adapter<BrandlistSearchNotFoundAdapter.BrandlistSearchNotFoundViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandlistSearchNotFoundViewHolder {
        return BrandlistSearchNotFoundViewHolder(parent.inflateLayout(R.layout.view_brand_not_found))
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: BrandlistSearchNotFoundViewHolder, position: Int) {
        holder.bindData()
    }

    inner class BrandlistSearchNotFoundViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val context: Context
        val imgAsset: ImageView

        init {
            context = itemView.context
            imgAsset = itemView.findViewById(R.id.img_brand_not_found)
        }

        fun bindData() {
            ImageHandler.loadImage(context, imgAsset, ImageAssets.BRAND_NOT_FOUND, null)
        }

    }

}