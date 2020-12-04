package com.tokopedia.tokopoints.view.tokopointhome

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.model.usersaving.TokopointsUserSaving
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingResponse
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@TokoPointScope
class TokoPointsHomeViewModel @Inject constructor(private val repository: TokopointsHomeRepository) : BaseViewModel(Dispatchers.Main), TokoPointsHomeContract.Presenter {

    val tokopointDetailLiveData = MutableLiveData<Resources<TokopointSuccess>>()
    val rewardIntroData = MutableLiveData<Resources<IntroResponse>>()
    var deferredSavingData: Deferred<UserSavingResponse>? = null

    override fun getTokoPointDetail() {
        launchCatchError(block = {
            tokopointDetailLiveData.value = Loading()
            val graphqlResponse = repository.getTokoPointDetailData()
            val data = graphqlResponse.getData<RewardResponse>(RewardResponse::class.java)
            data?.let {
                if (data.tokopediaRewardTopSection?.isShowIntroActivity == true) getRewardIntroData()
            }
            val dataSection = graphqlResponse.getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java)
            if (data.tokopediaRewardTopSection?.isShowSavingPage == true) {
                deferredSavingData = async { getUserSavingData() }
            }
            if (data != null && dataSection != null && dataSection.sectionContent != null && data.tokopediaRewardTopSection != null) {
                tokopointDetailLiveData.value = Success(TokopointSuccess(TopSectionResponse(data.tokopediaRewardTopSection, deferredSavingData?.await()?.tokopointsUserSaving), dataSection.sectionContent.sectionContent))
            } else {
                throw NullPointerException("error in data")
            }
        }) {
            tokopointDetailLiveData.value = ErrorMessage(it.localizedMessage)
        }
    }

    fun getRewardIntroData() {
        launchCatchError(block = {
            val response = repository.getRewardIntroData()
            val data = response.getData<IntroResponse>(IntroResponse::class.java)
            rewardIntroData.value = Success(data)
        }) {
        }
    }

    private suspend fun getUserSavingData(): UserSavingResponse {
        val response = repository.getUserSavingData()
        return response.getData(UserSavingResponse::class.java)
    }
}

data class TokopointSuccess(val topSectionResponse: TopSectionResponse, val sectionList: MutableList<SectionContent>)
data class TopSectionResponse(val tokopediaRewardTopSection: TokopediaRewardTopSection, val userSavingResponse: TokopointsUserSaving?)
