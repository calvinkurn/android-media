package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeInfoBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.TagAdapter
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder.TagViewHolder.TagListener
import com.tokopedia.tokopedianow.recipedetail.presentation.decoration.RecipeInfoTagDecoration
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class RecipeInfoViewHolder(itemView: View): AbstractViewHolder<RecipeInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_info
    }

    private var binding: ItemTokopedianowRecipeInfoBinding? by viewBinding()

    override fun bind(recipe: RecipeInfoUiModel) {
        renderTitle(recipe)
        renderBadgeIcon()
        renderPersonCount(recipe)
        renderDuration(recipe)
        renderTags(recipe)
    }

    private fun renderTitle(recipe: RecipeInfoUiModel) {
        binding?.textTitle?.text = recipe.title
    }

    private fun renderBadgeIcon() {
        binding?.textNowLabel?.setDrawableLeft(R.drawable.tokopedianow_ic_badge)
    }

    private fun renderPersonCount(recipe: RecipeInfoUiModel) {
        binding?.textPersonCount?.apply {
            text = itemView.context.resources
                .getString(R.string.tokopedianow_recipe_portion, recipe.portion)
            setDrawableLeft(R.drawable.tokopedianow_ic_user)
        }
    }

    private fun renderDuration(recipe: RecipeInfoUiModel) {
        binding?.textDuration?.apply {
            text = itemView.context.resources
                .getString(R.string.tokopedianow_recipe_duration, recipe.duration)
            setDrawableLeft(R.drawable.tokopedianow_ic_clock)
        }
    }

    private fun renderTags(recipe: RecipeInfoUiModel) {
        val tagAdapter = TagAdapter(recipe.tags)
        binding?.rvTags?.apply {
            layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(RecipeInfoTagDecoration())
            adapter = tagAdapter
        }
    }

    private fun Typography.setDrawableLeft(@DrawableRes drawableRes: Int) {
        val icon = ContextCompat.getDrawable(itemView.context, drawableRes)
        setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null)
        compoundDrawablePadding = itemView.context.resources
            .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
    }
}