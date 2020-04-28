package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.RatingAndTopicDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel

/**
 * Created by Yehezkiel on 28/04/20
 */
class ProductRatingFilterAdapter(val view: View, val listener: SellerRatingAndTopicListener) : RecyclerView.Adapter<RatingAndTopicDetailViewHolder>() {

    val listOfRatingData: MutableList<RatingBarUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingAndTopicDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RatingAndTopicDetailViewHolder.LAYOUT, parent, false)
        return RatingAndTopicDetailViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return listOfRatingData.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: RatingAndTopicDetailViewHolder, position: Int) {
        holder.bind(listOfRatingData[position])
    }
}