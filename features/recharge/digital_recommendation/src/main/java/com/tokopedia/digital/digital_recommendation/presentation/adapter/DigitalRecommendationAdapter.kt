package com.tokopedia.digital.digital_recommendation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.digital_recommendation.databinding.ItemDigitalRecommendationBinding
import com.tokopedia.digital.digital_recommendation.presentation.adapter.viewholder.DigitalRecommendationViewHolder
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemModel

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationAdapter(private val recommendationItems: List<DigitalRecommendationItemModel>,
                                   private val actionListener: DigitalRecommendationViewHolder.DigitalRecommendationItemActionListener
) : RecyclerView.Adapter<DigitalRecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalRecommendationViewHolder {
        val binding = ItemDigitalRecommendationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return DigitalRecommendationViewHolder(binding, actionListener)
    }

    override fun onBindViewHolder(holder: DigitalRecommendationViewHolder, position: Int) {
        holder.bind(recommendationItems[position])
    }

    override fun getItemCount(): Int = recommendationItems.size
}