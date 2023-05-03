package com.tokopedia.recommendation_widget_common.presenter

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.EmptyRecomException
import com.tokopedia.recommendation_widget_common.viewutil.asFail
import com.tokopedia.recommendation_widget_common.viewutil.asSuccess
import com.tokopedia.recommendation_widget_common.viewutil.isRecomPageNameEligibleForChips
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class RecomWidgetV2ViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    val userSession: UserSessionInterface,
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val addToCartUseCase: Lazy<AddToCartUseCase>,
    private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
    private val updateCartUseCase: Lazy<UpdateCartUseCase>,
    private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
    private val getRecommendationFilterChips: Lazy<GetRecommendationFilterChips>
) : BaseViewModel(dispatcher.main) {

    val titleFlow = MutableStateFlow("BPC Header")

    private val recommendationsMap: MutableMap<String, Result<RecommendationWidget>> = mutableMapOf()
    private val _recommendationsFlow: MutableStateFlow<Map<String, Result<RecommendationWidget>>> = MutableStateFlow(recommendationsMap.toMap())

//    val recommendationsFlow: StateFlow<Map<String, Result<RecommendationWidget>>> = _recommendationsFlow
    val recommendationsFlow = mutableMapOf<String, StateFlow<Result<RecommendationWidget>>>()

    fun loadRecommendation(recom: RecommendationVisitable) {
        viewModelScope.launchCatchError(block = {
            val isTokonow = recom is RecommendationCarouselModel && recom.isTokonow
            val recomFilterList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
            if (recom.metadata.pageName.isRecomPageNameEligibleForChips()) {
                getRecommendationFilterChips.get().setParams(
                    userId = if (userSession.userId.isEmpty()) 0 else userSession.userId.toInt(),
                    pageName = recom.metadata.pageName,
                    productIDs = TextUtils.join(",", recom.metadata.productIds) ?: "",
                    isTokonow = isTokonow,
                    xSource = recom.metadata.pageSource?.value.orEmpty()
                )
                recomFilterList.addAll(getRecommendationFilterChips.get().executeOnBackground().filterChip)
            }
            val result = getRecommendationUseCase.get().getData(
                GetRecommendationRequestParam(
                    pageNumber = recom.metadata.pageNumber,
                    productIds = recom.metadata.productIds,
                    queryParam = recom.metadata.queryParam,
                    pageName = recom.metadata.pageName,
                    categoryIds = recom.metadata.categoryIds,
                    xSource = recom.metadata.pageSource?.value.orEmpty(),
                    xDevice = recom.metadata.device,
                    keywords = recom.metadata.keyword,
                    isTokonow = isTokonow
                )
            )
            Log.d("globalrecom", "loadRecommendation: $result")
            if (result.isNotEmpty()) {
                val recomWidget = result[0].copy(recommendationFilterChips = recomFilterList)
                if (isTokonow) {
//                        mappingMiniCartDataToRecommendation(recomWidget, miniCartData.value)
                }
                recommendationsMap[recom.metadata.pageName] = recomWidget.asSuccess()
            } else {
                recommendationsMap[recom.metadata.pageName] = EmptyRecomException().asFail()
            }
            _recommendationsFlow.emit(recommendationsMap.toMap())
        }) {
            Log.e("globalrecom", "loadRecommendation: ", it)
            recommendationsMap[recom.metadata.pageName] = it.asFail()
            _recommendationsFlow.emit(recommendationsMap.toMap())
        }
    }

    fun getRecommendationByPageName(pageName: String): StateFlow<Result<RecommendationWidget>> {
        return recommendationsFlow.getOrPut(pageName) {
            _recommendationsFlow.map {
                it.getOrElse(pageName) {
                    RecommendationWidget().asSuccess()
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                RecommendationWidget().asSuccess()
            )
        }
    }
}
