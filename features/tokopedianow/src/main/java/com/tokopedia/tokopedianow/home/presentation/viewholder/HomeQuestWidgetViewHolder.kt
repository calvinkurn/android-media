package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestWidgetViewHolder(
    itemView: View
): AbstractViewHolder<HomeQuestWidgetUiModel>(itemView) {

    companion object {
        private const val PERCENT_MULTIPLIER = 100
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_widget
    }

    private var binding: ItemTokopedianowQuestWidgetBinding? by viewBinding()

    override fun bind(element: HomeQuestWidgetUiModel) {
        setCircularProgressBar(element.currentProgress, element.totalProgress)
        setTitle(element.title)
        setDesc(element.desc)
    }

    private fun setCircularProgressBar(currentProgress: Int, totalProgress: Int) {
        val result = (currentProgress.toFloat() / totalProgress) * PERCENT_MULTIPLIER
        binding?.circularProgressBar?.apply {
            setProgress(result)
            setProgressWidth(12F)
            setRounded(true)
            setProgressColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            setProgressBackgroundColor(ContextCompat.getColor(itemView.context, R.color.tokopedianow_circle_progressbar_background_dms_color))
        }
    }

    private fun setTitle(title: String) {
        binding?.title?.text = title
    }

    private fun setDesc(desc: String) {
        binding?.desc?.text = desc
    }
}