package com.tokopedia.centralized_promo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoDiffUtil
import com.tokopedia.centralized_promo.view.adapter.DiffUtilHelper
import com.tokopedia.centralized_promo.view.item_decoration.OnGoingPromotionItemDecoration
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionListUiModel
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionUiModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation_shimmering.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation_success.view.*

class PartialCentralizedPromoRecommendationView(
        private val view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory
) : PartialView<RecommendedPromotionListUiModel, CentralizedPromoAdapterTypeFactory, RecommendedPromotionUiModel>(adapterTypeFactory) {

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() {
        with(view) {
            rvCentralizedPromoRecommendation.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = this@PartialCentralizedPromoRecommendationView.adapter
                addItemDecoration(OnGoingPromotionItemDecoration(4.dpToPx(context.resources.displayMetrics)))
                setHasFixedSize(true)
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