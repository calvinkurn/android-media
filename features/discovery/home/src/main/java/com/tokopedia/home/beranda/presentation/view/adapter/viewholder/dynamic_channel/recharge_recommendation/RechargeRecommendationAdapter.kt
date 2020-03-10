package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.recharge_recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase.Companion.PARAM_CONTENT_ID
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase.Companion.PARAM_UUID
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.RechargeRecommendationViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.home_recharge_recommendation_item.view.*

class RechargeRecommendationAdapter(var items: RechargeRecommendation,
                                    val listener: RechargeRecommendationViewHolder.RechargeRecommendationListener)
    : RecyclerView.Adapter<RechargeRecommendationAdapter.RechargeRecommendationItemViewHolder>() {

    override fun onBindViewHolder(viewHolder: RechargeRecommendationItemViewHolder, position: Int) {
        viewHolder.bind(items.recommendations[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeRecommendationItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_recharge_recommendation_item, parent, false)
        return RechargeRecommendationItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.recommendations.size
    }

    private fun removeRechargeRecommendationItem(position: Int) {
        val newRechargeRecommendations = items.recommendations.toMutableList()
        newRechargeRecommendations.removeAt(position)
        items = items.copy( recommendations = newRechargeRecommendations )
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)

        // Remove widget if there are no more data
        if (itemCount == 0) listener.removeRechargeRecommendation()
    }

    inner class RechargeRecommendationItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeRecommendationData) {
//            itemView.spotlight_image.loadImage(element.mediaUrl)
//            itemView.spotlight_name.text = element.title
//            itemView.setOnClickListener {
//                listener.onContentClickListener(element.applink)
//            }
//
//            itemView.ic_close_review.setOnClickListener {
//                val requestParams = mapOf(PARAM_UUID to items.UUID, PARAM_CONTENT_ID to element.contentID)
//                listener.onDismissClickListener(requestParams)
//
//                removeRechargeRecommendationItem(adapterPosition)
//            }
        }

    }
}