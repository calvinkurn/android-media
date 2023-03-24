package com.tokopedia.product.detail.view.viewmodel.slicing.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.detail.view.viewmodel.slicing.ProductRecommendationSlice
import com.tokopedia.product.detail.view.viewmodel.slicing.ViewModelSlice
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

class ProductRecommendationSliceImpl @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase
) : ViewModelSlice(), ProductRecommendationSlice {

    private var alreadyHitRecom: MutableList<String> = mutableListOf()

    private val _loadViewToView = MutableLiveData<Result<RecommendationWidget>>()
    override val loadViewToView: LiveData<Result<RecommendationWidget>>
        get() = _loadViewToView

    private val _verticalRecommendation = MutableLiveData<Result<RecommendationWidget>>()
    override val verticalRecommendation: LiveData<Result<RecommendationWidget>>
        get() = _verticalRecommendation

    override fun loadViewToView(
        pageName: String,
        productId: String,
        isTokoNow: Boolean
    ) {
        if (GlobalConfig.isSellerApp()) return

        if (!alreadyHitRecom.contains(pageName)) {
            alreadyHitRecom.add(pageName)
        } else {
            return
        }

        scope.launch(scope.coroutineContext) {
            try {
                val response = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                        pageName = pageName,
                        productIds = arrayListOf(productId),
                        isTokonow = isTokoNow
                    )
                )

                _loadViewToView.value = if (response.isNotEmpty()) {
                    Success(response.first())
                } else {
                    Fail(MessageErrorException())
                }
            } catch (t: Throwable) {
                alreadyHitRecom.remove(pageName)
                _loadViewToView.value = Throwable(pageName, t).asFail()
            }
        }
    }

    override fun getVerticalRecommendationData(pageName: String, page: Int?, productId: String?) {
        val nonNullPage = page ?: ProductDetailConstant.DEFAULT_PAGE_NUMBER
        val nonNullProductId = productId.orEmpty()

        scope.launch(scope.coroutineContext) {
            try {
                val requestParams = GetRecommendationRequestParam(
                    pageNumber = nonNullPage,
                    pageName = pageName,
                    productIds = arrayListOf(nonNullProductId)
                )
                val recommendationResponse = getRecommendationUseCase.getData(requestParams)
                val dataResponse = recommendationResponse.firstOrNull()
                if (dataResponse == null) {
                    _verticalRecommendation.value = Fail(Throwable())
                } else {
                    _verticalRecommendation.value = dataResponse.asSuccess()
                }
            } catch (t: Throwable) {
                _verticalRecommendation.value = Fail(t)
            }
        }
    }
}
