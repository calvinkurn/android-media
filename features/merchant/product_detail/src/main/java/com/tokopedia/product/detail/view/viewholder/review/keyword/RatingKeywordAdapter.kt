package com.tokopedia.product.detail.view.viewholder.review.keyword

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.view.viewholder.review.ReviewRatingUiModel
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by yovi.putra on 09/11/23"
 * Project name: android-tokopedia-core
 **/


class RatingKeywordAdapter :
    ListAdapter<ReviewRatingUiModel.Keyword, RatingKeywordAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<ReviewRatingUiModel.Keyword>() {
            override fun areItemsTheSame(
                oldItem: ReviewRatingUiModel.Keyword,
                newItem: ReviewRatingUiModel.Keyword
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ReviewRatingUiModel.Keyword,
                newItem: ReviewRatingUiModel.Keyword
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChipsUnify(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList.getOrNull(position) ?: return
        holder.bind(item)
    }

    class ViewHolder(
        private val chip: ChipsUnify
    ) : RecyclerView.ViewHolder(chip) {

        fun bind(keyword: ReviewRatingUiModel.Keyword) {
            chip.chipText = keyword.label
        }
    }
}
