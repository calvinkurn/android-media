package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedplus.data.pojo.onboarding.DataItem
import com.tokopedia.feedplus.data.pojo.onboarding.OnboardingData
import com.tokopedia.feedplus.data.pojo.onboarding.SubmitInterestResponse
import com.tokopedia.feedplus.view.di.RawQueryKeyConstant
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.SubmitInterestResponseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-09-18
 */
class FeedOnboardingViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                  private val graphqlRepository: GraphqlRepository,
                                                  private val rawQueries: Map<String, String>)
    : BaseViewModel(baseDispatcher) {

    companion object {
        val PARAM_SOURCE_RECOM_PROFILE_CLICK = "click_recom_profile"
        val PARAM_SOURCE_SEE_ALL_CLICK = "click_see_all"
    }

    val PARAM_SOURC = "source"
    val PARAM_ACTION = "action"
    val PARAM_INTEREST_ID = "interestID"

    val onboardingResp = MutableLiveData<Result<OnboardingViewModel>> ()

    val submitInterestPickResp = MutableLiveData<Result<SubmitInterestResponseViewModel>>()

    fun getOnboardingData(source: String, forceRefresh: Boolean) {
        launchCatchError(block = {
            val resultDeffered = loadOnboardingData(source)
            onboardingResp.value = Success(resultDeffered.await())
        }){
            it.printStackTrace()
            onboardingResp.value = Fail(it)
        }
    }

    fun submitInterestPickData(dataList: List<OnboardingDataViewModel>, source: String, requestInt: Int) {
        launchCatchError(block = {
            val result = submitInterestPickDeferred(dataList.map { it.id }, source, requestInt)
            submitInterestPickResp.value = Success(result.await())
        }) {
            it.printStackTrace()
            submitInterestPickResp.value = Fail(it)
        }
    }

    private suspend fun loadOnboardingData(source: String): Deferred<OnboardingViewModel> {
        return async(Dispatchers.IO) {
            var resultData = OnboardingViewModel()
            val param = mapOf(PARAM_SOURC to source)
            val request = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_ONBOARDING_INTEREST],
                    OnboardingData::class.java, param)

            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            val requests = mutableListOf(request)
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)
            if (gqlResponse.getError(OnboardingData::class.java)?.isNotEmpty() != true) {
                val result = (gqlResponse.getData(OnboardingData::class.java) as OnboardingData)
                resultData = mappingOnboardingData(result)
            }
            resultData
        }
    }

    private suspend fun submitInterestPickDeferred(idList: List<Int>, source: String, requestInt: Int): Deferred<SubmitInterestResponseViewModel> {
        return async(Dispatchers.IO) {
            var resultData = SubmitInterestResponseViewModel()
            resultData.source = source
            resultData.requestInt = requestInt
            resultData.idList = idList
            val param = mapOf(PARAM_ACTION to "", PARAM_INTEREST_ID to idList)
            val request = GraphqlRequest(rawQueries[RawQueryKeyConstant.MUTATION_SUBMIT_INTEREST_ID],
                    SubmitInterestResponse::class.java, param)
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            val requests = mutableListOf(request)
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)
            if (gqlResponse.getError(SubmitInterestResponse::class.java)?.isNotEmpty() != true) {
                val result = (gqlResponse.getData(SubmitInterestResponse::class.java) as SubmitInterestResponse)
                resultData.success = result.feedInterestUserUpdate.success
                resultData.error = result.feedInterestUserUpdate.error
            }
            resultData
        }
    }

    private fun mappingOnboardingData(pojo: OnboardingData): OnboardingViewModel {
        return OnboardingViewModel(
                pojo.feedUserOnboardingInterests.meta.isEnabled,
                pojo.feedUserOnboardingInterests.meta.minPicked,
                pojo.feedUserOnboardingInterests.meta.source,
                pojo.feedUserOnboardingInterests.meta.assets.titleIntro,
                pojo.feedUserOnboardingInterests.meta.assets.titleFull,
                pojo.feedUserOnboardingInterests.meta.assets.instruction,
                pojo.feedUserOnboardingInterests.meta.assets.buttonCta,
                mappingOnboardingListData(pojo.feedUserOnboardingInterests.data)
        )
    }

    private fun mappingOnboardingListData(pojoList: List<DataItem>) : MutableList<OnboardingDataViewModel> {
        val dataList: MutableList<OnboardingDataViewModel> = ArrayList()
        for (pojo in pojoList) {
            dataList.add(OnboardingDataViewModel(
                    pojo.id,
                    pojo.name,
                    pojo.image,
                    pojo.isSelected
            ))
        }
        return dataList
    }

}