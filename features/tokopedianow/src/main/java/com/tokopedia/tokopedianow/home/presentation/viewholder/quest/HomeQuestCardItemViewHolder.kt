package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel

class HomeQuestCardItemViewHolder(
    private val binding: ItemTokopedianowQuestCardBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(element: HomeQuestCardItemUiModel) {
        binding.apply {
            tpTitle.text = element.title
            tpDescription.text = element.description
            iuIcon.showWithCondition(element.isLockedShown)
        }
    }
}
