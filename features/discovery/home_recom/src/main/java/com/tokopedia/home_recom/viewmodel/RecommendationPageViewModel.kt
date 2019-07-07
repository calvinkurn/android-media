package com.tokopedia.home_recom.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_recom.PARAM_PRODUCT_ID
import com.tokopedia.home_recom.PARAM_X_SOURCE
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

open class RecommendationPageViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val userSessionInterface: UserSessionInterface,
                                                      private val getRecommendationUseCase: GetRecommendationUseCase,
                                                      @Named("Main")
                                  val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
    open val recommendationListModel = MutableLiveData<List<RecommendationWidget>>()
    open val productInfoDataModel = MutableLiveData<ProductInfoDataModel>()

    val xSource = "recom_landing_page"
    val pageName = "recom_1,recom_2,recom_3"

    open fun getPrimaryProduct(productId: String,
                          context: Context) {
        launchCatchError(block = {
            val gqlData = withContext(Dispatchers.IO) {
                val cacheStrategy =
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

                var params = mapOf(
                        PARAM_PRODUCT_ID to productId.toInt(),
                        PARAM_X_SOURCE to xSource
                )

                val gqlRecommendationRequest = GraphqlRequest(
                        GraphqlHelper.loadRawString(context.resources, R.raw.gql_primary_product),
                        PrimaryProductEntity::class.java,
                        params
                )

                graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
            }
            gqlData.getSuccessData<PrimaryProductEntity>().productRecommendationProductDetail?.let {
                val productDetailResponse = it.data.get(0).recommendation.get(0)
                productInfoDataModel.value = ProductInfoDataModel(productDetailResponse)
            }

        }) {
            
        }
    }

    open fun getRecommendationList(
            productIds: ArrayList<String>,
            onErrorGetRecommendation: ((errorMessage: String?) -> Unit)?) {
        getRecommendationUseCase.execute(
                getRecommendationUseCase.getRecomParams(
                        1,
                        xSource,
                        pageName,
                        productIds), object : Subscriber<List<RecommendationWidget>>() {
            override fun onNext(t: List<RecommendationWidget>?) {
                recommendationListModel.value = t
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                onErrorGetRecommendation?.invoke(e?.message)
            }
        }
        )
    }

    fun isLoggedIn() = userSessionInterface.isLoggedIn
}