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
    var deferredSavingData: Deferred<UserSavingResponse>? = null
    var recomData: Deferred<RewardsRecommendation>? = null


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
                deferredSavingData = async(Dispatchers.IO) { getUserSavingData() }
            }
            recomData = async(Dispatchers.IO) {  getRecommendationData()}
            val recomReponseData = recomData?.await()
            if (data != null && dataSection != null && dataSection.sectionContent != null && data.tokopediaRewardTopSection != null) {
                tokopointDetailLiveData.value = Success(TokopointSuccess(TopSectionResponse(data.tokopediaRewardTopSection,
                        deferredSavingData?.await()?.tokopointsUserSaving), dataSection.sectionContent.sectionContent, recomReponseData))
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

    private suspend fun getRecommendationData(): RewardsRecommendation {
        val response = recommUsecase.getData(recommUsecase.getRequestParams(1, "", ""))
        val recomWidget = response.first()
        val title = recomWidget.title
        val appLink = recomWidget.seeMoreAppLink
        val list = recommUsecase.mapper.recommWidgetToListOfVisitables(recomWidget)
        return RewardsRecommendation(list,title,appLink)
    }

    private suspend fun getUserSavingData(): UserSavingResponse {
        val response = tokopointsHomeUsecase.getUserSavingData()
        return response.getData(UserSavingResponse::class.java)
    }
}

data class TokopointSuccess(val topSectionResponse: TopSectionResponse, val sectionList: MutableList<SectionContent>, val recomData: RewardsRecommendation?)
data class TopSectionResponse(val tokopediaRewardTopSection: TokopediaRewardTopSection, val userSavingResponse: TokopointsUserSaving?)
data class RewardsRecommendation(val recommendationWrapper: List<RecommendationWrapper> , val title:String , val appLink:String)
data class RecommendationWrapper( val recomendationItem : RecommendationItem,val recomData: ProductCardModel)