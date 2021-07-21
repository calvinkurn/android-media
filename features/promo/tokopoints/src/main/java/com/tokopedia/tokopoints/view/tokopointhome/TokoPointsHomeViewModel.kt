package com.tokopedia.tokopoints.view.tokopointhome

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.RewardTickerResponse
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.model.usersaving.TokopointsUserSaving
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
class TokoPointsHomeViewModel @Inject constructor(private val tokopointsHomeUsecase: TokopointsHomeUsecase, private val recommUsecase: RewardsRecommUsecase)
    : BaseViewModel(Dispatchers.Main), TokoPointsHomeContract.Presenter {

    val tokopointDetailLiveData = MutableLiveData<Resources<TokopointSuccess>>()
    val rewardIntroData = MutableLiveData<Resources<IntroResponse>>()
    var deferredSavingData: UserSavingResponse? = null
    var recomData: RewardsRecommendation? = null
    var rewardTickerResponse : RewardTickerResponse? = null

    override fun getTokoPointDetail() {
        launchCatchError(block = {
            tokopointDetailLiveData.value = Loading()
            val graphqlResponse = tokopointsHomeUsecase.getTokoPointDetailData()
            val data = graphqlResponse.getData<RewardResponse>(RewardResponse::class.java)
            data?.let {
                if (data.tokopediaRewardTopSection?.isShowIntroActivity == true) getRewardIntroData()
            }
            val dataSection = graphqlResponse.getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java)
            if (data.tokopediaRewardTopSection?.isShowSavingPage == true) {
                deferredSavingData = getUserSavingData().await()
            }
            rewardTickerResponse = getStatusMatchingData().await()
            recomData = getRecommendationData().await()
            if (data != null && dataSection != null && dataSection.sectionContent != null && data.tokopediaRewardTopSection != null) {
                tokopointDetailLiveData.value = Success(TokopointSuccess(TopSectionResponse(data.tokopediaRewardTopSection,
                    deferredSavingData?.tokopointsUserSaving, rewardTickerResponse), dataSection.sectionContent.sectionContent,recomData))
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

    private suspend fun getRecommendationData(): Deferred<RewardsRecommendation> {
        return async(Dispatchers.IO) {
            var recommendation = RewardsRecommendation(listOf(), "", "")
            try {
                val response = recommUsecase.getData(recommUsecase.getRequestParams(1, "", ""))
                val recomWidget = response.first()
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

    private suspend fun getStatusMatchingData(): Deferred<RewardTickerResponse> {
        return async(Dispatchers.IO) {
            var rewardTickerResponse = RewardTickerResponse()
            try {
                val response = tokopointsHomeUsecase.getUserStatusMatchingData()
                rewardTickerResponse = response.getData(RewardTickerResponse::class.java)
            } catch (e: Exception) {
            }
            rewardTickerResponse
        }
    }
}

data class TokopointSuccess(val topSectionResponse: TopSectionResponse, val sectionList: MutableList<SectionContent>, val recomData: RewardsRecommendation?)
data class TopSectionResponse(val tokopediaRewardTopSection: TokopediaRewardTopSection, val userSavingResponse: TokopointsUserSaving?, val rewardTickerResponse: RewardTickerResponse?)
data class RewardsRecommendation(val recommendationWrapper: List<RecommendationWrapper> , val title:String , val appLink:String)
data class RecommendationWrapper( val recomendationItem : RecommendationItem,val recomData: ProductCardModel)