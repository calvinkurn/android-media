package com.tokopedia.digital.home.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.presentation.adapter.adapter.DigitalItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_category.view.*

class DigitalHomePageCategoryViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageCategoryModel?) {
        val layoutManager = LinearLayoutManager(itemView.context)
        itemView?.category_recycler_view.layoutManager = layoutManager
        if (element?.isLoaded ?: false) {
            itemView?.categoryShimmering.hide()
            itemView?.category_recycler_view.show()
            itemView?.category_recycler_view.adapter = DigitalItemCategoryAdapter(element?.listSubtitle, onItemBindListener)
        } else {
            itemView?.categoryShimmering.show()
            itemView?.category_recycler_view.hide()
            onItemBindListener.onCategoryItemDigitalBind(element?.isLoadFromCloud)
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_category
        val CATEGORY_SPAN_COUNT = 5
    }
}