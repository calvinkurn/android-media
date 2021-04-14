package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.ItemLevelScoreProjectUiModel

class ItemLevelScoreProjectViewHolder(view: View): AbstractViewHolder<ItemLevelScoreProjectUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_level_skor_projection
    }

    override fun bind(element: ItemLevelScoreProjectUiModel?) {}
}