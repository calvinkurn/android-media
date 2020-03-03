package com.tokopedia.centralized_promo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralized_promo.view.item_decoration.OnGoingPromotionItemDecoration
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionListUiModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation_shimmering.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation_success.view.*

class PartialCentralizedPromoRecommendationView(private val view: View)
    : PartialView<RecommendedPromotionListUiModel, CentralizedPromoAdapterTypeFactory>(CentralizedPromoAdapterTypeFactory()) {

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() {
        with(view) {
            rvCentralizedPromoRecommendation.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = this@PartialCentralizedPromoRecommendationView.adapter
                addItemDecoration(OnGoingPromotionItemDecoration(4.dpToPx(context.resources.displayMetrics)))
            }
        }
    }

    override fun renderData(data: RecommendedPromotionListUiModel) {
        with(view) {
            adapter.apply {
                setElements(data.promotions)
                notifyDataSetChanged()
            }
            layoutCentralizedPromoRecommendationSuccess.show()
            layoutCentralizedPromoRecommendationShimmering.hide()
        }
    }

    override fun renderError(cause: Throwable) {
        // Static data cannot pruduce error ._.
    }

    override fun onRefresh() {
        with(view) {
            layoutCentralizedPromoRecommendationSuccess.hide()
            layoutCentralizedPromoRecommendationShimmering.show()
        }
    }
}