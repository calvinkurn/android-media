package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageRecommendationModel
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageRecommendationAdapter(private var list: List<TravelHomepageRecommendationModel.Item>,
                                          var listener: RecommendationViewHolder.OnItemClickListener?):
        RecyclerView.Adapter<TravelHomepageRecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RecommendationViewHolder.LAYOUT, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(list.get(position), position, listener)
    }


    class RecommendationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(recommendation: TravelHomepageRecommendationModel.Item, position: Int, listener: OnItemClickListener?) {
            with(itemView) {
                image.loadImage(recommendation.imageUrl)
                title.text = recommendation.title
                subtitle.text = recommendation.subtitle
                prefix.text = recommendation.prefix
            }
            if (listener != null) itemView.setOnClickListener { listener.onItemClick(recommendation, position) }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_travel_section_list_item
        }

        interface OnItemClickListener {
            fun onItemClick(category: TravelHomepageRecommendationModel.Item, position: Int)
        }
    }
}