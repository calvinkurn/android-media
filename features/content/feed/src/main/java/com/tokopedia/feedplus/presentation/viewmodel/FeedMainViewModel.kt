package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.usecase.GetWhiteListUseCase
import com.tokopedia.feedplus.data.FeedXHeader
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase,
    private val getWhiteListUseCase: GetWhiteListUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val onboardingPreferences: OnboardingPreferences,
    private val userSession: UserSessionInterface,
) : ViewModel(), OnboardingPreferences by onboardingPreferences {

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

    fun fetchData() {
        viewModelScope.launch {
            val header = async { getFeedHeader() }
            val whiteList = async { getWhiteList() }

            val feedTabs = MapperFeedTabs.transform(
                header.await(),
                whiteList.await(),
                userSession.isLoggedIn,
            )

            _feedTabs.value = Success(feedTabs)
        }
    }

    fun checkLoginStatus() {
//        _feedTabs.value = when (val result = _feedTabs.value) {
//            is Success -> result.copy(
//                data = result.data.copy(
//                    meta = result.data.meta.login(userSession.isLoggedIn)
//                )
//            )
//            else -> result
//        }
    }

    private suspend fun getWhiteList(): GetCheckWhitelistResponse {
        if (!userSession.isLoggedIn) return GetCheckWhitelistResponse()
        return getWhiteListUseCase(GetWhiteListUseCase.WhiteListType.EntryPoint)
    }

    private suspend fun getFeedHeader(): FeedXHeader {
        feedXHeaderUseCase.setRequestParams(
            FeedXHeaderUseCase.createParam()
        )
        return feedXHeaderUseCase.executeOnBackground().feedXHeaderData
    }

    fun fetchFeedTabs() {
        viewModelScope.launchCatchError(dispatchers.main, block = {
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
        viewModelScope.launchCatchError(dispatchers.main, block = {
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
        val authorUserdataList = creationDataList.find { it.type == CreatorType.USER }?.items
        val authorShopDataList = creationDataList.find { it.type == CreatorType.SHOP }?.items

        val creatorList =
            (authorUserdataList?.filter { it.isActive ?: false } ?: emptyList()) +
                (authorShopDataList?.filter { it.isActive ?: false } ?: emptyList()).distinct()
        _feedCreateContentBottomSheetData.value = Success(creatorList)
    }
}
