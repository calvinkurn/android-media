package com.tokopedia.catalog.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.CatalogProductListEmptyStateBinding
import com.tokopedia.catalog.ui.model.CatalogProductListEmptyModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding

class EmptyStateViewHolder(itemView: View, val listener: EmptyStateFilterListener): AbstractViewHolder<CatalogProductListEmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.catalog_product_list_empty_state
    }

    private val binding by viewBinding<CatalogProductListEmptyStateBinding>()

    override fun bind(data: CatalogProductListEmptyModel) {
        binding?.title?.text = data.title
        binding?.image?.let { iv ->
            ImageHandler.LoadImage(iv, data.urlRes)
        }
        binding?.desc?.text = data.description
        binding?.btnResetFilter?.setOnClickListener {
            listener.resetFilter()
        }
        binding?.btnResetFilter?.showWithCondition(data.isFromFilter)
    }
}

interface EmptyStateFilterListener{
    fun resetFilter()
}


