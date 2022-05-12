package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardCustomLastItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomLastItemModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageCustomLastItemAdapterTypeFactory
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel

class RechargeHomepageProductCardCustomLastItemViewHolder(
    val binding: ViewRechargeHomeProductCardCustomLastItemBinding,
    val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageProductCardCustomLastItemModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_product_card_custom_last_item
    }

    private lateinit var section: RechargeHomepageSections.Section

    override fun bind(element: RechargeHomepageProductCardCustomLastItemModel) {
        section = element.section
        if (section.items.isNotEmpty()) {
            hideLoading()
            setupInitialView(element)
            setupList(element)
        } else {
            showLoading()
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setupInitialView(element: RechargeHomepageProductCardCustomLastItemModel) {
        with(binding) {
            // handle background color
            try {
                containerRechargeCustomLastItem.setBackgroundColor(Color.parseColor(element.section.label2))
            } catch (t: Throwable) {
                t.printStackTrace()
            }

            // title
            tvRechargeCustomLastItemTitle.text = element.section.title

            // subtitle
            tvRechargeCustomLastItemSubtitle.text = element.section.subtitle
        }
    }

    private fun setupList(element: RechargeHomepageProductCardCustomLastItemModel) {
        with(binding) {
            val layoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)

            val newList: MutableList<Visitable<DigitalUnifyCardTypeFactory>> = mutableListOf()
            newList.addAll(element.digitalUnifyItems.subList(0, element.digitalUnifyItems.size - 1))
            newList.add(
                RechargeHomepageProductCardCustomLastItemModel.LastItem(element.section) as Visitable<DigitalUnifyCardTypeFactory>
            )

            rvRechargeCustomLastItem.layoutManager = layoutManager
            rvRechargeCustomLastItem.adapter = BaseAdapter(
                RechargeHomepageCustomLastItemAdapterTypeFactory(
                    listener,
                    digitalUnifyCardListener
                ),
                newList.toList()
            )
        }
    }

    private fun showLoading() {
        with(binding) {
            tvRechargeCustomLastItemTitle.hide()
            tvRechargeCustomLastItemSubtitle.hide()
            rvRechargeCustomLastItem.hide()
            viewRechargeHomeProductCardsShimmering.root.visible()
        }
    }

    private fun hideLoading() {
        with(binding) {
            tvRechargeCustomLastItemTitle.visible()
            tvRechargeCustomLastItemSubtitle.visible()
            rvRechargeCustomLastItem.visible()
            viewRechargeHomeProductCardsShimmering.root.hide()
        }
    }

    private val digitalUnifyCardListener =
        object : DigitalUnifyCardViewHolder.DigitalUnifyCardListener {
            override fun onItemClicked(item: DigitalUnifyModel, index: Int) {
                if (section.items.size > index)
                    listener.onRechargeSectionItemClicked(section.items[index])
            }

            override fun onItemImpression(item: DigitalUnifyModel, index: Int) {

            }
        }
}