package com.tokopedia.digital.home.presentation.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home_category.view.*

class DigitalHomePageCategoryViewHolder(itemView : View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageCategoryModel?) {
        val layoutManager = LinearLayoutManager(itemView.context)
        itemView?.category_recycler_view.layoutManager = layoutManager
        if(element?.isLoaded?:false){
            itemView?.category_recycler_view.adapter = DigitalItemCategoryAdapter(element?.listSubtitle, onItemBindListener)
        }else{
            onItemBindListener.onCategoryItemDigitalBind()
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_digital_home_category
        val CATEGORY_SPAN_COUNT = 5
    }
}