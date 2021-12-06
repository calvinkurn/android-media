package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestTitleWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestTitleViewHolder(
    itemView: View
): AbstractViewHolder<HomeQuestTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_title_widget
    }

    private var binding: ItemTokopedianowQuestTitleWidgetBinding? by viewBinding()

    override fun bind(element: HomeQuestTitleUiModel) {
        hideShimmering()
        if (element.isErrorState) {
            // not yet
        } else {
            binding?.iuGift?.setImage(newIconId = IconUnify.GIFT)
            binding?.tpCounter?.text = String.format("${element.currentQuestFinished}/${element.totalQuestTarget}")
        }
    }

    private fun hideShimmering() {
        binding?.questTitleWidgetShimmering?.root?.hide()
        showQuestWidget()
    }

    private fun showQuestWidget() {
        binding?.questTitleWidget?.show()
    }
}