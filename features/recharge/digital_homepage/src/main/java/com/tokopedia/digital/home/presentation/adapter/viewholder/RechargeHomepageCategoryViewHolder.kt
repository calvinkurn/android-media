package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeCarousellBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeCategoryBinding
import com.tokopedia.digital.home.model.RechargeHomepageCategoryModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by resakemal on 05/06/20.
 */

class RechargeHomepageCategoryViewHolder(itemView: View, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageCategoryModel>(itemView) {

    override fun bind(element: RechargeHomepageCategoryModel) {

        val bind = ViewRechargeHomeCategoryBinding.bind(itemView)
        val section = element.section
        with(bind) {
            if (section.items.isNotEmpty()) {

                viewRechargeHomeCategoryShimmering.hide()

                val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
                rvRechargeHomeCategory.layoutManager = layoutManager
                rvRechargeHomeCategory.adapter = RechargeItemCategoryAdapter(section.items, listener)
                tvRechargeHomeCategoryTitle.text = section.title
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                viewRechargeHomeCategoryShimmering.show()
                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}