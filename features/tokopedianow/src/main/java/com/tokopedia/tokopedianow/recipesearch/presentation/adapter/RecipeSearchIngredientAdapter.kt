package com.tokopedia.tokopedianow.recipesearch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeSearchIngredientBinding
import com.tokopedia.tokopedianow.recipesearch.presentation.uimodel.RecipeSearchIngredientUiModel
import com.tokopedia.tokopedianow.recipesearch.presentation.viewholder.RecipeSearchIngredientViewHolder

class RecipeSearchIngredientAdapter(
    private val listener: RecipeSearchIngredientViewHolder.RecipeSearchIngredientListener? = null
): ListAdapter<RecipeSearchIngredientUiModel, RecipeSearchIngredientViewHolder>(
    object : DiffUtil.ItemCallback<RecipeSearchIngredientUiModel>() {
        override fun areItemsTheSame(
            oldItem: RecipeSearchIngredientUiModel,
            newItem: RecipeSearchIngredientUiModel
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RecipeSearchIngredientUiModel,
            newItem: RecipeSearchIngredientUiModel
        ): Boolean = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeSearchIngredientViewHolder = RecipeSearchIngredientViewHolder(
        binding = ItemTokopedianowRecipeSearchIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ),
        listener = listener
    )

    override fun onBindViewHolder(holder: RecipeSearchIngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}