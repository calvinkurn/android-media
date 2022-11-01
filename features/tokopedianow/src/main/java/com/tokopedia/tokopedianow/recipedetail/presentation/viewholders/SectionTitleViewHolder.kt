package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeSectionTitleBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SectionTitleViewHolder(itemView: View) : AbstractViewHolder<SectionTitleUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_section_title
    }

    private var binding: ItemTokopedianowRecipeSectionTitleBinding? by viewBinding()

    override fun bind(section: SectionTitleUiModel) {
        binding?.textTitle?.text = itemView.context.getString(section.resId)
    }
}