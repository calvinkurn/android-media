package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.DetailLoadingStateUiModel

class DetailLoadingStateViewHolder(itemView: View?) : AbstractViewHolder<DetailLoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_detail_loading_state
    }

    override fun bind(element: DetailLoadingStateUiModel?) {}
}