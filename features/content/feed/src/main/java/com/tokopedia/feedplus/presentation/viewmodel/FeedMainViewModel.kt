package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.feedplus.data.FeedXHeaderRequestFields
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val onboardingPreferences: OnboardingPreferences,
    private val userSession: UserSessionInterface
) : ViewModel(), OnboardingPreferences by onboardingPreferences {

    private val _feedTabs = MutableLiveData<Result<List<FeedDataModel>>>()
    val feedTabs: LiveData<Result<List<FeedDataModel>>>
        get() = _feedTabs

    private val _metaData = MutableLiveData<Result<MetaModel>>()
    val metaData: LiveData<Result<MetaModel>>
        get() = _metaData

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
                it.data.forEachIndexed { index, tab ->
                    if (tab.type == type && tab.isActive) {
                        _currentTabIndex.value = index
                    }
                }
            }
        }
    }

    fun fetchFeedTabs() {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.TAB.value
                        )
                    )
                )
                feedXHeaderUseCase.executeOnBackground()
            }
            val mappedData =
                MapperFeedTabs.transform(response.feedXHeaderData, userSession.isLoggedIn)
            _feedTabs.value = Success(mappedData.data)
        }) {
            _feedTabs.value = Fail(it)
        }
    }

    fun fetchFeedMetaData() {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.LIVE.value,
                            FeedXHeaderRequestFields.CREATION.value,
                            FeedXHeaderRequestFields.USER.value
                        )
                    )
                )
                feedXHeaderUseCase.executeOnBackground()
            }
            val mappedData =
                MapperFeedTabs.transform(response.feedXHeaderData, userSession.isLoggedIn)
            _metaData.value = Success(mappedData.meta)

            handleCreationData(
                MapperFeedTabs.getCreationBottomSheetData(
                    response.feedXHeaderData
                )
            )
        }) {
            _metaData.value = Fail(it)
            _feedCreateContentBottomSheetData.value = Fail(it)
        }
    }

    fun getTabType(index: Int): String =
        feedTabs.value?.let {
            if (it is Success && it.data.size > index) {
                it.data[index].type
            } else {
                ""
            }
        } ?: ""

    fun reportContent(feedReportRequestParamModel: FeedReportRequestParamModel) {
        viewModelScope.launchCatchError(block = {
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
