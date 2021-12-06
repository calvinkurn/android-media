package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
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
            setErrorState()
        } else {
            setCounter(element)
        }
    }

    private fun hideShimmering() {
        binding?.questTitleWidgetShimmering?.root?.hide()
    }

    private fun setCounter(element: HomeQuestTitleUiModel) {
        binding?.questTitleWidgetErrorState?.root?.hide()
        binding?.questTitleWidget?.show()
        binding?.iuGift?.setImage(newIconId = IconUnify.GIFT)
        binding?.tpCounter?.text = String.format("${element.currentQuestFinished}/${element.totalQuestTarget}")
    }

    private fun setErrorState() {
        binding?.questTitleWidgetErrorState?.root?.show()
        binding?.questTitleWidget?.hide()
        binding?.questTitleWidgetErrorState?.apply {
            val unifyColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_YN500)
            sivRefresh.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(unifyColor, BlendModeCompat.SRC_ATOP)
            sivRefresh.setOnClickListener {

            }
        }
    }
}