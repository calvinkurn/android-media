package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loaded
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loading
import com.tokopedia.thankyou_native.presentation.viewModel.state.RequestDataState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PDPThankYouViewModel @Inject constructor(
        private val getRecommendationUseCase: GetRecommendationUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val titleLiveData: MutableLiveData<RequestDataState<String>> = MutableLiveData()

    val recommendationListModel = MutableLiveData<RequestDataState<List<RecommendationWidget>>>()

    fun loadRecommendationData() {
        recommendationListModel.value = Loading
        launch(Dispatchers.IO) {
            val topAdsProductDef = try {
                val data = getRecommendationUseCase
                        .createObservable(getRecommendationUseCase.getRecomParams(
                                pageNumber = 1,
                                pageName = "thankyou",
                                productIds = arrayListOf()
                        )).toBlocking()
                Loaded(Success(data.first() ?: emptyList()))
            } catch (e: Throwable) {
                Loaded(Fail(e))
            }

            withContext(Dispatchers.Main) {
                recommendationListModel.value = if ((topAdsProductDef.data as? Success)?.data != null) {
                    titleLiveData.value = Loaded(Success(data = topAdsProductDef.data.data.first().title))
                    Loaded(Success((topAdsProductDef.data as? Success)?.data!!))
                } else {
                    Loaded(Fail(RuntimeException()))
                }
            }
        }
    }
}
