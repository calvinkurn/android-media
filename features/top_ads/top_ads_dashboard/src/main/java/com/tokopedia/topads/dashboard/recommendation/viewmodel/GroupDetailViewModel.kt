package com.tokopedia.topads.dashboard.recommendation.viewmodel

//import com.tokopedia.usecase.coroutines.Success
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGroupDetailUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GroupDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsGroupDetailUseCase: TopAdsGroupDetailUseCase
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private val _detailPageLiveData =
        MutableLiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>()
    val detailPageLiveData: LiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>
        get() = _detailPageLiveData

    fun loadDetailPage() {
        _detailPageLiveData.value = TopAdsListAllInsightState.Loading(0)
        launchCatchError(dispatcher.main, {
            val data = topAdsGroupDetailUseCase.executeOnBackground()
            _detailPageLiveData.value = TopAdsListAllInsightState.Success(data)
            Log.d("svfjbv", "getSellerPerformance: $data")
        }, {
            Log.d("svfjbv", "getSellerPerformance: ${it.message}")
            _detailPageLiveData.value = TopAdsListAllInsightState.Fail(it)
        })
    }


}
