package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.TagAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RecipeViewHolder(
    itemView: View,
    private val listener: RecipeItemListener
) : AbstractViewHolder<RecipeUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe
    }

    private var binding: ItemTokopedianowRecipeBinding? by viewBinding()

    private val context by lazy { itemView.context }

    override fun bind(recipe: RecipeUiModel) {
        renderImage(recipe)
        renderTitle(recipe)
        renderLabel(recipe)
        renderPortion(recipe)
        renderDuration(recipe)
        renderBookmark(recipe)
        setClickListener(recipe)
    }

    private fun renderImage(recipe: RecipeUiModel) {
        binding?.imageThumbnail?.loadImage(recipe.thumbnail)
    }

    private fun renderTitle(recipe: RecipeUiModel) {
        binding?.apply {
            textTitle.text = recipe.title
        }
    }

    private fun renderLabel(recipe: RecipeUiModel) {
        val tagAdapter = TagAdapter(recipe.labels)
        binding?.rvTags?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding?.rvTags?.adapter = tagAdapter
    }

    private fun renderPortion(recipe: RecipeUiModel) {
        binding?.textPersonCount?.apply {
            text = itemView.context.resources
                .getString(R.string.tokopedianow_recipe_portion, recipe.portion)
            setDrawableLeft(R.drawable.tokopedianow_ic_user)
        }
    }

    private fun renderDuration(recipe: RecipeUiModel) {
        binding?.textDuration?.apply {
            text = itemView.context.resources
                .getString(R.string.tokopedianow_recipe_duration, recipe.duration)
            setDrawableLeft(R.drawable.tokopedianow_ic_clock)
        }
    }

    private fun renderBookmark(recipe: RecipeUiModel) {
        binding?.imageBookmark?.apply {
            val iconResId = if (recipe.isBookmarked) {
                com.tokopedia.iconunify.R.drawable.iconunify_bookmark_filled
            } else {
                com.tokopedia.iconunify.R.drawable.iconunify_bookmark
            }
            setImageDrawable(ContextCompat.getDrawable(context, iconResId))
        }
    }

    private fun TextView.setDrawableLeft(@DrawableRes drawableRes: Int) {
        val icon = ContextCompat.getDrawable(itemView.context, drawableRes)
        icon?.setTint(
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
        setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null)
        compoundDrawablePadding = itemView.context.resources
            .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
    }

    private fun setClickListener(recipe: RecipeUiModel) {
        binding?.root?.setOnClickListener {
            listener.onClickItem(recipe)
        }
    }

    interface RecipeItemListener {
        fun onClickItem(recipe: RecipeUiModel)
    }
}