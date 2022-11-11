package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsEntryPointModel
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsEntrypointBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage

/**
 * created by @bayazidnasir on 7/11/2022
 */

class RechargeHomepageMyBillsEntryPointWidgetViewHolder(
    itemView: View
): AbstractViewHolder<RechargeHomepageMyBillsEntryPointModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_my_bills_entrypoint
    }

    override fun bind(element: RechargeHomepageMyBillsEntryPointModel) {
        val binding = ViewRechargeHomeMyBillsEntrypointBinding.bind(itemView)

        if (element.section.items.isNotEmpty()) {
            binding.containerContent.visible()
            binding.containerShimmer.gone()
            val sectionItem = element.section.items.first()
            with(binding) {
                tvTitle.text = sectionItem.title
                tvSubtitle.text = sectionItem.subtitle
                labelWidget.text = sectionItem.label1
                ivProductIcon.loadImage(sectionItem.mediaUrl)
            }
        } else {
            binding.containerContent.gone()
            binding.containerShimmer.visible()
        }
    }
}
