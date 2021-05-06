package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView

class BannerViewHolder(itemView: View): AbstractViewHolder<BannerDataView>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = 0
    }

    override fun bind(element: BannerDataView?) {

    }

}
