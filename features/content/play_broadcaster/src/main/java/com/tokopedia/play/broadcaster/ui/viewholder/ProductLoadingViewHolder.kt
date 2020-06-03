package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 03/06/20
 */
class ProductLoadingViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivLoading: ImageView = itemView.findViewById(R.id.iv_loading)

    init {
        Glide.with(ivLoading.context)
                .asGif()
                .load(com.tokopedia.resources.common.R.drawable.ic_loading_indeterminate)
                .into(ivLoading)
    }

    companion object {

        val LAYOUT = R.layout.item_product_loading
    }
}