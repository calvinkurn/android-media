package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreatorType
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
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _isInClearView = MutableLiveData<Boolean>(false)
    val isInClearView: LiveData<Boolean>
        get() = _isInClearView

    private val _feedTabs = MutableLiveData<Result<FeedTabsModel>>()
    val feedTabs: LiveData<Result<FeedTabsModel>>
        get() = _feedTabs

    private val _reportResponse = MutableLiveData<Result<FeedComplaintSubmitReportResponse>>()
    val reportResponse: LiveData<Result<FeedComplaintSubmitReportResponse>>
        get() = _reportResponse

    private val _feedCreateContentBottomSheetData =
        MutableLiveData<Result<List<ContentCreationTypeItem>>>()
    val feedCreateContentBottomSheetData: LiveData<Result<List<ContentCreationTypeItem>>>
        get() = _feedCreateContentBottomSheetData

    fun fetchFeedTabs() {
        launchCatchError(dispatchers.main, block = {
            val response = withContext(dispatchers.io) {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam()
                )
                feedXHeaderUseCase.executeOnBackground()
            }
            _feedTabs.value = Success(MapperFeedTabs.transform(response.feedXHeaderData))

            handleCreationData(
                MapperFeedTabs.getCreationBottomSheetData(
                    response.feedXHeaderData
                )
            )
        }) {
            _feedTabs.value = Fail(it)
            _feedCreateContentBottomSheetData.value = Fail(it)
        }
    }

    fun reportContent(feedReportRequestParamModel: FeedReportRequestParamModel) {
        launchCatchError(dispatchers.main, block = {
            val response = withContext(dispatchers.io) {
                submitReportUseCase.setRequestParams(
                    FeedComplaintSubmitReportUseCase.createParam(
                        feedReportRequestParamModel
                    )
                )
                submitReportUseCase.executeOnBackground()
            }
            if (response.data.success.not()) {
                throw MessageErrorException("Error in Reporting")
            } else {
                _reportResponse.value = Success(response)
            }
        }) {
            _reportResponse.value = Fail(it)
        }
    }

    fun toggleClearView(clearView: Boolean) {
        _isInClearView.value = clearView
    }

    private fun handleCreationData(creationDataList: List<ContentCreationItem>) {
        val authorUserdata = creationDataList.find { it.type == CreatorType.USER }
        val authorUserdataList = creationDataList.find { it.type == CreatorType.USER }?.items
        val authorShopdata = creationDataList.find { it.type == CreatorType.SHOP }
        val authorShopdataList = creationDataList.find { it.type == CreatorType.SHOP }?.items

        val creatorList =
            (authorUserdataList?.filter { it.isActive ?: false } ?: emptyList()) +
                (authorShopdataList?.filter { it.isActive ?: false } ?: emptyList()).distinct()
        _feedCreateContentBottomSheetData.value = Success(creatorList)
    }
}
