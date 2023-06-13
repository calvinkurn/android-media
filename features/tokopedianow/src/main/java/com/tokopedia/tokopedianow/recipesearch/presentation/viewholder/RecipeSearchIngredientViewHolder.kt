package com.tokopedia.tokopedianow.recipesearch.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeSearchIngredientBinding
import com.tokopedia.tokopedianow.recipesearch.presentation.uimodel.RecipeSearchIngredientUiModel

class RecipeSearchIngredientViewHolder(
    private val binding: ItemTokopedianowRecipeSearchIngredientBinding,
    private val listener: RecipeSearchIngredientListener? = null
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val ALL_CORNER_SIZES = 20f
    }

    fun bind(model: RecipeSearchIngredientUiModel) {
        binding.apply {
            tpTitle.text = model.title
            cbOption.isChecked = model.isChecked
            sivPic.loadImage(model.imgUrl)
            sivPic.shapeAppearanceModel = sivPic.shapeAppearanceModel
                .toBuilder()
                .setAllCornerSizes(ALL_CORNER_SIZES)
                .build()
            root.setOnClickListener {
                listener?.onSelectIngredient(!model.isChecked, model.id, model.title)
            }
            cbOption.setOnClickListener {
                cbOption.isChecked = model.isChecked
                listener?.onSelectIngredient(!model.isChecked, model.id, model.title)
            }
        }
    }

    interface RecipeSearchIngredientListener {
        fun onSelectIngredient(isChecked: Boolean, id: String, title: String)
    }

}