package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R

class AllBrandLoadingRecomViewHolder(itemView: View): AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel) { }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_loading_brand_recommendation
    }

}