package com.tokopedia.digital.widget.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.digital.R
import com.tokopedia.digital.widget.view.model.Recommendation

/**
 * Created by Rizky on 21/11/18.
 */
class RecommendationAdapter(val recommendations: List<Recommendation>, val listener: OnClickListener):
        RecyclerView.Adapter<RecommendationAdapter.RecommendationItemViewHolder>() {

    interface OnClickListener {
        fun onClickRecommendation(recommendation: Recommendation, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)

        return RecommendationItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recommendations.size
    }

    override fun onBindViewHolder(holder: RecommendationItemViewHolder, position: Int) {
        holder.bind(recommendations[position])
    }

    inner class RecommendationItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val iconOperator: ImageView = itemView.findViewById(R.id.icon_operator)
        private val textClientNumber: TextView = itemView.findViewById(R.id.text_client_number)
        private val textProductName: TextView = itemView.findViewById(R.id.text_product_name)

        private lateinit var recommendation: Recommendation

        init {
            itemView.setOnClickListener {
                listener.onClickRecommendation(recommendation, adapterPosition)
            }
        }

        fun bind(recommendation: Recommendation) {
            this.recommendation = recommendation
            ImageHandler.loadImageWithoutPlaceholder(iconOperator, recommendation.iconUrl,
                    ContextCompat.getDrawable(itemView.context, R.drawable.status_no_result)
            )
            textClientNumber.text = recommendation.clientNumber
            textProductName.text = recommendation.title
        }

    }

}