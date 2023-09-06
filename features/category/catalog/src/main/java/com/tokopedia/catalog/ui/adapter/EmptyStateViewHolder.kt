package com.tokopedia.catalog.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.CatalogProductListEmptyStateBinding
import com.tokopedia.utils.view.binding.viewBinding

class EmptyStateViewHolder(itemView: View): AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.catalog_product_list_empty_state
    }

    private val binding by viewBinding<CatalogProductListEmptyStateBinding>()

    override fun bind(data: EmptyModel) {
        setupEmptyStateContainer(data)
        //binding?.title?.text = getString(data.contentRes)
        binding?.image?.let { iv ->
            ImageHandler.LoadImage(iv, data.urlRes)
        }
    }

    private fun setupEmptyStateContainer(data: EmptyModel) {
//        val layoutParams = if(data.contentRes == R.string.product_manage_list_empty_product) {
//            LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//        } else {
//            LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
//        }
//        binding?.container?.layoutParams = layoutParams
    }
}
