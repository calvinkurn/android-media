package com.tokopedia.home_recom.testViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Created by Lukas on 2019-07-04
 */
class FakeRecommendationPageViewModel(
        graphqlRepository: GraphqlRepository,
        userSessionInterface: UserSessionInterface,
        getRecommendationUseCase: GetRecommendationUseCase,
        dispatcher: CoroutineDispatcher
) : RecommendationPageViewModel(
    graphqlRepository, userSessionInterface, getRecommendationUseCase, dispatcher
){
    val errorMessage = "error"

    private val mockRecommendationWidget: RecommendationWidget = Mockito.mock(RecommendationWidget::class.java)

    private val mockProductInfoDataModel: ProductInfoDataModel = Mockito.mock(ProductInfoDataModel::class.java)

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
            recommendationListModel.value = null
            onErrorGetRecommendation?.invoke(errorMessage)
        }else {
            recommendationListModel.value = listOf(mockRecommendationWidget)
        }
    }
}