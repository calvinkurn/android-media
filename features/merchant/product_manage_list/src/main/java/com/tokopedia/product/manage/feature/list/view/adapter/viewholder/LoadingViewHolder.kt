package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R

class LoadingViewHolder(itemView: View): AbstractViewHolder<LoadingModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_product_manage_list_shimmer
    }

    override fun bind(element: LoadingModel?) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
    }
}