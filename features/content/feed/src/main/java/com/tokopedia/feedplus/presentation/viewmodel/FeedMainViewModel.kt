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
import com.tokopedia.feedplus.presentation.model.CreateContentType
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

    private val _currentTabIndex = MutableLiveData<Int>()
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    fun changeCurrentTabByIndex(index: Int) {
        _currentTabIndex.value = index
    }

    fun changeCurrentTabByType(type: String) {
        feedTabs.value?.let {
            if (it is Success) {
                it.data.data.forEachIndexed { index, tab ->
                    if (tab.type == type && tab.isActive) {
                        _currentTabIndex.value = index
                    }
                }
            }
        }
    }

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

    fun getTabType(index: Int): String =
        feedTabs.value?.let {
            if (it is Success && it.data.data.size > index) {
                it.data.data[index].type
            } else {
                ""
            }
        } ?: ""

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

    /**
     * Creation Button Position is Static :
     * 1. Short
     * 2. Post
     * 3. Live
     */
    private fun handleCreationData(creationDataList: List<ContentCreationItem>) {
        val authorUserdataList = creationDataList.find { it.type == CreatorType.USER }?.items
        val authorShopDataList = creationDataList.find { it.type == CreatorType.SHOP }?.items

        val creatorList = mutableListOf<ContentCreationTypeItem>()

        authorShopDataList?.find {
            it.type == CreateContentType.CREATE_SHORT_VIDEO && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorUserdataList?.find {
            it.type == CreateContentType.CREATE_SHORT_VIDEO && it.isActive
        }?.let {
            creatorList.add(it)
        }

        authorShopDataList?.find {
            it.type == CreateContentType.CREATE_POST && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorUserdataList?.find {
            it.type == CreateContentType.CREATE_POST && it.isActive
        }?.let {
            creatorList.add(it)
        }

        authorShopDataList?.find {
            it.type == CreateContentType.CREATE_LIVE && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorUserdataList?.find {
            it.type == CreateContentType.CREATE_LIVE && it.isActive
        }?.let {
            creatorList.add(it)
        }

        _feedCreateContentBottomSheetData.value = Success(creatorList)
    }
}
