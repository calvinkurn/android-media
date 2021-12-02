package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.databinding.HolderHomeNavTitleBinding
import com.tokopedia.utils.view.binding.viewBinding

class HomeNavTitleViewHolder(itemView: View
): AbstractViewHolder<HomeNavTitleDataModel>(itemView) {
    private var binding: HolderHomeNavTitleBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_title
    }

    override fun bind(element: HomeNavTitleDataModel) {
        binding?.title?.text = element.title
    }
}