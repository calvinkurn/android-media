package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedTabsUseCase
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @Inject constructor(
    private val feedTabsUseCase: FeedTabsUseCase,
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _feedTabs = MutableLiveData<Result<FeedTabsModel>>()
    private val _reportResponse = MutableLiveData<Result<FeedComplaintSubmitReportResponse>>()
    val feedTabs: LiveData<Result<FeedTabsModel>>
        get() = _feedTabs
    val reportResponse: LiveData<Result<FeedComplaintSubmitReportResponse>>
        get() = _reportResponse

    fun fetchFeedTabs() {
        launchCatchError(dispatchers.main, block = {
            val response = withContext(dispatchers.io) { feedTabsUseCase.executeOnBackground() }
            _feedTabs.value = (Success(MapperFeedTabs.transform(response.feedTabs)))
        }) {
            _feedTabs.value = Fail(it)
        }
    }

    fun reportContent(feedReportRequestParamModel: FeedReportRequestParamModel) {
        launchCatchError(dispatchers.io, block = {
            submitReportUseCase.setRequestParams(
                FeedComplaintSubmitReportUseCase.createParam(
                    feedReportRequestParamModel
                )
            )
            val response = submitReportUseCase.executeOnBackground()
            if (response.data.success.not()) {
                throw MessageErrorException("Error in Reporting")
            } else {
                _reportResponse.postValue(Success(response))
            }
        }) {
            _reportResponse.postValue(Fail(it))
        }
    }


}
