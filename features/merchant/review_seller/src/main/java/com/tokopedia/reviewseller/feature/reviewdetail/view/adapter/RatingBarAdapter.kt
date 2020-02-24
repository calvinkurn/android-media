package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.ItemRatingBarViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ItemRatingBarModel

class RatingBarAdapter(var list: MutableList<ItemRatingBarModel>, var context: Context?): RecyclerView.Adapter<ItemRatingBarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRatingBarViewHolder {
        return ItemRatingBarViewHolder(LayoutInflater.from(context).inflate(ItemRatingBarViewHolder.LAYOUT, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemRatingBarViewHolder, position: Int) {
        holder.bind(list[position])
    }
}