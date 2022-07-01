package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeInstructionBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.utils.view.binding.viewBinding

class InstructionViewHolder(itemView: View) : AbstractViewHolder<InstructionUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_instruction
    }

    private var binding: ItemTokopedianowRecipeInstructionBinding? by viewBinding()

    override fun bind(instruction: InstructionUiModel) {
        binding?.textInstruction?.text = MethodChecker.fromHtml(instruction.htmlText)
    }
}