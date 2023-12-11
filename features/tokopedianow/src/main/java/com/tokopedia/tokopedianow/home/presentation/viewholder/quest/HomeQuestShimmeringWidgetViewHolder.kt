package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestShimmeringWidgetUiModel

class HomeQuestShimmeringWidgetViewHolder (
    itemView: View
): AbstractViewHolder<HomeQuestShimmeringWidgetUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_shimmering
    }

    override fun bind(element: HomeQuestShimmeringWidgetUiModel?) { /* do nothing */ }
}
