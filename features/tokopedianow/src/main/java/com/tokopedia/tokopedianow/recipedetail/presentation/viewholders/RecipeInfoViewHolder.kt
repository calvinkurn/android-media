package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeInfoBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class RecipeInfoViewHolder(itemView: View): AbstractViewHolder<RecipeInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_info

        private const val MAX_LABEL_COUNT = 3
    }

    private var binding: ItemTokopedianowRecipeInfoBinding? by viewBinding()

    override fun bind(recipe: RecipeInfoUiModel) {
        renderTitle(recipe)
        renderBadgeIcon()
        renderPersonCount(recipe)
        renderDuration(recipe)
        renderLabel(recipe)
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

    private fun renderLabel(recipe: RecipeInfoUiModel) {
        val viewIds = mutableListOf<Int>()
        val labelCount = recipe.labels.count()
        val labels = mutableListOf<String>()

        if(labelCount > MAX_LABEL_COUNT) {
            val otherLabelCount = labelCount - MAX_LABEL_COUNT
            val otherLabelText = itemView.context.resources.getString(
                R.string.tokopedianow_recipe_other_label, otherLabelCount)
            labels.addAll(recipe.labels.take(MAX_LABEL_COUNT))
            labels.add(otherLabelText)
        } else {
            labels.addAll(recipe.labels)
        }

        labels.forEachIndexed { index, label ->
            val viewId = if(index == 0) {
                binding?.labelRecipe?.text = label
                binding?.labelRecipe?.show()
                R.id.labelRecipe
            } else {
                val prevId = viewIds[index - 1]
                createRecipeLabel(prevId, label)
            }
            viewIds.add(viewId)
        }
    }

    private fun createRecipeLabel(prevViewId: Int, label: String): Int {
        val viewId = View.generateViewId()
        val margin = itemView.context.resources
            .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

        val labelView = Label(itemView.context).apply {
            setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            id = viewId
            text = label
        }
        binding?.root?.addView(labelView)

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding?.root)
        constraintSet.connect(
            viewId,
            ConstraintSet.START,
            prevViewId,
            ConstraintSet.END,
            margin
        )
        constraintSet.connect(
            viewId,
            ConstraintSet.TOP,
            prevViewId,
            ConstraintSet.TOP
        )
        constraintSet.applyTo(binding?.root)

        return viewId
    }

    private fun Typography.setDrawableLeft(@DrawableRes drawableRes: Int) {
        val icon = ContextCompat.getDrawable(itemView.context, drawableRes)
        setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null)
        compoundDrawablePadding = itemView.context.resources
            .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
    }
}