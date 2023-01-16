package com.tokopedia.digital.home.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsEntrypointBinding
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsEntryPointModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage

/**
 * created by @bayazidnasir on 7/11/2022
 */

class RechargeHomepageMyBillsEntryPointWidgetViewHolder(
    val binding: ViewRechargeHomeMyBillsEntrypointBinding,
    val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageMyBillsEntryPointModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_my_bills_entrypoint
    }

    override fun bind(element: RechargeHomepageMyBillsEntryPointModel) {
        if (element.section.items.isNotEmpty()) {
            binding.containerContent.visible()
            binding.containerShimmer.gone()
            val sectionItem = element.section.items.first()
            with(binding) {
                tvTitle.text = sectionItem.content
                tvSubtitle.text = sectionItem.attributes.soldValue
                labelWidget.shouldShowWithAction(sectionItem.subtitle.isNotEmpty()) {
                    labelWidget.text = sectionItem.subtitle
                }
                ivProductIcon.loadImage(sectionItem.mediaUrl)
            }

            itemView.setOnClickListener {
                RouteManager.route(it.context, sectionItem.applink)
            }
        } else {
            binding.containerContent.gone()
            binding.containerShimmer.visible()
            listener.loadRechargeSectionData(element.visitableId())
        }
    }
}
