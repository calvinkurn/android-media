package com.tokopedia.review.feature.createreputation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.presentation.adapter.diffutil.ReviewBadRatingCategoriesDiffUtilCallback
import com.tokopedia.review.feature.createreputation.presentation.listener.RecyclerViewItemRemoverListener
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewBadRatingCategoryListener
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.ReviewBadRatingCategoryViewHolder

class ReviewBadRatingCategoriesAdapter(private val badRatingCategoryListener: ReviewBadRatingCategoryListener) :
    RecyclerView.Adapter<ReviewBadRatingCategoryViewHolder>(),
    RecyclerViewItemRemoverListener {

    private var badRatingCategories: List<BadRatingCategory> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewBadRatingCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review_bad_rating_category, parent, false)
        return ReviewBadRatingCategoryViewHolder(
            itemView = view,
            badRatingCategoryListener = badRatingCategoryListener
        )
    }

    override fun getItemCount(): Int {
        return badRatingCategories.size
    }

    override fun onBindViewHolder(holder: ReviewBadRatingCategoryViewHolder, position: Int) {
        holder.bind(badRatingCategories[position])
    }

    override fun onItemClicked(adapterPosition: Int) {
        // No Op
    }

    fun setData(badRatingCategories: List<BadRatingCategory>) {
        val diffUtilCallback = ReviewBadRatingCategoriesDiffUtilCallback(
            this.badRatingCategories.toMutableList(),
            badRatingCategories
        )
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        this.badRatingCategories = badRatingCategories
        result.dispatchUpdatesTo(this)
    }
}