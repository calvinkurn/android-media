package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ContentRechargeHomepageLastItemBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsWidgetModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageMyBillsAdapterTypeFactory
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
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

        itemList.addAll(newItems)
        itemList.add(RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel(section.items.last()))

        with(binding.rvBillsBanner) {
            adapter = BaseAdapter(
                RechargeHomepageMyBillsAdapterTypeFactory(),
                itemList.toList()
            )
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
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

            binding.ivProductIcon.loadImage(element.item.mediaUrl)
            binding.tvMainPrice.text = element.item.attributes.pricePrefix

            renderProductInfo(binding, element.item)
            renderExpiredInfo(binding, element.item)
        }

        private fun renderProductInfo(
            binding: ViewRechargeHomeMyBillsItemBinding,
            element: RechargeHomepageSections.Item
        ){
            with(binding){
                tvProductDetailName.text = element.title
                tvProductDetailNumber.text = element.subtitle

                try {
                    tvProductDetailName.setTextColor(
                        Color.parseColor(element.attributes.titleColor)
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

                try {
                    tvProductDetailNumber.setTextColor(
                        Color.parseColor(element.attributes.subtitleColor)
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }

        private fun renderExpiredInfo(
            binding: ViewRechargeHomeMyBillsItemBinding,
            element: RechargeHomepageSections.Item
        ) {
            binding.tvExpired.text = element.attributes.specialInfoText
            try {
                binding.tvExpired.setTextColor(Color.parseColor(element.attributes.specialInfoColor))
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
            val LAYOUT = R.layout.content_recharge_homepage_last_item
        }

        override fun bind(element: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel) {
            val binding = ContentRechargeHomepageLastItemBinding.bind(itemView)
            TODO("not implemented yet")

        }
    }
}
