package com.tokopedia.digital.home.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.digital.home.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsTripleEntrypointBinding
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsTripleEntryPointsModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loaderfresco.loadImageFresco

class RechargeHomepageMyBillsTripleEntryPointWidgetViewHolder(
    val binding: ViewRechargeHomeMyBillsTripleEntrypointBinding,
    val listener: RechargeHomepageItemListener
): AbstractViewHolder<RechargeHomepageMyBillsTripleEntryPointsModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_my_bills_triple_entrypoint
        private const val SIZE = 3
        private const val SECOND = 1
        private const val THIRD = 2
    }

    override fun bind(element: RechargeHomepageMyBillsTripleEntryPointsModel) {
        if(element.isTripleEntryPointLoaded && element.section.items.isNotEmpty() && element.section.items.size == SIZE) {
            binding.containerContent.visible()
            binding.containerShimmer.gone()

            val leftSection = element.section.items.first()
            val rightSection = element.section.items[SECOND]
            val bottomSection = element.section.items[THIRD]

            showBottomSection(binding, bottomSection)
            showLeftSection(binding, leftSection)
            showRightSection(binding, rightSection)

            binding.root.addOnImpressionListener(element.section) {
                listener.onRechargeSectionItemImpression(element.section)
            }
        } else if(element.isTripleEntryPointLoaded && (element.section.items.isEmpty() || element.section.items.size < SIZE)) {
            binding.containerContent.gone()
            binding.containerShimmer.gone()
        } else if(!element.isTripleEntryPointLoaded && element.section.items.isEmpty()) {
            binding.containerContent.gone()
            binding.containerShimmer.visible()
            listener.loadRechargeSectionDataWithLoadedParam(element.visitableId(), isLoaded = true, element.section.name)
        }
    }

    private fun showBottomSection(binding: ViewRechargeHomeMyBillsTripleEntrypointBinding,
                                  bottomSection: RechargeHomepageSections.Item) {
        with(binding) {
            tvTitle.text = bottomSection.content
            tvSubtitle.text = bottomSection.attributes.soldValue
            ivProductIcon.loadImageFresco(bottomSection.mediaUrl)
            labelWidget.shouldShowWithAction(bottomSection.subtitle.isNotEmpty()) {
                labelWidget.text = bottomSection.subtitle
            }
            containerBottom.setOnClickListener {
                clickedSection(bottomSection)
            }
        }
    }

    private fun showLeftSection(binding: ViewRechargeHomeMyBillsTripleEntrypointBinding,
                                  leftSection: RechargeHomepageSections.Item) {
        with(binding) {
            tvLeftTitle.text = leftSection.content
            tvLeftSubtitle.text = leftSection.attributes.soldValue
            imgLeft.loadImageFresco(leftSection.mediaUrl)

            containerLeft.setOnClickListener {
                clickedSection(leftSection)
            }
        }
    }

    private fun showRightSection(binding: ViewRechargeHomeMyBillsTripleEntrypointBinding,
                                  rightSection: RechargeHomepageSections.Item) {
        with(binding) {
            tvRightTitle.text = rightSection.content
            tvRightSubtitle.text = rightSection.attributes.soldValue
            imgRight.loadImageFresco(rightSection.mediaUrl)

            containerRight.setOnClickListener {
                clickedSection(rightSection)
            }
        }
    }

    private fun clickedSection(section: RechargeHomepageSections.Item) {
        listener.onRechargeSectionItemClicked(section)
    }
}
