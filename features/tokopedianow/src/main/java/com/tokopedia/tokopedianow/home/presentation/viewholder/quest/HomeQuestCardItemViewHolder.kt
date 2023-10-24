package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel

class HomeQuestCardItemViewHolder(
    private val binding: ItemTokopedianowQuestCardBinding
): RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val QUEST_CARD_DISPLAYED_PERCENTAGE = 0.95f
    }

    init {
        binding.root.initQuestCardWidth()
    }

    private fun View.initQuestCardWidth() {
        val padding = getDpFromDimen(
            context = itemView.context,
            id = R.dimen.tokopedianow_quest_card_horizontal_padding
        )
        val width = ((getScreenWidth() - (padding + padding)) * QUEST_CARD_DISPLAYED_PERCENTAGE)
        layoutParams.width = width.toIntSafely()
    }

    private fun ItemTokopedianowQuestCardBinding.adjustTitleStartPadding() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(layoutQuestCard)
        constraintSet.setMargin(
            tpTitle.id,
            ConstraintSet.START,
            getDpFromDimen(
                context = itemView.context,
                id = R.dimen.tokopedianow_quest_card_title_start_padding
            ).toIntSafely()
        )
        constraintSet.applyTo(layoutQuestCard)
    }

    fun bind(element: HomeQuestCardItemUiModel) {
        binding.apply {
            tpTitle.text = element.title
            tpDescription.text = element.description
            aivIcon.showIfWithBlock(element.isLockedShown) {
                setBackgroundResource(R.drawable.tokopedianow_bg_quest_locked)
                adjustTitleStartPadding()
            }
        }
    }
}
