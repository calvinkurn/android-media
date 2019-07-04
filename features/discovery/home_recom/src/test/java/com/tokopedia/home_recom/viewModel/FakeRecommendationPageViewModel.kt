package com.tokopedia.home_recom.viewModel

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import org.mockito.Mock

/**
 * Created by Lukas on 2019-07-04
 */
open class FakeRecommendationPageViewModel(
        graphqlRepository: GraphqlRepository,
        userSessionInterface: UserSessionInterface,
        getRecommendationUseCase: GetRecommendationUseCase,
        dispatcher: CoroutineDispatcher
) : RecommendationPageViewModel(
    graphqlRepository, userSessionInterface, getRecommendationUseCase, dispatcher
){
    val errorMessage = "error"

    @Mock
    lateinit var mockRecommendationWidget: RecommendationWidget
    @Mock
    lateinit var mockProductInfoDataModel: ProductInfoDataModel

    private var shouldReturnError = false

    fun setReturnError(value: Boolean){
        shouldReturnError = value
    }

    override fun getPrimaryProduct(productId: String, context: Context) {
        if(shouldReturnError){
            productInfoDataModel.value = null
        }else {
            productInfoDataModel.value = mockProductInfoDataModel
        }
    }

    override fun getRecommendationList(productIds: ArrayList<String>, onErrorGetRecommendation: ((errorMessage: String?) -> Unit)?) {
        if(shouldReturnError){
            onErrorGetRecommendation?.invoke(errorMessage)
        }else {
            recommendationListModel.value = listOf(mockRecommendationWidget)
        }
    }
}