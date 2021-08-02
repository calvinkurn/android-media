package com.tokopedia.recommendation_widget_common.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.asFail
import com.tokopedia.recommendation_widget_common.viewutil.asSuccess
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import javax.inject.Inject

/**
 * Created by yfsx on 02/08/21.
 */
class RecommendationViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        val userSession: UserSessionInterface,
        val getRecommendationUseCase: GetRecommendationUseCase
) : BaseViewModel(dispatcher.io) {

    private var getRecommendationJob: Job? = null

    private val _getRecommendationLiveData = MutableLiveData<Result<RecommendationWidget>>()
    val getRecommendationLiveData: LiveData<Result<RecommendationWidget>>
        get() = _getRecommendationLiveData

    fun loadRecommendation(
            pageNumber: Int = 1,
            productIds: List<String> = listOf(),
            queryParam: String = "",
            pageName: String = "",
            categoryIds: List<String> = listOf(),
            xSource: String = "",
            xDevice: String = "",
            isTokonow: Boolean = false,
            keywords: List<String> = listOf()) {
        if (isJobAvailable(getRecommendationJob)) {
            getRecommendationJob = viewModelScope.launchCatchError(coroutineContext, {
                val result = getRecommendationUseCase.getData(
                        GetRecommendationRequestParam(
                                pageNumber = pageNumber,
                                productIds = productIds,
                                queryParam = queryParam,
                                pageName = pageName,
                                categoryIds = categoryIds,
                                xSource = xSource,
                                xDevice = xDevice,
                                keywords = keywords,
                                isTokonow = isTokonow,
                        ))
                if (result.isNotEmpty()) {
                    val recomWidget = result[0]
                    _getRecommendationLiveData.postValue(recomWidget.asSuccess())
                }
            }) {
                _getRecommendationLiveData.postValue(it.asFail())
            }
        }
    }

    private fun isJobAvailable(job: Job?): Boolean = job == null || !job?.isActive

}