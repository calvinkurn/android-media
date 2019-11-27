package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R


class OfficialLoadingBannerViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.viewmodel_official_loading
    }

    override fun bind(element: LoadingModel) { }

}