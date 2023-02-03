package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowEmptyStateRecipeListBinding
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeEmptyStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RecipeEmptyStateViewHolder(
    itemView: View,
    private val listener: RecipeEmptyStateListener? = null
): AbstractViewHolder<RecipeEmptyStateUiModel>(itemView) {

    companion object {
        private const val NO_DATA_IMAGE = TokopediaImageUrl.NO_DATA_IMAGE

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_empty_state_recipe_list
    }

    private var binding: ItemTokopedianowEmptyStateRecipeListBinding? by viewBinding()

    override fun bind(element: RecipeEmptyStateUiModel) {
        binding?.apply {
            root.addOnImpressionListener(element, object : ViewHintListener {
                override fun onViewHint() {
                    listener?.onImpressEmptyStatePage()
                }
            })

            iuPicture.loadImage(NO_DATA_IMAGE)

            if (element.isFilterSelected) {
                tpTitle.text = root.context.getString(R.string.tokopedianow_recipe_search_filter_empty_state_title)
                tpDescription.text = root.context.getString(R.string.tokopedianow_recipe_search_filter_empty_state_description)
            } else {
                tpTitle.text = root.context.getString(R.string.tokopedianow_recipe_search_empty_state_title)
                tpDescription.text = root.context.getString(R.string.tokopedianow_recipe_search_empty_state_description, element.title)
            }
            
            ubCta.setOnClickListener {
                listener?.onClickResetFilter()
            }
        }
    }

    interface RecipeEmptyStateListener {
        fun onClickResetFilter()
        fun onImpressEmptyStatePage()
    }

}