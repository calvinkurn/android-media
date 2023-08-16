package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessItemKeywordListBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class ListInspirationKeywordItemViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val isNeedAdjustImageRation: Boolean,
) : AbstractViewHolder<InspirationKeywordDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_semless_item_keyword_list
        private const val RATIO_IMAGE = "H,2:1"
    }

    private var binding: SearchInspirationSemlessItemKeywordListBinding? by viewBinding()

    override fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView
    ) {
        val binding = binding ?: return
        binding.ivSearchKeyword.adjustRatioImage()
        binding.ivSearchKeyword.loadImage(inspirationKeywordDataView.imageKeyword)
        binding.textViewKeyword.text = inspirationKeywordDataView.keyword
        binding.root.setOnClickListener {
            inspirationKeywordListener.onInspirationKeywordItemClicked(inspirationKeywordDataView)
        }
    }

    private fun ImageUnify.adjustRatioImage() {
        if (isNeedAdjustImageRation) {
            val constrainProductLayout = binding?.containerCardViewOptionsKeywordCard ?: return
            val imageProduct = this.id
            val set = ConstraintSet()
            set.clone(constrainProductLayout)
            set.setDimensionRatio(imageProduct, RATIO_IMAGE)
            set.applyTo(constrainProductLayout)
        }
    }
}
