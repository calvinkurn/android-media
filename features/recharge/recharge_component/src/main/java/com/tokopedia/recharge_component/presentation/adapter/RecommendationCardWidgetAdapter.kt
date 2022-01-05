package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardBigBinding
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardSingleBinding
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardSmallBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card.RecommendationCardBigViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card.RecommendationCardSingleViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card.RecommendationCardSmallViewHolder

class RecommendationCardWidgetAdapter(private val recommendationListener: RechargeRecommendationCardListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listRecommendationProduct = emptyList<RecommendationCardWidgetModel>()

    override fun getItemCount(): Int = listRecommendationProduct.size

    override fun getItemViewType(position: Int): Int {
        return listRecommendationProduct.get(position).layoutType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (listRecommendationProduct.get(position).layoutType.ordinal) {
            RecommendationCardEnum.BIG.ordinal -> {
                (holder as RecommendationCardBigViewHolder).bind(listRecommendationProduct[position])
            }

            RecommendationCardEnum.SINGLE.ordinal -> {
                (holder as RecommendationCardSingleViewHolder).bind(listRecommendationProduct[position])
            }

            RecommendationCardEnum.SMALL.ordinal -> {
                (holder as RecommendationCardSmallViewHolder).bind(listRecommendationProduct[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecommendationCardEnum.BIG.ordinal -> {
                val binding = ViewRechargeRecommendationCardBigBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardBigViewHolder(recommendationListener, binding)
            }

            RecommendationCardEnum.SINGLE.ordinal -> {
                val binding = ViewRechargeRecommendationCardSingleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardSingleViewHolder(recommendationListener, binding)
            }

            RecommendationCardEnum.SMALL.ordinal -> {
                val binding = ViewRechargeRecommendationCardSmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardSmallViewHolder(recommendationListener, binding)
            }

            else -> {
                val binding = ViewRechargeRecommendationCardSmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardSmallViewHolder(recommendationListener, binding)
            }
        }
    }

    fun setRecommendationList(listRecommendationProduct: List<RecommendationCardWidgetModel>){
        this.listRecommendationProduct = listRecommendationProduct
    }
}