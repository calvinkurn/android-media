package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.ColorTemplateListItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashCardColorVh

class TokomemberDashCardColorAdapter(var cardColorList: ArrayList<ColorTemplateListItem>) :
    RecyclerView.Adapter<TokomemberDashCardColorVh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokomemberDashCardColorVh {
        return TokomemberDashCardColorVh(
            View.inflate(
                parent.context,
                R.layout.tm_dash_color_item,
                null
            )
        )
    }

    override fun onBindViewHolder(holder: TokomemberDashCardColorVh, position: Int) {
        holder.bind(cardColorList[position])
    }

    override fun getItemCount() = cardColorList.size

}