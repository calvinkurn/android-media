package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalItemCategoryViewHolder
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel

class DigitalItemCategoryAdapter(
    val listSubtitle: List<DigitalHomePageCategoryModel.Subtitle>?,
    val onItemBindListener: OnItemBindListener
) : RecyclerView.Adapter<DigitalItemCategoryViewHolder>() {

    override fun getItemCount(): Int {
        return listSubtitle?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: DigitalItemCategoryViewHolder, position: Int) {
        viewHolder.bind(listSubtitle?.get(position), onItemBindListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): DigitalItemCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_digital_home_category_item, parent, false)
        return DigitalItemCategoryViewHolder(view)
    }

}