package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.RatingAndTopicDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel

/**
 * Created by Yehezkiel on 28/04/20
 */
class ProductRatingFilterAdapter(val view: View, val listener: SellerRatingAndTopicListener) : RecyclerView.Adapter<RatingAndTopicDetailViewHolder>() {

    private val listOfRatingData: MutableList<RatingBarUiModel> = mutableListOf()

    fun setData(newOptionList: List<RatingBarUiModel>) {

        val callback = ProductRatingDiffUtil(listOfRatingData, newOptionList)
        val diffResult = DiffUtil.calculateDiff(callback)

        diffResult.dispatchUpdatesTo(this)
        listOfRatingData.clear()
        listOfRatingData.addAll(newOptionList)
    }

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

internal class ProductRatingDiffUtil(val oldList:List<RatingBarUiModel>, val newList:List<RatingBarUiModel>):DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].ratingLabel == newList[newItemPosition].ratingLabel
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].ratingCount == newList[newItemPosition].ratingCount && oldList[oldItemPosition].ratingIsChecked == newList[newItemPosition].ratingIsChecked
    }

}