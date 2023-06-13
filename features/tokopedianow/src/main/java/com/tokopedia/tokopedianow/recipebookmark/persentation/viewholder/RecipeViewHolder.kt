package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
            setTitleSubtitle(
                binding = this,
                element = element
            )

            iuRecipePicture.setImageUrl(
                url = element.picture
            )

            setBookmarkIcon(
                binding = this,
                element = element
            )

            setTagList(
                binding = this,
                element = element
            )

            root.setOnClickListener {
                listener.onClickRecipeCard(
                    appUrl = element.appUrl,
                    recipeId = element.id,
                    recipeTitle = element.title,
                    position = layoutPosition
                )
            }

            root.addOnImpressionListener(element, object : ViewHintListener {
                override fun onViewHint() {
                    listener.onImpressRecipeCard(
                        recipeId = element.id,
                        recipeTitle = element.title,
                        position = layoutPosition
                    )
                }
            })
        }
    }

    private fun setTitleSubtitle(binding: ItemTokopedianowRecipeBookmarkBinding, element: RecipeUiModel) {
        binding.apply {
            tpTitle.text = element.title

            if (element.duration == null) {
                tpSubtitle.text = root.resources.getString(R.string.tokopedianow_recipe_bookmark_item_subtitle_without_duration, element.portion)
            } else {
                tpSubtitle.text = root.resources.getString(R.string.tokopedianow_recipe_bookmark_item_subtitle, element.duration, element.portion)
            }
        }
    }

    private fun setTagList(binding: ItemTokopedianowRecipeBookmarkBinding, element: RecipeUiModel) {
        binding.rvTags.run {
            adapter = TagAdapter(element.tags.orEmpty())
            layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, true)
        }
    }

    private fun setBookmarkIcon(binding: ItemTokopedianowRecipeBookmarkBinding, element: RecipeUiModel) {
        binding.icuBookmark.setOnClickListener {
            listener.onRemoveBookmark(
                recipeTitle = element.title,
                position = layoutPosition,
                recipeId = element.id
            )
        }
    }

    interface RecipeListener {
        fun onRemoveBookmark(recipeTitle: String, position: Int, recipeId: String)
        fun onClickRecipeCard(appUrl: String, recipeId: String, recipeTitle: String, position: Int)
        fun onImpressRecipeCard(recipeId: String, recipeTitle: String, position: Int)
    }
}