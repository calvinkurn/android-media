package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderSeparatorBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class SeparatorViewHolder(itemView: View,
                          mainNavListener: MainNavListener
): AbstractViewHolder<SeparatorDataModel>(itemView) {

    private var binding: HolderSeparatorBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_separator
        private const val MARGIN_HORIZONTAL_CONTROL = 8
        private const val MARGIN_VERTICAL_CONTROL = 0
        private const val MARGIN_HORIZONTAL_MEPAGE = 0
        private const val MARGIN_VERTICAL_MEPAGE = 4
    }

    @SuppressLint("ResourcePackage")
    override fun bind(element: SeparatorDataModel) {
        val context = itemView.context
        val dividerLayoutParams = binding?.dividerUnify?.layoutParams as? MarginLayoutParams
        if(element.isMePageVariant){
            dividerLayoutParams?.height = context.resources.getDimensionPixelSize(com.tokopedia.homenav.R.dimen.nav_mepage_divider_height)
            dividerLayoutParams?.setMargins(
                MARGIN_HORIZONTAL_MEPAGE.toPx(),
                MARGIN_VERTICAL_MEPAGE.toPx(),
                MARGIN_HORIZONTAL_MEPAGE.toPx(),
                MARGIN_VERTICAL_MEPAGE.toPx()
            )
        } else {
            dividerLayoutParams?.height = context.resources.getDimensionPixelSize(com.tokopedia.homenav.R.dimen.nav_control_divider_height)
            dividerLayoutParams?.setMargins(
                MARGIN_HORIZONTAL_CONTROL.toPx(),
                MARGIN_VERTICAL_CONTROL.toPx(),
                MARGIN_HORIZONTAL_CONTROL.toPx(),
                MARGIN_VERTICAL_CONTROL.toPx()
            )
        }
        binding?.dividerUnify?.layoutParams = dividerLayoutParams
    }
}

