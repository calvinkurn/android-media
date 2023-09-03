package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderSeparatorBinding
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.homenav.R as homenavR

class SeparatorViewHolder(itemView: View,
                          mainNavListener: MainNavListener
): AbstractViewHolder<SeparatorDataModel>(itemView) {

    private var binding: HolderSeparatorBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_separator
        private const val CONTROL_MARGIN_HORIZONTAL = 8
        private const val CONTROL_MARGIN_VERTICAL = 0
        private const val MEPAGE_MARGIN_HORIZONTAL = 0
        private const val MEPAGE_MARGIN_VERTICAL = 4
        private const val MEPAGE_MARGIN_TOP_PROFILE = 8
    }

    @SuppressLint("ResourcePackage")
    override fun bind(element: SeparatorDataModel) {
        val context = itemView.context
        val dividerLayoutParams = binding?.dividerUnify?.layoutParams as? MarginLayoutParams
        if(element.isMePageVariant){
            val marginTop = if(element.sectionId == MainNavConst.Section.PROFILE) MEPAGE_MARGIN_TOP_PROFILE else MEPAGE_MARGIN_VERTICAL
            dividerLayoutParams?.height = context.resources.getDimensionPixelSize(homenavR.dimen.nav_mepage_divider_height)
            dividerLayoutParams?.setMargins(
                MEPAGE_MARGIN_HORIZONTAL.toPx(),
                marginTop.toPx(),
                MEPAGE_MARGIN_HORIZONTAL.toPx(),
                MEPAGE_MARGIN_VERTICAL.toPx()
            )
        } else {
            dividerLayoutParams?.height = context.resources.getDimensionPixelSize(homenavR.dimen.nav_control_divider_height)
            dividerLayoutParams?.setMargins(
                CONTROL_MARGIN_HORIZONTAL.toPx(),
                CONTROL_MARGIN_VERTICAL.toPx(),
                CONTROL_MARGIN_HORIZONTAL.toPx(),
                CONTROL_MARGIN_VERTICAL.toPx()
            )
        }
        binding?.dividerUnify?.layoutParams = dividerLayoutParams
    }
}

