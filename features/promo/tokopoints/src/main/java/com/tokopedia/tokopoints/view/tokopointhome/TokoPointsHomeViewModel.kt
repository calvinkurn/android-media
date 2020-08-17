package com.tokopedia.tokopoints.view.tokopointhome

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedeemptionViewModel
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.json.JSONObject
import java.lang.NullPointerException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@TokoPointScope
class TokoPointsHomeViewModel @Inject constructor(private val repository: TokopointsHomeRepository, private val topAdsImageViewUseCase: TopAdsImageViewUseCase) : CatalogPurchaseRedeemptionViewModel(repository), TokoPointsHomeContract.Presenter {

    val tokopointDetailLiveData = MutableLiveData<Resources<TokopointSuccess>>()
    val rewardIntroData = MutableLiveData<Resources<IntroResponse>>()
    val topAdsBannerData = MutableLiveData<TopAdsImageViewModel?>()

    companion object {
        const val ITEM = "item"
        const val INVENTORY_ID = "inventory_id"
    }

    override fun getTokoPointDetail() {
        launchCatchError(block = {
            tokopointDetailLiveData.value = Loading()
            val graphqlResponse = repository.getTokoPointDetailData()
            val data = graphqlResponse.getData<RewardResponse>(RewardResponse::class.java)
            data?.let {
                if (data.tokopediaRewardTopSection?.isShowIntroActivity == true) getRewardIntroData()
            }
            val dataSection = graphqlResponse.getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java)
            var queryString = ""
            for (item in dataSection.sectionContent.sectionContent) {
                if (item.layoutTopAdsAttr != null && item.layoutTopAdsAttr.jsonTopAdsDisplayParam.isNotEmpty()) {
                    queryString = item.layoutTopAdsAttr.jsonTopAdsDisplayParam
                    break
                }
            }

            if (queryString.isNotEmpty()) {
                val result = getTopadsBanner(queryString).await()

                if (result.isNotEmpty()) {
                    val stack: Stack<TopAdsImageViewModel> = Stack()
                    stack.addAll(result.toMutableList())
                    stack.reverse()
                    topAdsBannerData.value = stack.pop()
                }
            }

            if (data != null && dataSection != null && dataSection.sectionContent != null) {
                tokopointDetailLiveData.value = Success(TokopointSuccess(data.tokopediaRewardTopSection, dataSection.sectionContent.sectionContent, topAdsBannerData.value))
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

    fun getTopadsBanner(topadsMap: String): Deferred<ArrayList<TopAdsImageViewModel>> {
        return async {
            val jObject = JSONObject(topadsMap)
            topAdsImageViewUseCase.getImageData(
                    topAdsImageViewUseCase.getQueryMap(
                            "",
                            jObject.getString(INVENTORY_ID),
                            "",
                            jObject.getInt(ITEM),
                            7,
                            ""
                    )
            )

        }
    }
}

data class TokopointSuccess(val tokoPointEntity: TokopediaRewardTopSection?, val sectionList: MutableList<SectionContent>, val topAdsImageViewModel: TopAdsImageViewModel?)
