package com.tokopedia.review.feature.createreputation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.RecyclerViewItemRemoverListener
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewTemplateListener
import com.tokopedia.review.feature.createreputation.presentation.viewholder.ReviewTemplatesViewHolder

class ReviewTemplatesAdapter(private val reviewTemplateListener: ReviewTemplateListener) : RecyclerView.Adapter<ReviewTemplatesViewHolder>(), RecyclerViewItemRemoverListener {

    private var reviewTemplates: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTemplatesViewHolder {
        return ReviewTemplatesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_review_template, parent, false))
    }

    override fun getItemCount(): Int {
        return reviewTemplates.size
    }

    override fun onBindViewHolder(holder: ReviewTemplatesViewHolder, position: Int) {
        holder.bind(reviewTemplates[position], reviewTemplateListener, this)
    }

    override fun onItemClicked(adapterPosition: Int) {
        reviewTemplates.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun setData(reviewTemplates: List<String>) {
        this.reviewTemplates = reviewTemplates.toMutableList()
        notifyDataSetChanged()
    }

    fun getTemplates(): List<String> {
        return reviewTemplates
    }

}