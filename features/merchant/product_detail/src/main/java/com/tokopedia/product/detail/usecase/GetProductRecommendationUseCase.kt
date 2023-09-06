package com.tokopedia.product.detail.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetProductRecommendationUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getRecommendationFilterChips: GetRecommendationFilterChips,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    val userSessionInterface: UserSessionInterface
) :
    UseCase<RecommendationWidget>(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + SupervisorJob()

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE_NAME = "pageName"
        const val PARAM_TOKONOW = "tokonow"
        const val PARAM_MINI_CART = "minicart"
        const val PARAM_QUERY_PARAM = "queryParam"
        const val PARAM_THEMATIC_ID = "thematicId"

        fun createParams(
            productId: String,
            pageName: String,
            isTokoNow: Boolean,
            miniCartData: MutableMap<String, MiniCartItem.MiniCartItemProduct>?,
            queryParam: String,
            thematicId: String
        ): RequestParams =
            RequestParams.create().apply {
                putString(PARAM_PRODUCT_ID, productId)
                putString(PARAM_PAGE_NAME, pageName)
                putBoolean(PARAM_TOKONOW, isTokoNow)
                putObject(PARAM_MINI_CART, miniCartData)
                putString(PARAM_QUERY_PARAM, queryParam)
                putString(PARAM_THEMATIC_ID, thematicId)
            }
    }

    private var requestParams: RequestParams = RequestParams.EMPTY

    suspend fun executeOnBackground(requestParams: RequestParams): RecommendationWidget {
        this.requestParams = requestParams
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): RecommendationWidget {
        val productIdParam = requestParams.getString(PARAM_PRODUCT_ID, "")
        val pageNameParam = requestParams.getString(PARAM_PAGE_NAME, "")
        val isTokoNowParam = requestParams.getBoolean(PARAM_TOKONOW, false)
        val miniCartParam =
            requestParams.getObject(PARAM_MINI_CART) as MutableMap<String, MiniCartItem.MiniCartItemProduct>?
        val queryParam = requestParams.getString(PARAM_QUERY_PARAM, "")
        val thematicIdParam = requestParams.getString(PARAM_THEMATIC_ID, "")

        val recommendationFilterResponse = try {
            getRecommendationFilter(
                pageNameParam,
                productIdParam,
                isTokoNowParam
            )
        } catch (e: Throwable) {
            mutableListOf()
        }

        val recommendationWidgetResponse = try {
            getRecommendationWidget(
                pageNameParam = pageNameParam,
                productId = productIdParam,
                isTokoNowParam = isTokoNowParam,
                recommendationFilterResponse = recommendationFilterResponse ?: mutableListOf(),
                queryParam = queryParam,
                thematicId = thematicIdParam
            )
        } catch (e: Throwable) {
            null
        }

        if (recommendationWidgetResponse == null ||
            recommendationWidgetResponse.recommendationItemList.isEmpty()
        ) {
            throw MessageErrorException()
        } else {
            return updateRecomWhenTokoNow(recommendationWidgetResponse, miniCartParam)
        }
    }

    private suspend fun getRecommendationWidget(
        pageNameParam: String,
        productId: String,
        isTokoNowParam: Boolean,
        thematicId: String,
        queryParam: String,
        recommendationFilterResponse: List<RecommendationFilterChipsEntity.RecommendationFilterChip>
    ): RecommendationWidget {
        return withContext(dispatcher.io, block = {
            val recomRequestParams = GetRecommendationRequestParam(
                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                pageName = pageNameParam,
                productIds = arrayListOf(productId),
                isTokonow = isTokoNowParam,
                queryParam = queryParam,
                criteriaThematicIDs = arrayListOf(thematicId)
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
        })
    }

    private suspend fun getRecommendationFilter(
        pageNameParam: String,
        productId: String,
        isTokoNowParam: Boolean
    ): MutableList<RecommendationFilterChipsEntity.RecommendationFilterChip>? {
        return withContext(dispatcher.io, block = {
            val productIdsString = TextUtils.join(",", arrayListOf(productId)) ?: ""

            if (pageNameParam == ProductDetailConstant.PDP_3 ||
                pageNameParam == ProductDetailConstant.PDP_K2K
            ) {
                getRecommendationFilterChips.setParams(
                    userId = if (userSessionInterface.userId.isEmpty()) 0 else userSessionInterface.userId.toInt(),
                    pageName = pageNameParam,
                    productIDs = productIdsString,
                    xSource = ProductDetailConstant.DEFAULT_X_SOURCE,
                    isTokonow = isTokoNowParam
                )
                val result = getRecommendationFilterChips.executeOnBackground()
                result.filterChip.toMutableList()
            } else {
                mutableListOf()
            }
        })
    }

    private fun updateRecomWhenTokoNow(
        response: RecommendationWidget,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?
    ): RecommendationWidget {
        return if (response.hasQuantityEditor()) {
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
                        item.updateItemCurrentStock(
                            it[item.productId.toString()]?.quantity
                                ?: 0
                        )
                    }
                }
            }
            response
        } else {
            response
        }
    }
}
