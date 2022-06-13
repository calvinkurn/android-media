package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.databinding.ViewmodelOfficialLoadingMoreBinding
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import com.tokopedia.utils.view.binding.viewBinding


class OfficialLoadingMoreViewHolder(itemView: View) : AbstractViewHolder<OfficialLoadingMoreDataModel>(itemView) {

    private var binding: ViewmodelOfficialLoadingMoreBinding? by viewBinding()
    companion object {
        @LayoutRes
        var LAYOUT = R.layout.viewmodel_official_loading_more
    }

    override fun bind(element: OfficialLoadingMoreDataModel) {
        binding?.loader?.circular?.start()
    }
}