package com.tokopedia.tokopoints.view.tokopointhome

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.notification.PopupNotifUsecase
import com.tokopedia.tokopoints.notification.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.model.homeresponse.TokopointSuccess
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.RewardTickerListResponse
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingResponse
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommUsecase
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@TokoPointScope
class TokoPointsHomeViewModel @Inject constructor(
    private val tokopointsHomeUsecase: TokopointsHomeUsecase,
    private val recommUsecase: RewardsRecommUsecase,
    private val popupNotifUsecase: PopupNotifUsecase
) : BaseViewModel(Dispatchers.Main), TokoPointsHomeContract.Presenter {

    val tokopointDetailLiveData = MutableLiveData<Resources<TokopointSuccess>>()
    val rewardIntroData = MutableLiveData<Resources<IntroResponse>>()
    var defferedPopUpNotifData : Deferred<TokoPointDetailEntity>? = null
    var deferredSavingData: Deferred<UserSavingResponse>? = null
    var defferedRecomData: Deferred<RewardsRecommendation>? = null
    var defferedRewardTickerResponse: Deferred<RewardTickerListResponse>? = null
    val PAGE_NAME = "rewards_page"
    val PAGE_NUMBER = 1
    val recommIndex = 0
    val TOKOPOINT_DRAWER = "drawer"

    override fun getTokoPointDetail() {
        launchCatchError(block = {
            tokopointDetailLiveData.value = Loading()
            val graphqlResponse = tokopointsHomeUsecase.getTokoPointDetailData()
            val data = graphqlResponse.getData<RewardResponse>(RewardResponse::class.java)
            val dataSection =
                graphqlResponse.getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java)
            data?.let {
                if (data.tokopediaRewardTopSection?.isShowIntroActivity == true) {
                    getRewardIntroData()
                }
            }
            if (data.tokopediaRewardTopSection?.isShowSavingPage == true) {
                deferredSavingData = getUserSavingData()
            }
            defferedRewardTickerResponse = getStatusMatchingData()
            defferedPopUpNotifData = getPopNotifData()
            defferedRecomData = getRecommendationData()
            if (data != null && dataSection != null && dataSection.sectionContent != null &&
                data.tokopediaRewardTopSection != null
            ) {
                tokopointDetailLiveData.value = Success(
                    TokopointSuccess(
                        TopSectionResponse(
                            data.tokopediaRewardTopSection,
                            deferredSavingData?.await()?.tokopointsUserSaving, defferedRewardTickerResponse?.await(),
                            defferedPopUpNotifData?.await()
                        ), dataSection.sectionContent.sectionContent, defferedRecomData?.await()
                    )
                )
            } else {
                throw NullPointerException("error in data")
            }
        }) {
            tokopointDetailLiveData.value = ErrorMessage(it.localizedMessage)
        }
    }

    fun getRewardIntroData() {
        launchCatchError(block = {
            val response = tokopointsHomeUsecase.getRewardIntroData()
            val data = response.getData<IntroResponse>(IntroResponse::class.java)
            rewardIntroData.value = Success(data)
        }) {
        }
    }

    fun getPopNotifData(): Deferred<TokoPointDetailEntity>{
        var tokopointDetailEntity  = TokoPointDetailEntity()
        return async(Dispatchers.IO) {
            try {
                val  response = popupNotifUsecase.getPopupNotif(TOKOPOINT_DRAWER)
                tokopointDetailEntity= response.getData(TokoPointDetailEntity::class.java)
            }catch (e: Exception){}
            tokopointDetailEntity
        }
    }

    private suspend fun getRecommendationData(): Deferred<RewardsRecommendation> {
        return async(Dispatchers.IO) {
            var recommendation = RewardsRecommendation(listOf(), "", "")
            try {
                val response = recommUsecase.getData(recommUsecase.getRequestParams(PAGE_NUMBER,PAGE_NAME))
                val recomWidget = response[recommIndex]
                val title = recomWidget.title
                val appLink = recomWidget.seeMoreAppLink
                val list = recommUsecase.mapper.recommWidgetToListOfVisitables(recomWidget)
                recommendation = RewardsRecommendation(list, title, appLink)
            } catch (e: Exception) {
            }
            recommendation
        }
    }

    private suspend fun getUserSavingData(): Deferred<UserSavingResponse> {
        return async(Dispatchers.IO) {
            var userSavingResponse = UserSavingResponse()
            try {
                val response = tokopointsHomeUsecase.getUserSavingData()
                userSavingResponse = response.getData(UserSavingResponse::class.java)
            } catch (e: Exception) {
            }
            userSavingResponse
        }
    }

    private suspend fun getStatusMatchingData(): Deferred<RewardTickerListResponse> {
        return async(Dispatchers.IO) {
            var rewardTickerResponse = RewardTickerListResponse()
            try {
                val response = tokopointsHomeUsecase.getUserStatusMatchingData()
                rewardTickerResponse = response.getData(RewardTickerListResponse::class.java)
            } catch (e: Exception) {
            }
            rewardTickerResponse
        }
    }
}
