package com.tokopedia.recommendation_widget_common.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.*
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationList
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModelDummy
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class RecommendationItemViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                  private val userSessionInterface: UserSessionInterface,
                                  @Named("Main")
                                  val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
    val recommendationListModel = MutableLiveData<RecommendationList>()

    fun getRecommendationList(recommendationParams: RecommendationParams) {
        launchCatchError(block = {
            val gqlData = withContext(Dispatchers.IO) {
                val cacheStrategy =
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

                val params = mapOf(
                        PARAM_USER_ID to userSessionInterface.userId,
                        PARAM_X_SOURCE to recommendationParams.source,
                        PARAM_PAGE_NUMBER to recommendationParams.pageNumber,
                        PARAM_X_DEVICE to DEFAULT_VALUE_X_DEVICE,
                        PARAM_PAGE_NAME to recommendationParams.pageName,
                        PARAM_PRODUCT_IDS to recommendationParams.productIds
                )

                //still dummy
                val gqlRecommendationRequest = GraphqlRequest(
                        RecommendationRawQueryKeyConstant.QUERY_RECOMMENDATION_WIDGET,
                        RecomendationEntity::class.java,
                        params
                )

//                graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
            }

            recommendationListModel.value = RecommendationList(
                    listOf(
                            generateDummyInfo("Product Info", TYPE_INFO),
                            generateDummyModel("Product Info", TYPE_CAROUSEL),
                            generateDummyModel("Product Info", TYPE_SCROLL))
            )
        }) {

        }
    }

    private fun generateDummyInfo(
            title: String,
            type: String
    ) : RecommendationModelDummy {
        val listItem: MutableList<RecommendationItem> = arrayListOf()
        listItem.add(RecommendationItem(
                1,
                "Product1",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        return RecommendationModelDummy(
                listItem,
                title,
                title,
                "dari sana",
                "www.facebook.com",
                "www.facebook.com",
                type
        )
    }

    private fun generateDummyModel(
            title: String,
            type: String
    ) : RecommendationModelDummy {
        val listItem: MutableList<RecommendationItem> = arrayListOf()
        listItem.add(RecommendationItem(
                1,
                "Product1",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                2,
                "Product2",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                3,
                "Product3",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                4,
                "Product4",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                5,
                "Product5",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                6,
                "Product6",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                7,
                "Product7",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                8,
                "Product8",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                9,
                "Product9",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        listItem.add(RecommendationItem(
                10,
                "Product10",
                "breadcumbs aja",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "www.facebook.com",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp 20500",
                20500,
                1,
                5,
                250,
                40,
                "testing",
                true
        ))
        return RecommendationModelDummy(
                listItem,
                title,
                title,
                "dari mana saja",
                "www.facebook.com",
                "www.facebook.com",
                type
        )
    }
}