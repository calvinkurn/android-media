package com.tokopedia.review.feature.createreputation.presentation.viewholder.old

import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewBadRatingCategoryListener
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewBadRatingCategoryViewHolder(
    itemView: View,
    private val impressHolder: ImpressHolder = ImpressHolder(),
    private val badRatingCategoryListener: ReviewBadRatingCategoryListener
) : RecyclerView.ViewHolder(itemView) {

    private val checkBox: CheckboxUnify?
        get() = itemView.findViewById(R.id.review_bad_rating_category_checkbox)
    private val tvTitle: Typography?
        get() = itemView.findViewById(R.id.review_bad_rating_category_title)

    private val checkboxListener = CheckboxListener()

    private var badRatingCategory: BadRatingCategory? = null

    private fun setupCheckbox(badRatingCategory: BadRatingCategory) {
        checkBox?.setOnCheckedChangeListener(null)
        if (badRatingCategory.selected != checkBox?.isChecked) {
            checkBox?.isChecked = badRatingCategory.selected
            checkBox?.skipAnimation()
        }
        checkBox?.setOnCheckedChangeListener(checkboxListener)
    }

    fun bind(badRatingCategory: BadRatingCategory) {
        this.badRatingCategory = badRatingCategory
        with(badRatingCategory) {
            itemView.apply {
                addOnImpressionListener(impressHolder) {
                    badRatingCategoryListener.onImpressBadRatingCategory(description)
                }
                tvTitle?.text = description
                setupCheckbox(badRatingCategory)
                setOnClickListener {
                    checkBox?.isChecked = !checkBox?.isChecked.orFalse()
                }
                setBackgroundResource(R.drawable.bg_review_highlighted_topic)
            }
        }
    }

    private inner class CheckboxListener: CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            badRatingCategory?.run {
                badRatingCategoryListener.onBadRatingCategoryClicked(
                    description,
                    isChecked,
                    id,
                    shouldRequestFocus
                )
            }
        }
    }
}