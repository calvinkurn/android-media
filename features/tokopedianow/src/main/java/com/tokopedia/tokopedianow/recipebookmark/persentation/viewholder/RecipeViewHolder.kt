package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeEmptyStateBinding
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeBookmarkBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RecipeViewHolder(itemView: View): AbstractViewHolder<RecipeUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_bookmark
    }

    private var binding: ItemTokopedianowRecipeBookmarkBinding? by viewBinding()

    override fun bind(element: RecipeUiModel) {
        binding?.apply {
            tpTitle.text = element.title
            tpSubtitle.text = "${element.duration} Menit | ${element.portion} Porsi"
            iuRecipePicture.loadImage(element.picture)

        }
    }
}