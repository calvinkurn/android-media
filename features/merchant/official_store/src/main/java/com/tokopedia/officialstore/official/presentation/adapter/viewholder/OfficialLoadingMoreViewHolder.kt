package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import kotlinx.android.synthetic.main.viewmodel_official_loading_more.view.*


class OfficialLoadingMoreViewHolder(itemView: View) : AbstractViewHolder<OfficialLoadingMoreDataModel>(itemView) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.viewmodel_official_loading_more
    }

    override fun bind(element: OfficialLoadingMoreDataModel) {
        itemView.loader?.circular?.start()
    }
}