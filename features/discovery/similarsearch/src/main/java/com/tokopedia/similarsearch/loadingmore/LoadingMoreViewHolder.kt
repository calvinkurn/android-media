package com.tokopedia.similarsearch.loadingmore

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.similarsearch.abstraction.BaseViewHolder

internal class LoadingMoreViewHolder(itemView: View): BaseViewHolder<LoadingMoreModel>(itemView) {

    companion object {
        val LAYOUT = com.tokopedia.abstraction.R.layout.loading_layout
    }

    override fun bind(item: LoadingMoreModel) {

    }
}