package com.tokopedia.feedplus.view.presenter

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedplus.data.pojo.onboarding.DataItem
import com.tokopedia.feedplus.data.pojo.onboarding.OnboardingData
import com.tokopedia.feedplus.view.di.RawQueryKeyConstant
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
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

    val PARAM_SOURC = "source"

    val onboardingResp = MutableLiveData<Result<OnboardingViewModel>> ()

    fun getOnboardingData(source: String, forceRefresh: Boolean) {
        launchCatchError(block = {
            val resultDeffered = loadOnboardingData(source, forceRefresh)
            onboardingResp.value = Success(resultDeffered.await())
        }){
            it.printStackTrace()
            onboardingResp.value = Fail(it)
        }
    }
    private suspend fun loadOnboardingData(source: String, forceRefresh: Boolean): Deferred<OnboardingViewModel> {
        return async(Dispatchers.IO) {
            var resultData = OnboardingViewModel()
            val param = mapOf(PARAM_SOURC to source)
            val request = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_ONBOARDING_INTEREST],
                    OnboardingData::class.java, param)

            val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
            val requests = mutableListOf(request)
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)
            if (gqlResponse.getError(OnboardingData::class.java)?.isNotEmpty() != true) {
                val result = (gqlResponse.getData(OnboardingData::class.java) as OnboardingData)
                resultData = mappingOnboardingData(result)
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