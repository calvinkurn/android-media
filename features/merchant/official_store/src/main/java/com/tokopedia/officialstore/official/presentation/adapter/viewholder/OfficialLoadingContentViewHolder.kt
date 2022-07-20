package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.databinding.ViewmodelOfficialLoadingContentBinding
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingDataModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.isRunningTest
import com.tokopedia.utils.view.binding.viewBinding

class OfficialLoadingContentViewHolder(itemView: View) : AbstractViewHolder<OfficialLoadingDataModel>(itemView) {

    private var binding: ViewmodelOfficialLoadingContentBinding? by viewBinding()

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.viewmodel_official_loading_content
    }

    override fun bind(element: OfficialLoadingDataModel) {
        if (!isRunningTest())
            binding?.containerLoadingContent?.visible()
    }
}