package com.tokopedia.digital.home.old.presentation.adapter.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.presentation.adapter.viewholder.DigitalItemCategoryViewHolder
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener

class DigitalItemCategoryAdapter(val listSubtitle: List<DigitalHomePageCategoryModel.Subtitle>?, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemCategoryViewHolder>() {

    override fun getItemCount(): Int {
        return listSubtitle?.size?:0
    }

    override fun onBindViewHolder(viewHolder: DigitalItemCategoryViewHolder, position: Int) {
        viewHolder.bind(listSubtitle?.get(position), onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_category_item, parent, false)
        return DigitalItemCategoryViewHolder(view)
    }

}
