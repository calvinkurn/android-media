package com.tokopedia.product.detail.common.bmgm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel
import com.tokopedia.product.detail.common.databinding.BmgmProductItemBinding
import com.tokopedia.product.detail.common.databinding.BmgmProductShowMoreViewBinding

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMProductViewHolder(
    private val binding: BmgmProductItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val overlayBinding by lazyThreadSafetyNone {
        val view = binding.bmgmProductShowMoreStub.inflate()
        BmgmProductShowMoreViewBinding.bind(view)
    }

    fun bind(product: BMGMUiModel.Product) {
        binding.bmgmProductImage.loadImage(product.imageUrl)

        if (product.loadMoreText.isNotBlank()) {
            overlayBinding.root.show()
            overlayBinding.bmgmShowMoreText.text = product.loadMoreText
        } else if (binding.bmgmProductShowMoreStub.parent == null) {
            overlayBinding.root.hide()
        }
    }

    companion object {

        fun create(parent: ViewGroup): BMGMProductViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = BmgmProductItemBinding.inflate(inflater)
            return BMGMProductViewHolder(view)
        }
    }
}
