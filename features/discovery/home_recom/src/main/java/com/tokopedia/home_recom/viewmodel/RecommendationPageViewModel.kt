package com.tokopedia.home_recom.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_recom.HomeRecomRawQueryKeyConstant
import com.tokopedia.home_recom.PARAM_PRODUCT_ID
import com.tokopedia.home_recom.PrimaryProductParams
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.RecommendationProductDetail
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class RecommendationPageViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val getRecommendationUseCase: GetRecommendationUseCase,
                                                      @Named("Main")
                                  val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
    val recommendationListModel = MutableLiveData<List<RecommendationWidget>>()
    val productInfoDataModel = MutableLiveData<ProductInfoDataModel>()

    val xSource = "recom_widget"
    val pageName = "recom_page"

    fun getProductInfoDataModel(primaryProductParams: PrimaryProductParams) {
        launchCatchError(block = {
            val gqlData = withContext(Dispatchers.IO) {
                val cacheStrategy =
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

                val params = mapOf(
                        PARAM_PRODUCT_ID to primaryProductParams.productId
                )

                //still dummy
                val gqlRecommendationRequest = GraphqlRequest(
                        HomeRecomRawQueryKeyConstant.QUERY_PRIMARY_PRODUCT,
                        RecommendationProductDetail::class.java,
                        params
                )

                graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
            }



        }) {

        }
    }

    fun getRecommendationList(
            productIds: ArrayList<String>,
            onErrorGetRecommendation: ((errorMessage: String?) -> Unit)?) {
        getRecommendationUseCase.execute(
                getRecommendationUseCase.getRecomParams(
                        0,
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
}