package com.tokopedia.search.result.product.samesessionrecommendation

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultSameSessionRecommendationFeedbackLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

class SameSessionRecommendationFeedbackItemViewHolder(
    private val sameSessionRecommendationListener: SameSessionRecommendationListener,
    view: View,
) : RecyclerView.ViewHolder(view) {
    private var binding: SearchResultSameSessionRecommendationFeedbackLayoutBinding? by viewBinding()

    fun bind(element: SameSessionRecommendationDataView.Feedback.FeedbackItem) {
        val binding = binding ?: return
        binding.imgFeedbackItem.loadImage(element.imageUrl)
        binding.tgFeedbackItemName.text = element.name
        itemView.addOnImpressionListener(element) {
            sameSessionRecommendationListener.onSameSessionRecommendationFeedbackItemImpressed(
                element
            )
        }
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_same_session_recommendation_feedback_layout
    }
}
