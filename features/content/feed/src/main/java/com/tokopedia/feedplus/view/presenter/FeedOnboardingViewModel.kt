package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedplus.NON_LOGIN_USER_ID
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.interest_pick_common.data.DataItem
import com.tokopedia.interest_pick_common.data.OnboardingData
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.interest_pick_common.view.viewmodel.SubmitInterestResponseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.interest_pick_common.data.FeedUserOnboardingInterests
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.domain.usecase.SubmitInterestPickUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-09-18
 */
class FeedOnboardingViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                  private val graphqlRepository: GraphqlRepository,
                                                  private val userSession: UserSessionInterface,
                                                  private val getInterestPickUseCase: GetInterestPickUseCase,
                                                  private val submitInterestPickUseCase: SubmitInterestPickUseCase,
                                                  private val getDynamicFeedFirstPageUseCase: GetDynamicFeedFirstPageUseCase,
                                                  private val rawQueries: Map<String, String>)
    : BaseViewModel(baseDispatcher) {

    companion object {
        val PARAM_SOURCE_RECOM_PROFILE_CLICK = "click_recom_profile"
        val PARAM_SOURCE_SEE_ALL_CLICK = "click_see_all"
    }

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else NON_LOGIN_USER_ID

    val onboardingResp = MutableLiveData<Result<OnboardingViewModel>> ()
    val submitInterestPickResp = MutableLiveData<Result<SubmitInterestResponseViewModel>>()
    val getFeedFirstPageResp = MutableLiveData<Result<DynamicFeedFirstPageDomainModel>>()
    private var currentCursor = ""
    private val pagingHandler: PagingHandler

    init {
        this.pagingHandler = PagingHandler()
    }

    fun getOnboardingData(source: String, forceRefresh: Boolean) {
        getInterestPickUseCase.apply {
            clearRequest()
            addRequestWithParam(source)
        }.execute({
            onboardingResp.value = Success(it.convertToViewModel())
        }, {
            onboardingResp.value = Fail(it)
        })
    }

    fun submitInterestPickData(dataList: List<InterestPickDataViewModel>, source: String, requestInt: Int) {
        val idList = dataList.map { it.id }
        submitInterestPickUseCase.apply {
            clearRequest()
            addRequestWithParam(idList)
        }.execute({
            val resultData = SubmitInterestResponseViewModel()
            resultData.source = source
            resultData.requestInt = requestInt
            resultData.idList = idList
            resultData.success = it.feedInterestUserUpdate.success
            resultData.error = it.feedInterestUserUpdate.error
            submitInterestPickResp.value = Success(resultData)
        }, {
            submitInterestPickResp.value = Fail(it)
        })
    }

    fun getFirstFeedPage(firstPageCursor: String) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                getFeedDataResult(firstPageCursor)
            }
            currentCursor = results.dynamicFeedDomainModel.cursor
            getFeedFirstPageResp.value = Success(results)
        }) {
            getFeedFirstPageResp.value = Fail(it)
        }
    }

    private fun OnboardingData.convertToViewModel(): OnboardingViewModel = feedUserOnboardingInterests.let { result ->
        mappingOnboardingData(result)
    }

    private fun mappingOnboardingData(pojo: FeedUserOnboardingInterests): OnboardingViewModel {
        return OnboardingViewModel(
                pojo.meta.isEnabled,
                pojo.meta.minPicked,
                pojo.meta.source,
                pojo.meta.assets.titleIntro,
                pojo.meta.assets.titleFull,
                pojo.meta.assets.instruction,
                pojo.meta.assets.buttonCta,
                mappingOnboardingListData(pojo.data)
        )
    }

    private fun mappingOnboardingListData(pojoList: List<DataItem>) : MutableList<InterestPickDataViewModel> {
        val dataList: MutableList<InterestPickDataViewModel> = mutableListOf()
        for (pojo in pojoList) {
            dataList.add(InterestPickDataViewModel(
                    pojo.id,
                    pojo.name,
                    pojo.image,
                    pojo.isSelected
            ))
        }
        return dataList
    }

    private fun getFeedDataResult(firstPageCursor: String): DynamicFeedFirstPageDomainModel {
        try {
            val request = getDynamicFeedFirstPageUseCase.createObservable(GetDynamicFeedFirstPageUseCase.createRequestParams(
                    userId,
                    "",
                    GetDynamicFeedUseCase.FeedV2Source.Feeds,
                    firstPageCursor,
                    userSession.isLoggedIn))
                    .toBlocking().single()?: DynamicFeedFirstPageDomainModel()
            return request
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

}