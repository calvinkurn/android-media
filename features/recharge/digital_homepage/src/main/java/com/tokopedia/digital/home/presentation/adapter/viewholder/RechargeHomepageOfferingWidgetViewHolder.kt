package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeOfferingWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageOfferingWidgetModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.customview.DigitalHorizontalProductCard
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RechargeHomepageOfferingWidgetViewHolder(
    view: View,
    val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageOfferingWidgetModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_offering_widget

        private const val BACKGROUND_CORNER_RADIUS = 8f
    }

    override fun bind(element: RechargeHomepageOfferingWidgetModel) {
        val binding = ViewRechargeHomeOfferingWidgetBinding.bind(itemView)

        if (element.section.items.isNotEmpty()) {

            setupBackground(binding, element.section.label1, element.section.label2)
            setupTitle(binding, element.section.title)
            setupSubtitle(binding, element.section.subtitle)
            setupOtherButton(binding, element.section)
            setupProduct(binding, element.section)

            binding.root.addOnImpressionListener(element.section) {
                listener.onRechargeSectionItemImpression(element.section)
            }
        } else {
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setupBackground(
        binding: ViewRechargeHomeOfferingWidgetBinding,
        backgroundColor1: String,
        backgroundColor2: String
    ) {
        try {
            val gradient = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    Color.parseColor(backgroundColor1),
                    Color.parseColor(backgroundColor2)
                )
            )
            gradient.cornerRadius = BACKGROUND_CORNER_RADIUS

            binding.containerRechargeHomeOffering.background = gradient
        } catch (e: Throwable) {
            binding.containerRechargeHomeOffering.setBackgroundColor(
                MethodChecker.getColor(
                    binding.containerRechargeHomeOffering.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }


    private fun setupTitle(binding: ViewRechargeHomeOfferingWidgetBinding, title: String) {
        with(binding.tgRechargeHomeOfferingTitle) {
            if (title.isNotEmpty()) {
                text = title
                show()
            } else {
                hide()
            }
        }
    }

    private fun setupSubtitle(binding: ViewRechargeHomeOfferingWidgetBinding, subtitle: String) {
        with(binding.tgRechargeHomeOfferingSubtitle) {
            if (subtitle.isNotEmpty()) {
                text = subtitle
                show()
            } else {
                hide()
            }
        }
    }

    private fun setupOtherButton(
        binding: ViewRechargeHomeOfferingWidgetBinding,
        section: RechargeHomepageSections.Section,
    ) {
        with(binding.btnRechargeHomeOfferingOther) {
            if (section.applink.isNotEmpty()) {
                if (section.textLink.isNotEmpty()) {
                    text = section.textLink
                }
                setOnClickListener {
                    listener.onRechargeBannerAllItemClicked(section)
                }
                show()
            } else {
                hide()
            }
        }
    }

    private fun setupProduct(
        binding: ViewRechargeHomeOfferingWidgetBinding,
        section: RechargeHomepageSections.Section,
    ) {
        with(binding.rechargeHomeDigitalHorizontalProductCard) {
            if (section.items.isNotEmpty()) {
                val item = section.items[0]
                productCategory = item.title
                productDetail = item.subtitle
                productPrice = item.label2
                productSlashPrice = item.label1
                imageUrl = item.mediaUrl
                actionListener = object: DigitalHorizontalProductCard.ActionListener {
                    override fun onClick() {
                        listener.onRechargeSectionItemClicked(item)
                    }
                }

                buildView()
                show()
            } else {
                hide()
            }
        }
    }
}