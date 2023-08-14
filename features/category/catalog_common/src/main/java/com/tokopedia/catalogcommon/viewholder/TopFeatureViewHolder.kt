package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel

class TopFeatureViewHolder(itemView: View) : AbstractViewHolder<TopFeaturesUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = 0 //TODO: replace with layout res
    }
    override fun bind(element: TopFeaturesUiModel) {

    }
}
