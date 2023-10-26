package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestReloadBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestReloadWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestReloadWidgetViewHolder(
    itemView: View,
    private val listener: HomeQuestReloadWidgetListener? = null
): AbstractViewHolder<HomeQuestReloadWidgetUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_reload
    }

    private val binding: ItemTokopedianowQuestReloadBinding? by viewBinding()

    override fun bind(element: HomeQuestReloadWidgetUiModel) {
        binding?.apply {
            root.setOnClickListener {
                listener?.onReloadListener()
                root.progressState = !root.progressState
            }
        }
    }

    interface HomeQuestReloadWidgetListener {
        fun onReloadListener()
    }
}
