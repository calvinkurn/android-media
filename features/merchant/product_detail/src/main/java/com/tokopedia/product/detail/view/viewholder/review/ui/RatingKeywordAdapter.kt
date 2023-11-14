package com.tokopedia.product.detail.view.viewholder.review.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.review.delegate.ReviewCallback
import com.tokopedia.product.detail.view.viewholder.review.event.OnTopicClicked
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by yovi.putra on 09/11/23"
 * Project name: android-tokopedia-core
 **/


class RatingKeywordAdapter(
    private val callback: ReviewCallback
) : ListAdapter<ReviewRatingUiModel.Keyword, RatingKeywordAdapter.ViewHolder>(DIFF_UTIL) {

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
        return ViewHolder(chip = ChipsUnify(parent.context), callback = callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList.getOrNull(position) ?: return
        holder.bind(item)
    }

    class ViewHolder(
        private val chip: ChipsUnify,
        private val callback: ReviewCallback
    ) : RecyclerView.ViewHolder(chip) {

        fun bind(keyword: ReviewRatingUiModel.Keyword) {
            setText(keyword = keyword)
            setListener(keyword = keyword)
        }

        private fun setText(keyword: ReviewRatingUiModel.Keyword) {
            chip.chipText = keyword.text
        }

        private fun setListener(keyword: ReviewRatingUiModel.Keyword) {
            chip.setOnClickListener {
                callback.onEvent(event = OnTopicClicked(topic = keyword.filter))
            }
        }
    }
}
