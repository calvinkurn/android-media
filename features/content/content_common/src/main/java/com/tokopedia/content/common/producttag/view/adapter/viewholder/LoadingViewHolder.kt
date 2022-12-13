package com.tokopedia.content.common.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.common.databinding.ItemProductTagLoadingListBinding

/**
 * Created By : Jonathan Darwin on May 26, 2022
 */
class LoadingViewHolder(
    binding: ItemProductTagLoadingListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        val layoutParams = itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    companion object {

        fun create(parent: ViewGroup) = LoadingViewHolder(
            ItemProductTagLoadingListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }
}