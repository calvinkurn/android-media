package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeBookmarkBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.TagAdapter
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RecipeViewHolder(
    itemView: View,
    private val listener: RecipeListener
): AbstractViewHolder<RecipeUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_bookmark
    }

    private var binding: ItemTokopedianowRecipeBookmarkBinding? by viewBinding()

    override fun bind(element: RecipeUiModel) {
        binding?.apply {
            tpTitle.text = element.title
            tpSubtitle.text = root.resources.getString(R.string.tokopedianow_recipe_bookmark_item_subtitle, element.duration, element.portion)
            iuRecipePicture.loadImage(element.picture)

            icuBookmark.setOnClickListener {
                listener.onRemoveBookmark(element.id)
            }

            rvTags.run {
                adapter = TagAdapter(element.tags)
                layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, true)
            }
        }
    }

    interface RecipeListener {
        fun onRemoveBookmark(recipeId: String)
    }
}