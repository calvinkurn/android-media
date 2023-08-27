package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderSeparatorBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel
import com.tokopedia.utils.view.binding.viewBinding

class SeparatorViewHolder(itemView: View,
                          mainNavListener: MainNavListener
): AbstractViewHolder<SeparatorDataModel>(itemView) {

    private var binding: HolderSeparatorBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_separator
    }

    @SuppressLint("ResourcePackage")
    override fun bind(element: SeparatorDataModel) {
        val context = itemView.context
        val dividerLayoutParams = binding?.dividerUnify?.layoutParams
        val heightDimenId = if(element.isMePageVariant){
            com.tokopedia.homenav.R.dimen.nav_control_divider_height
        } else {
            com.tokopedia.homenav.R.dimen.nav_mepage_divider_height
        }
        dividerLayoutParams?.height = context.resources.getDimensionPixelSize(heightDimenId)
        binding?.dividerUnify?.layoutParams = dividerLayoutParams
    }
}

