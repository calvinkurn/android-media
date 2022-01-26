package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.recharge_component.databinding.WidgetRechargeRecommendationCardBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.RecommendationCardWidgetAdapter
import org.jetbrains.annotations.NotNull

class RechargeRecommendationCardWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                 defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeRecommendationViewBinding: WidgetRechargeRecommendationCardBinding = WidgetRechargeRecommendationCardBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderRecommendationLayout(recommendationListener: RechargeRecommendationCardListener, titleRecommendation:String, listRecommendation: List<RecommendationCardWidgetModel>){
            val adapterRecommendation = RecommendationCardWidgetAdapter(recommendationListener)
            with(rechargeRecommendationViewBinding) {
                shimmeringRecommendation.root.hide()
                if (!listRecommendation.isNullOrEmpty()) {
                tgRechargeRecommendationCardTitle.show()
                rvRechargeRecommendationCardTitle.show()
                tgRechargeRecommendationCardTitle.text = titleRecommendation
                rvRechargeRecommendationCardTitle.apply {
                    adapterRecommendation.setRecommendationList(listRecommendation)
                    adapter = adapterRecommendation
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
            }
        }
    }

    fun renderShimmering(){
        with(rechargeRecommendationViewBinding){
            tgRechargeRecommendationCardTitle.hide()
            rvRechargeRecommendationCardTitle.hide()
            shimmeringRecommendation.root.show()
        }
    }

    fun renderFailRecommendation(){
        with(rechargeRecommendationViewBinding){
            shimmeringRecommendation.root.hide()
            root.hide()
        }
    }

}