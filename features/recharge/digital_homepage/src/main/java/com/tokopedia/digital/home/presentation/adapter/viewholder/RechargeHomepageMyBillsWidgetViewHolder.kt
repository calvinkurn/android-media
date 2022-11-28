package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ContentRechargeHomepageMyBillsLastItemBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsWidgetModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageMyBillsAdapterTypeFactory
import com.tokopedia.digital.home.presentation.adapter.decoration.RechargeItemSpaceDecorator
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * created by @bayazidnasir on 20/10/2022
 */

class RechargeHomepageMyBillsWidgetViewHolder(
    itemView: View,
    val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageMyBillsWidgetModel>(itemView) {

    var itemDecoration: RechargeItemSpaceDecorator? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_my_bills

        private const val ITEMS_MARGIN = 8
    }

    override fun bind(element: RechargeHomepageMyBillsWidgetModel) {
        val binding = ViewRechargeHomeMyBillsBinding.bind(itemView)

        if (element.section.items.isNotEmpty()) {
            with(binding) {
                shimmerMyBills.root.gone()
                container.visible()
                tvTitle.text = element.section.title

                initItems(this, element.section)
            }
        } else {
            binding.shimmerMyBills.root.visible()
            binding.container.gone()
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun initItems(
        binding: ViewRechargeHomeMyBillsBinding,
        section: RechargeHomepageSections.Section
    ) {
        val itemList = mutableListOf<Visitable<RechargeHomepageMyBillsAdapterTypeFactory>>()
        val newItems = section.items.map { RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel(it) }

        itemList.addAll(newItems.subList(Int.ZERO, newItems.size - Int.ONE))
        itemList.add(RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel(section.items.last()))

        val displayMetrics = itemView.context.resources.displayMetrics

        with(binding.rvBillsBanner) {
            adapter = BaseAdapter(
                RechargeHomepageMyBillsAdapterTypeFactory(),
                itemList.toList()
            )
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            if (itemDecoration == null) {
                itemDecoration = RechargeItemSpaceDecorator(ITEMS_MARGIN.dpToPx(displayMetrics))
            }
            itemDecoration?.let {
                removeItemDecoration(it)
                addItemDecoration(it)
            }
        }
    }

    class RechargeHomepageMyBillsItemViewHolder(
        itemView: View
    ) : AbstractViewHolder<RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_my_bills_item

            private const val LABEL_TEAL = "Teal"
            private const val LABEL_ORANGE = "Orange"
            private const val LABEL_RED = "Red"
        }

        override fun bind(element: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel) {
            val binding = ViewRechargeHomeMyBillsItemBinding.bind(itemView)

            with(binding) {
                ivProductIcon.loadImage(element.item.attributes.iconUrl)
                tvMainPrice.text = element.item.label2
                tvMainPrice.setTextColor(element.item.attributes.titleColor)

                renderProductInfo(this, element.item)
                renderExpiredInfo(this, element.item)

                itemView.setOnClickListener {
                    RouteManager.route(itemView.context, element.item.applink)
                }
            }
        }

        private fun renderProductInfo(
            binding: ViewRechargeHomeMyBillsItemBinding,
            element: RechargeHomepageSections.Item
        ) {
            with(binding) {
                tvProductDetailName.text = element.subtitle
                tvProductDetailCategory.text = element.title
                tvProductDetailNumber.text = element.content
            }
        }

        private fun renderExpiredInfo(
            binding: ViewRechargeHomeMyBillsItemBinding,
            element: RechargeHomepageSections.Item
        ) {
            binding.tvExpired.text = element.attributes.specialInfoText
            binding.tvExpired.setLabelColor(element.attributes.specialInfoColor)
        }

        private fun Label.setLabelColor(style: String) {
            val type = when (style) {
                LABEL_TEAL -> Label.GENERAL_TEAL
                LABEL_ORANGE -> Label.GENERAL_ORANGE
                LABEL_RED -> Label.GENERAL_RED
                else -> Label.GENERAL_TEAL
            }
            setLabelType(type)
        }

        private fun Typography.setTextColor(color: String) {
            try {
                setTextColor(Color.parseColor(color))
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    class RechargeHomepageMyBillsLastItemViewHolder(
        itemView: View
    ) : AbstractViewHolder<RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.content_recharge_homepage_my_bills_last_item
        }

        override fun bind(element: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel) {
            val binding = ContentRechargeHomepageMyBillsLastItemBinding.bind(itemView)
            with(binding) {
                imgBackground.loadImage(element.items.mediaUrl)
                tvTitle.text = element.items.content
                containerCta.setOnClickListener {
                    RouteManager.route(itemView.context, element.items.applink)
                }
            }
        }
    }
}
