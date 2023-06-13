package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.TagAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class RecipeViewHolder(
    itemView: View,
    private val listener: RecipeItemListener
) : AbstractViewHolder<RecipeUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe

        private const val ICON_SIZE = 16
        private const val DEFAULT_BOUND = 0
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
        setImpressionListener(recipe)
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
        val tagAdapter = TagAdapter(recipe.tags)
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
            setDrawableLeft(com.tokopedia.iconunify.R.drawable.iconunify_user)
        }
    }

    private fun renderDuration(recipe: RecipeUiModel) {
        binding?.textDuration?.apply {
            text = itemView.context.resources
                .getString(R.string.tokopedianow_recipe_duration, recipe.duration)
            setDrawableLeft(com.tokopedia.iconunify.R.drawable.iconunify_clock)
        }
    }

    private fun renderBookmark(recipe: RecipeUiModel) {
        binding?.imageBookmark?.apply {
            changeIconBookmark(this, recipe.isBookmarked)
            setOnClickListener {
                listener.onClickBookmark(recipe, layoutPosition, !recipe.isBookmarked)
            }
        }
    }

    private fun changeIconBookmark(imageBookmark: ImageUnify, isBookmarked: Boolean) {
        val iconResId = if (isBookmarked) {
            com.tokopedia.iconunify.R.drawable.iconunify_bookmark_filled
        } else {
            com.tokopedia.iconunify.R.drawable.iconunify_bookmark
        }
        setIconBookmark(imageBookmark, iconResId)
    }

    private fun setIconBookmark(imageBookmark: ImageUnify, iconResId: Int) {
        val color = ContextCompat.getColor(context, R.color.tokopedianow_recipe_image_bookmark_dms_color)
        val icon = ContextCompat.getDrawable(context, iconResId)
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
        imageBookmark.setImageDrawable(icon)
    }

    private fun TextView.setDrawableLeft(@DrawableRes drawableRes: Int) {
        val color = ContextCompat.getColor(context, R.color.tokopedianow_recipe_white_dms_color)
        val icon = ContextCompat.getDrawable(context, drawableRes)
        icon?.setBounds(DEFAULT_BOUND, DEFAULT_BOUND, ICON_SIZE.toPx(), ICON_SIZE.toPx())
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
        setCompoundDrawables(icon, null, null, null)
        compoundDrawablePadding = itemView.context.resources
            .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
    }

    private fun setClickListener(recipe: RecipeUiModel) {
        binding?.root?.setOnClickListener {
            listener.onClickItem(recipe, layoutPosition)
        }
    }

    private fun setImpressionListener(recipe: RecipeUiModel) {
        binding?.root?.addOnImpressionListener(recipe, object : ViewHintListener {
            override fun onViewHint() {
                listener.onImpressItem(recipe, layoutPosition)
            }
        })
    }

    interface RecipeItemListener {
        fun onClickItem(recipe: RecipeUiModel, position: Int)
        fun onImpressItem(recipe: RecipeUiModel, position: Int)
        fun onClickBookmark(recipe: RecipeUiModel, position: Int, isBookmarked: Boolean)
    }
}
