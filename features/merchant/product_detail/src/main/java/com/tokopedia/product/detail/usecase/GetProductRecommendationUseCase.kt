package com.tokopedia.product.detail.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetProductRecommendationUseCase @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val getRecommendationFilterChips: GetRecommendationFilterChips,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        val userSessionInterface: UserSessionInterface)
    : UseCase<RecommendationWidget>(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + SupervisorJob()

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE_NAME = "pageName"
        const val PARAM_TOKONOW = "tokonow"
        const val PARAM_MINI_CART = "minicart"


        fun createParams(productId: String,
                         pageName: String,
                         isTokoNow: Boolean,
                         miniCartData: MutableMap<String, MiniCartItem>?): RequestParams =
                RequestParams.create().apply {
                    putString(PARAM_PRODUCT_ID, productId)
                    putString(PARAM_PAGE_NAME, pageName)
                    putBoolean(PARAM_TOKONOW, isTokoNow)
                    putObject(PARAM_MINI_CART, miniCartData)
                }
    }

    private var requestParams: RequestParams = RequestParams.EMPTY

    fun executeOnBackground(requestParams: RequestParams): RecommendationWidget {
        this.requestParams = requestParams
        return executeOnBackground(requestParams)
    }

    override suspend fun executeOnBackground(): RecommendationWidget {
        val productIdParam = requestParams.getString(PARAM_PRODUCT_ID, "")
        val pageNameParam = requestParams.getString(PARAM_PAGE_NAME, "")
        val isTokoNowParam = requestParams.getBoolean(PARAM_TOKONOW, false)
        val miniCartParam = requestParams.getObject("asd") as MutableMap<String, MiniCartItem>?

        val recommendationFilterResponse = getRecommendationFilter(
                pageNameParam,
                productIdParam,
                isTokoNowParam
        )

        val recommendationWidgetResponse = getRecommendationWidget(
                pageNameParam = pageNameParam,
                productId = productIdParam,
                isTokoNowParam = isTokoNowParam,
                recommendationFilterResponse = recommendationFilterResponse.await()
                        ?: mutableListOf()
        ).await()

        if (recommendationWidgetResponse == null ||
                recommendationWidgetResponse.recommendationItemList.isEmpty()) {
            throw MessageErrorException()
        } else {
            return updateRecomWhenTokoNow(recommendationWidgetResponse, miniCartParam)
        }
    }

    private fun getRecommendationWidget(pageNameParam: String,
                                        productId: String,
                                        isTokoNowParam: Boolean,
                                        recommendationFilterResponse: List<RecommendationFilterChipsEntity.RecommendationFilterChip>)
            : Deferred<RecommendationWidget?> {
        return asyncCatchError(dispatcher.io, block = {

            val recomRequestParams = GetRecommendationRequestParam(
                    pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                    pageName = pageNameParam,
                    productIds = arrayListOf(productId),
                    isTokonow = isTokoNowParam
            )

            val recomResponse = getRecommendationUseCase.getData(recomRequestParams)
            if (recomResponse.isNotEmpty() && recomResponse.first().recommendationItemList.isNotEmpty()) {
                recomResponse.first().copy(
                        recommendationFilterChips = recommendationFilterResponse,
                        pageName = pageNameParam
                )
            } else {
                RecommendationWidget()
            }
        }, onError = {
            null
        })
    }

    private fun getRecommendationFilter(pageNameParam: String,
                                        productId: String,
                                        isTokoNowParam: Boolean)
            : Deferred<MutableList<RecommendationFilterChipsEntity.RecommendationFilterChip>?> {
        return asyncCatchError(dispatcher.io, block = {
            val productIdsString = TextUtils.join(",", arrayListOf(productId)) ?: ""

            if (pageNameParam == ProductDetailConstant.PDP_3
                    || pageNameParam == ProductDetailConstant.PDP_K2K) {
                getRecommendationFilterChips.setParams(
                        userId = if (userSessionInterface.userId.isEmpty()) 0 else userSessionInterface.userId.toInt(),
                        pageName = pageNameParam,
                        productIDs = productIdsString,
                        xSource = ProductDetailConstant.DEFAULT_X_SOURCE,
                        isTokonow = isTokoNowParam
                )
                getRecommendationFilterChips.executeOnBackground().filterChip.toMutableList()
            } else {
                mutableListOf()
            }
        }, onError = {
            mutableListOf()
        })
    }

    private fun updateRecomWhenTokoNow(response: RecommendationWidget,
                                       miniCart: MutableMap<String, MiniCartItem>?): RecommendationWidget {
        return if (response.layoutType == LAYOUTTYPE_HORIZONTAL_ATC) {
            response.recommendationItemList.forEach { item ->
                miniCart?.let {
                    if (item.isProductHasParentID()) {
                        var variantTotalItems = 0
                        it.values.forEach { miniCartItem ->
                            if (miniCartItem.productParentId == item.parentID.toString()) {
                                variantTotalItems += miniCartItem.quantity
                            }
                        }
                        item.updateItemCurrentStock(variantTotalItems)
                    } else {
                        item.updateItemCurrentStock(it[item.productId.toString()]?.quantity
                                ?: 0)
                    }
                }
            }
            response
        } else {
            response
        }
    }
}