package com.tokopedia.tokopedianow.seeallcategory.persentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.seeallcategory.persentation.uimodel.SeeAllCategoryItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSeeAllCategoryBinding
import com.tokopedia.utils.view.binding.viewBinding

class SeeAllCategoryItemViewHolder(
    itemView: View,
    private val listener: SeeAllCategoryListener? = null
): AbstractViewHolder<SeeAllCategoryItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_see_all_category
    }

    private var binding: ItemTokopedianowSeeAllCategoryBinding? by viewBinding()

    override fun bind(data: SeeAllCategoryItemUiModel) {
        binding?.apply {
            tpCategoryTitle.text = data.name
            ivCategoryImage.loadImage(data.imageUrl)
            root.setOnClickListener {
                listener?.onCategoryClicked(data, layoutPosition)
            }
            root.addOnImpressionListener(data) {
                listener?.onCategoryImpressed(data, layoutPosition)
            }
        }
    }

    interface SeeAllCategoryListener {
        fun onCategoryClicked(data: SeeAllCategoryItemUiModel, position: Int)
        fun onCategoryImpressed(data: SeeAllCategoryItemUiModel, position: Int)
    }
}
