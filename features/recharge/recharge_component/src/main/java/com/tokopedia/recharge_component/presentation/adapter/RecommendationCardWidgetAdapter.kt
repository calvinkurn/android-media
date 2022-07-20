package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardBigBinding
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardSmallBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card.RecommendationCardBigViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card.RecommendationCardSmallViewHolder

class RecommendationCardWidgetAdapter(
    private val recommendationListener: RechargeRecommendationCardListener,
    private var recommendationTitle: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listRecommendationProduct = emptyList<RecommendationCardWidgetModel>()

    override fun getItemCount(): Int = listRecommendationProduct.size

    override fun getItemViewType(position: Int): Int {
        return listRecommendationProduct.get(position).layoutType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (listRecommendationProduct.get(position).layoutType.ordinal) {
            RecommendationCardEnum.BIG.ordinal -> {
                (holder as RecommendationCardBigViewHolder).bind(listRecommendationProduct[position], position)
            }

            RecommendationCardEnum.SMALL.ordinal -> {
                (holder as RecommendationCardSmallViewHolder).bind(listRecommendationProduct[position], position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder =  when (viewType) {
            RecommendationCardEnum.BIG.ordinal -> {
                val binding = ViewRechargeRecommendationCardBigBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardBigViewHolder(recommendationTitle, recommendationListener, binding)
            }

            RecommendationCardEnum.SMALL.ordinal -> {
                val binding = ViewRechargeRecommendationCardSmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardSmallViewHolder(recommendationTitle, recommendationListener, binding)
            }

            else -> {
                val binding = ViewRechargeRecommendationCardSmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecommendationCardSmallViewHolder(recommendationTitle, recommendationListener, binding)
            }
        }
        if (listRecommendationProduct.size == 1){
            viewHolder.itemView.run {
                layoutParams?.width = MATCH_PARENT
                setMargin(
                    resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        .toInt(),
                    resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        .toInt(),
                    resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                        .toInt(),
                    resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        .toInt()
                )
            }
        }
        return viewHolder
    }

    fun setRecommendationList(listRecommendationProduct: List<RecommendationCardWidgetModel>){
        this.listRecommendationProduct = listRecommendationProduct
    }
}