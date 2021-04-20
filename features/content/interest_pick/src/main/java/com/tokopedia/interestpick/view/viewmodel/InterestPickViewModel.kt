package com.tokopedia.interestpick.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.interestpick.data.pojo.*
import com.tokopedia.interestpick.domain.usecase.GetInterestUseCase
import com.tokopedia.interestpick.domain.usecase.UpdateInterestUseCase
import com.tokopedia.interestpick.view.subscriber.InterestPickViewState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_HEADER_TITLE = "Kamu Suka Apa?"

class InterestPickViewModel @Inject constructor(
        private val getInterestPickUseCase: GetInterestUseCase,
        private val updateInterestUseCase: UpdateInterestUseCase) : ViewModel() {

    private var interestPickLiveData: MutableLiveData<InterestPickViewState> = MutableLiveData()

    var isSaved = false

    fun getInterestPickLiveData(): LiveData<InterestPickViewState> = interestPickLiveData

    fun onViewCreated(){
        fetchData()
    }

    fun onRetry(){
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launchCatchError(
                block = {
                    interestPickLiveData.value = InterestPickViewState.LoadingState
                    val getInterestData: GetInterestData = getInterestPickUseCase.executeOnBackground()
                    handleGetInterestData(getInterestData.feedInterestUser)
                },
                onError = {
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        it.printStackTrace()
                    }
                    interestPickLiveData.value = it.message?.let { message -> InterestPickViewState.Error(onUpdate = false, error = message) }
                }
        )
    }

    fun updateInterest(interestIds: List<Int>) {
        viewModelScope.launchCatchError(
                block = {
                    interestPickLiveData.value = InterestPickViewState.ProgressState
                    updateInterestUseCase.setRequestParams(interestIds)
                    val updateInterestData = updateInterestUseCase.executeOnBackground()
                    handleUpdateInterestData(updateInterestData.feedInterestUserUpdate)
                },
                onError = {
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        it.printStackTrace()
                    }
                    interestPickLiveData.value = it.message?.let { message -> InterestPickViewState.Error(onUpdate = true, error = message) }
                }
        )
    }

    fun updateInterestWIthSkip() {
        viewModelScope.launch(
                block = {
                    updateInterestUseCase.setRequestParamsSkip()
                    updateInterestUseCase.executeOnBackground()
                }
        )
    }

    private fun handleUpdateInterestData(feedInterestUserUpdate: FeedInterestUserUpdate) {
        if (feedInterestUserUpdate.error.isNotEmpty()) {
            interestPickLiveData.value = InterestPickViewState.Error(onUpdate = true, error = feedInterestUserUpdate.error)
        } else {
            interestPickLiveData.value = InterestPickViewState.UpdateInterestSuccess
            isSaved = true
        }
    }

    private fun handleGetInterestData(feedInterestUser: FeedInterestUser) {
        if (feedInterestUser.error.isNotEmpty()) {
            interestPickLiveData.value = InterestPickViewState.Error(onUpdate = false, error = feedInterestUser.error)
        } else {
            interestPickLiveData.value = InterestPickViewState.GetInterestSuccess(headerModel = getTitle(feedInterestUser.header),
                    interestList = convertToInterestList(feedInterestUser.interests))
        }
    }

    fun convertToInterestList(list: List<InterestsItem>)
            : ArrayList<InterestPickDataViewModel> {
        val interestList: ArrayList<InterestPickDataViewModel> = ArrayList()
        for (item in list) {
            interestList.add(
                    InterestPickDataViewModel(
                            item.id,
                            item.name,
                            item.imageUrl,
                            item.relationships.isSelected
                    )
            )
        }
        return interestList
    }

    fun getTitle(header: Header): String {
        return if (header.title.isNotEmpty()) {
            header.title
        } else {
            DEFAULT_HEADER_TITLE
        }
    }

    fun onBackPressed() {
        if (!isSaved) {
            updateInterestWIthSkip()
        }
    }
}