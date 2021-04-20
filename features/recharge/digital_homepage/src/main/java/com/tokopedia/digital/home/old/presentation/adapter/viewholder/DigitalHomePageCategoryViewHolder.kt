package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemCategoryAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_category.view.*

class DigitalHomePageCategoryViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageCategoryModel?) {
        val layoutManager = LinearLayoutManager(itemView.context)
        itemView.rv_digital_homepage_category.layoutManager = layoutManager
        if (element?.isLoaded == true) {
            itemView.view_recharge_home_category_shimmering.hide()
            itemView.rv_digital_homepage_category.show()
            itemView.rv_digital_homepage_category.adapter = DigitalItemCategoryAdapter(element.listSubtitle, onItemBindListener)
        } else {
            itemView.view_recharge_home_category_shimmering.show()
            itemView.rv_digital_homepage_category.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}