package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import com.tokopedia.tokomember_seller_dashboard.model.ColorTemplateListItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashCardBgVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashCardColorVh

class TokomemberDashCardColorBgAdapter(var cardColorList: ArrayList<CardTemplateImageListItem>) :
    RecyclerView.Adapter<TokomemberDashCardBgVh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokomemberDashCardBgVh {
        return TokomemberDashCardBgVh(
            View.inflate(
                parent.context,
                R.layout.tm_dash_colorbg_item,
                null
            )
        )
    }

    override fun onBindViewHolder(holder: TokomemberDashCardBgVh, position: Int) {
        holder.bind(cardColorList[position])
    }

    override fun getItemCount() = cardColorList.size

}