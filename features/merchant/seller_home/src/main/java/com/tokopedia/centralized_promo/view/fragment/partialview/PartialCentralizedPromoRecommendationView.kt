package com.tokopedia.centralized_promo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralized_promo.view.adapter.DiffUtilHelper
import com.tokopedia.centralized_promo.view.fragment.CoachMarkListener
import com.tokopedia.centralized_promo.view.item_decoration.OnGoingPromotionItemDecoration
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionListUiModel
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionUiModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation_shimmering.view.*

class PartialCentralizedPromoRecommendationView(
        private val view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener
) : PartialView<RecommendedPromotionListUiModel, CentralizedPromoAdapterTypeFactory, RecommendedPromotionUiModel>(adapterTypeFactory, coachMarkListener) {

    override var shouldShowCoachMark: Boolean = true

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() {
        with(view) {
            rvCentralizedPromoRecommendation.apply {
                layoutManager = object : GridLayoutManager(context, 2) {
                    override fun onLayoutCompleted(state: RecyclerView.State?) {
                        post {
                            isReadyToShowCoachMark = true
                            coachMarkListener.onCoachMarkItemReady()
                        }
                        super.onLayoutCompleted(state)
                    }
                }
                adapter = this@PartialCentralizedPromoRecommendationView.adapter
                addItemDecoration(OnGoingPromotionItemDecoration(4.dpToPx(context.resources.displayMetrics)))
            }
        }
    }

    override fun renderData(data: RecommendedPromotionListUiModel) {
        with(view) {
            DiffUtilHelper.calculate(adapter.data, data.promotions, adapter)
            partialSuccess.show()
            rvCentralizedPromoRecommendation.show()
            layoutCentralizedPromoRecommendationShimmering.hide()
        }
    }

    override fun renderError(cause: Throwable) {
        // Static data cannot pruduce error ._.
    }

    override fun onRefresh() {
        with(view) {
            partialSuccess.hide()
            rvCentralizedPromoRecommendation.hide()
            layoutCentralizedPromoRecommendationShimmering.show()
        }
    }
}