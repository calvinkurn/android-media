package com.tokopedia.cartrevamp.view

import androidx.lifecycle.ViewModel
import com.tokopedia.cartrevamp.view.uimodel.CartModel
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import kotlinx.coroutines.launch
import timber.log.Timber

class CartViewModel : ViewModel() {

    var cartModel: CartModel = CartModel()
        private set

    fun processGetRecommendationData(page: Int, allProductIds: List<String>) {
//        view?.showItemLoading()
//        launch {
//            try {
//                val recommendationWidgets = getRecommendationUseCase.getData(
//                    GetRecommendationRequestParam(
//                        pageNumber = page,
//                        xSource = "recom_widget",
//                        pageName = "cart",
//                        productIds = allProductIds,
//                        queryParam = ""
//                    )
//                )
//                view?.let {
//                    it.hideItemLoading()
//                    if (recommendationWidgets[0].recommendationItemList.isNotEmpty()) {
//                        it.renderRecommendation(recommendationWidgets[0])
//                    }
//                    it.setHasTriedToLoadRecommendation()
//                    it.stopAllCartPerformanceTrace()
//                }
//            } catch (t: Throwable) {
//                Timber.d(t)
//                view?.hideItemLoading()
//                view?.setHasTriedToLoadRecommendation()
//                view?.stopAllCartPerformanceTrace()
//            }
//        }
    }
}
