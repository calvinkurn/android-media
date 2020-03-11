package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.RecommendedPromotionListUiModel
import com.tokopedia.centralizedpromo.view.model.RecommendedPromotionUiModel
import com.tokopedia.centralizedpromo.view.viewholder.RecommendedPromotionViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation_shimmering.view.*

class PartialCentralizedPromoRecommendationView(
        view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        shouldWaitForCoachMark: Boolean
) : BasePartialListView<RecommendedPromotionListUiModel, CentralizedPromoAdapterTypeFactory, RecommendedPromotionUiModel>(view, adapterTypeFactory, coachMarkListener, shouldWaitForCoachMark) {

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() = with(view) {
        rvCentralizedPromoRecommendation.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@PartialCentralizedPromoRecommendationView.adapter
            isNestedScrollingEnabled = false
            addItemDecoration(RecommendedPromotionViewHolder.ItemDecoration(context.dpToPx(4).toInt()))
        }
    }

    override fun renderLoading() = with(view) {
        partialSuccess.hide()
        rvCentralizedPromoRecommendation.hide()
        layoutCentralizedPromoRecommendationShimmering.show()
    }

    override fun onRecyclerViewResultDispatched() = with(view) {
        partialSuccess.show()
        rvCentralizedPromoRecommendation.show()
        layoutCentralizedPromoRecommendationShimmering.hide()
    }

    override fun getSuccessView(): ConstraintLayout? = view.layoutCentralizedPromoRecommendation

    override fun onRecyclerViewItemEmpty() {}

    override fun bindSuccessData(data: RecommendedPromotionListUiModel) {}
}