package com.tokopedia.product.detail.view.viewholder.bmgm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.databinding.BmgmProductItemBinding
import com.tokopedia.product.detail.databinding.BmgmProductShowMoreViewBinding
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMProductItemViewHolder(
    private val binding: BmgmProductItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val overlayBinding by lazyThreadSafetyNone {
        val view = binding.bmgmProductShowMoreStub.inflate()
        BmgmProductShowMoreViewBinding.bind(view)
    }

    fun bind(product: BMGMWidgetUiModel.Product) {
        binding.bmgmProductImage.loadImage(product.imageUrl)

        if (product.loadMoreText.isNotBlank()) {
            overlayBinding.root.show()
            overlayBinding.bmgmShowMoreText.text = product.loadMoreText
        } else if (binding.bmgmProductShowMoreStub.isInflated()) {
            overlayBinding.root.hide()
        }
    }

    companion object {

        fun create(parent: ViewGroup): BMGMProductItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = BmgmProductItemBinding.inflate(inflater)
            return BMGMProductItemViewHolder(view)
        }
    }
}
