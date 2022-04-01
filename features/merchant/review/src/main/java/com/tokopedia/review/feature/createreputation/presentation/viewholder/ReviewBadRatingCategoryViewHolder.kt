package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewBadRatingCategoryListener
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewBadRatingCategoryViewHolder(
    itemView: View,
    private val impressHolder: ImpressHolder = ImpressHolder()
) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        badRatingCategory: BadRatingCategory,
        badRatingCategoryListener: ReviewBadRatingCategoryListener
    ) {
        with(badRatingCategory) {
            itemView.apply {
                addOnImpressionListener(impressHolder) {
                    badRatingCategoryListener.onImpressBadRatingCategory(description)
                }
                findViewById<Typography>(R.id.review_bad_rating_category_title)?.text = description
                val checkBox = findViewById<CheckboxUnify>(R.id.review_bad_rating_category_checkbox)
                checkBox.apply {
                    setOnCheckedChangeListener { compoundButton, b ->
                        badRatingCategoryListener.onBadRatingCategoryClicked(description, checkBox.isChecked, this@with.id, shouldRequestFocus)
                    }
                }
                setOnClickListener {
                    checkBox.isChecked = !checkBox.isChecked
                }
                setBackgroundResource(R.drawable.bg_review_highlighted_topic)
            }
        }
    }

}