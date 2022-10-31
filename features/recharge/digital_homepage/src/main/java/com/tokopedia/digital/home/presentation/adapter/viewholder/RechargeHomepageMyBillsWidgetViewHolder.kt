package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import android.widget.TextView
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
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.media.loader.loadImage


/**
 * created by @bayazidnasir on 20/10/2022
 */

class RechargeHomepageMyBillsWidgetViewHolder(
    itemView: View
): AbstractViewHolder<RechargeHomepageMyBillsWidgetModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_my_bills

        private const val ITEMS_MARGIN = 10
    }

    override fun bind(element: RechargeHomepageMyBillsWidgetModel) {
        val binding = ViewRechargeHomeMyBillsBinding.bind(itemView)

        binding.tvTitle.text = element.section.title

        initItems(binding, element.section)
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
            addItemDecoration(RechargeItemSpaceDecorator(ITEMS_MARGIN.dpToPx(displayMetrics)))
        }
    }

    class RechargeHomepageMyBillsItemViewHolder(
        itemView: View
    ): AbstractViewHolder<RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_my_bills_item
        }

        override fun bind(element: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel) {
            val binding = ViewRechargeHomeMyBillsItemBinding.bind(itemView)

            binding.ivProductIcon.loadImage(element.item.attributes.iconUrl)
            binding.tvMainPrice.text = element.item.label2
            binding.tvMainPrice.setTextColor(element.item.attributes.titleColor)

            renderProductInfo(binding, element.item)
            renderExpiredInfo(binding, element.item)
        }

        private fun renderProductInfo(
            binding: ViewRechargeHomeMyBillsItemBinding,
            element: RechargeHomepageSections.Item
        ){
            with(binding){
                tvProductDetailName.text = element.title
                tvProductDetailNumber.text = element.content
            }
        }

        private fun renderExpiredInfo(
            binding: ViewRechargeHomeMyBillsItemBinding,
            element: RechargeHomepageSections.Item
        ) {
            binding.tvExpired.text = element.attributes.specialInfoText
            binding.tvExpired.setTextColor(element.attributes.specialInfoColor)
        }

        private fun TextView.setTextColor(color: String) {
            try {
                setTextColor(Color.parseColor(color))
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    class RechargeHomepageMyBillsLastItemViewHolder(
        itemView: View
    ): AbstractViewHolder<RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.content_recharge_homepage_my_bills_last_item
        }

        override fun bind(element: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel) {
            val binding = ContentRechargeHomepageMyBillsLastItemBinding.bind(itemView)
            with(binding){
                tvTitle.text = element.items.title
                containerCta.setOnClickListener {
                    RouteManager.route(itemView.context, element.items.applink)
                }
            }
        }
    }
}
