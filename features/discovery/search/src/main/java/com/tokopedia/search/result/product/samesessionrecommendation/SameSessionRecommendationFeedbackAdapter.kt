package com.tokopedia.search.result.product.samesessionrecommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback.FeedbackItem

class SameSessionRecommendationFeedbackAdapter(
    private val sameSessionRecommendationListener: SameSessionRecommendationListener,
    private val onFeedbackItemClicked: (Feedback, FeedbackItem) -> Unit,
    diffCallback: DiffUtil.ItemCallback<FeedbackItem> = DEFAULT_DIFF_CALLBACK
) : ListAdapter<FeedbackItem, SameSessionRecommendationFeedbackItemViewHolder>(diffCallback) {
    private var feedback: Feedback? = null

    fun setFeedback(feedback: Feedback) {
        this.feedback = feedback
    }

    override fun onViewRecycled(holder: SameSessionRecommendationFeedbackItemViewHolder) {
        feedback = null
        super.onViewRecycled(holder)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SameSessionRecommendationFeedbackItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(
            SameSessionRecommendationFeedbackItemViewHolder.LAYOUT,
            parent,
            false
        )
        return SameSessionRecommendationFeedbackItemViewHolder(
            sameSessionRecommendationListener,
            view,
        ).apply {
            itemView.setOnClickListener { handleFeedbackItemClick() }
        }
    }

    private fun SameSessionRecommendationFeedbackItemViewHolder.handleFeedbackItemClick() {
        val feedback = feedback ?: return
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            onFeedbackItemClicked(feedback, getItem(bindingAdapterPosition))
        }
    }

    override fun onBindViewHolder(
        holder: SameSessionRecommendationFeedbackItemViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedbackItem>() {
            override fun areItemsTheSame(
                oldItem: FeedbackItem,
                newItem: FeedbackItem
            ): Boolean {
                return oldItem.componentId == newItem.componentId
            }

            override fun areContentsTheSame(
                oldItem: FeedbackItem,
                newItem: FeedbackItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
