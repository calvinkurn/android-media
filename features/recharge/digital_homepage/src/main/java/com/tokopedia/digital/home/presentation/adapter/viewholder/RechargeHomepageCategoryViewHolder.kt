package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageCategoryModel
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_category.view.*

/**
 * @author by resakemal on 05/06/20.
 */

class RechargeHomepageCategoryViewHolder(itemView: View, val listener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageCategoryModel>(itemView) {

    override fun bind(element: RechargeHomepageCategoryModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                view_recharge_home_category_shimmering.hide()

                val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
                rv_recharge_home_category.layoutManager = layoutManager
                rv_recharge_home_category.adapter = RechargeItemCategoryAdapter(section.items, listener)
                tv_recharge_home_category_title.text = section.title
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                view_recharge_home_category_shimmering.show()
                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}