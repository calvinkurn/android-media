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
    itemView: View,
    private val listener: HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener? = null
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
            setLoadState(element)
        }
    }

    private fun hideShimmering() {
        binding?.container?.setBackgroundResource(R.drawable.tokopedianow_bg_quest_title)
        binding?.questTitleWidgetShimmering?.root?.hide()
    }

    private fun showShimmering() {
        binding?.apply {
            binding?.container?.setBackgroundColor(ContextCompat.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_Background))
            questTitleWidgetShimmering.root.show()
            questTitleWidgetErrorState.root.hide()
            questTitleWidget.hide()
        }
    }

    private fun setLoadState(element: HomeQuestTitleUiModel) {
        binding?.apply {
            questTitleWidgetErrorState.root.hide()
            questTitleWidget.show()
            iuGift.setImage(newIconId = IconUnify.GIFT)
            iuGift.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White), BlendModeCompat.SRC_ATOP)
            tpCounter.text = String.format("${element.currentQuestFinished}/${element.totalQuestTarget}")
        }
    }

    private fun setErrorState() {
        binding?.apply {
            questTitleWidget.hide()
            questTitleWidgetErrorState.let {
                it.root.show()
                it.sivRefresh.setOnClickListener {
                    listener?.onClickRefreshQuestWidget()
                    showShimmering()
                }
            }
        }
    }
}