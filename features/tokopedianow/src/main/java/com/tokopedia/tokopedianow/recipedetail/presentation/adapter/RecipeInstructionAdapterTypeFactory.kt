package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.IngredientViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.InstructionViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.SectionTitleViewHolder

class RecipeInstructionAdapterTypeFactory : BaseAdapterTypeFactory(), RecipeInstructionTypeFactory {

    override fun type(uiModel: SectionTitleUiModel) = SectionTitleViewHolder.LAYOUT

    override fun type(uiModel: IngredientUiModel) = IngredientViewHolder.LAYOUT

    override fun type(uiModel: InstructionUiModel) = InstructionViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(parent)
            IngredientViewHolder.LAYOUT -> IngredientViewHolder(parent)
            InstructionViewHolder.LAYOUT -> InstructionViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}