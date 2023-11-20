package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeCategoryBinding
import com.tokopedia.digital.home.model.RechargeHomepageCategoryModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by resakemal on 05/06/20.
 */

class RechargeHomepageCategoryViewHolder(
    itemView: View,
    val listener: RechargeHomepageItemListener
) :
    AbstractViewHolder<RechargeHomepageCategoryModel>(itemView) {

    var isExpanded: Boolean = true
    val adapter = RechargeItemCategoryAdapter(arrayListOf(), listener)

    override fun bind(element: RechargeHomepageCategoryModel) {

        val bind = ViewRechargeHomeCategoryBinding.bind(itemView)
        val section = element.section
        with(bind) {
            if (section.items.isNotEmpty()) {

                viewRechargeHomeCategoryShimmering.hide()

                val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
                rvRechargeHomeCategory.layoutManager = layoutManager
                setAdapterItems(section.items)
                rvRechargeHomeCategory.adapter = adapter

                if (section.title.isNotEmpty()) {
                    tvRechargeHomeCategoryTitle.text = section.title
                    tvRechargeHomeCategoryTitle.show()
                } else {
                    tvRechargeHomeCategoryTitle.hide()
                }

                setupChevron(bind, element)

                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                viewRechargeHomeCategoryShimmering.show()
                listener.loadRechargeSectionData(element.visitableId(), element.section.name)
            }
        }
    }

    private fun setupChevron(
        binding: ViewRechargeHomeCategoryBinding,
        element: RechargeHomepageCategoryModel
    ) {
        with(binding) {
            if (element.shouldShowChevron()) {
                icRechargeHomeCategoryChevron.show()
            } else {
                icRechargeHomeCategoryChevron.hide()
            }

            icRechargeHomeCategoryChevron.setOnClickListener {
                isExpanded = !isExpanded
                setAdapterItems(element.section.items)

                if (isExpanded) {
                    icRechargeHomeCategoryChevron.setImage(IconUnify.CHEVRON_UP)
                } else {
                    icRechargeHomeCategoryChevron.setImage(IconUnify.CHEVRON_DOWN)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterItems(items: List<RechargeHomepageSections.Item>) {
        if (isExpanded)
            adapter.items = items
        else
            adapter.items = arrayListOf()

        adapter.notifyDataSetChanged()
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}
