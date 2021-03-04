package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingDataModel


class OfficialLoadingContentViewHolder(itemView: View) : AbstractViewHolder<OfficialLoadingDataModel>(itemView) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.viewmodel_official_loading_content
    }

    override fun bind(element: OfficialLoadingDataModel) {}
}