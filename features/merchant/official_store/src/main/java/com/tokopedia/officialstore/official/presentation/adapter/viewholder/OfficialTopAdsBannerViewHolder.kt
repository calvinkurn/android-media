package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.databinding.ViewmodelOfficialTopadsBannerBinding
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsBannerDataModel
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.utils.view.binding.viewBinding

class OfficialTopAdsBannerViewHolder(view: View) : AbstractViewHolder<OfficialTopAdsBannerDataModel>(view) {

    private var binding: ViewmodelOfficialTopadsBannerBinding? by viewBinding()

    override fun bind(element: OfficialTopAdsBannerDataModel) {
        // TDN implementation
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_topads_banner
    }

}
