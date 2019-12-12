package com.tokopedia.feedplus.view.presenter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.AtcViewModel
import com.tokopedia.feedplus.NON_LOGIN_USER_ID
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.interest_pick_common.data.DataItem
import com.tokopedia.interest_pick_common.data.OnboardingData
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.interest_pick_common.view.viewmodel.SubmitInterestResponseViewModel
import com.tokopedia.interest_pick_common.data.FeedUserOnboardingInterests
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.domain.usecase.SubmitInterestPickUseCase
import com.tokopedia.kolcommon.data.pojo.FollowKolDomain
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.TrackAffiliateViewModel
import com.tokopedia.feedplus.view.viewmodel.VoteViewModel
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.usecase.SendVoteUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-09-18
 */
class FeedViewModel @Inject constructor(@ApplicationContext private val context: Context,
                                        baseDispatcher: CoroutineDispatcher,
                                        private val userSession: UserSessionInterface,
                                        private val getInterestPickUseCase: GetInterestPickUseCase,
                                        private val submitInterestPickUseCase: SubmitInterestPickUseCase,
                                        private val getDynamicFeedFirstPageUseCase: GetDynamicFeedFirstPageUseCase,
                                        private val getDynamicFeedUseCase: GetDynamicFeedUseCase,
                                        private val doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
                                        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
                                        private val likeKolPostUseCase: LikeKolPostUseCase,
                                        private val sendVoteUseCase: SendVoteUseCase,
                                        private val atcUseCase: AddToCartUseCase,
                                        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
                                        private val deletePostUseCase: DeletePostUseCase)
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
    val getFeedNextPageResp = MutableLiveData<Result<DynamicFeedDomainModel>>()
    val doFavoriteShopResp = MutableLiveData<Result<FeedPromotedShopViewModel>>()
    val followKolResp = MutableLiveData<Result<FollowKolViewModel>>()
    val followKolRecomResp = MutableLiveData<Result<FollowKolViewModel>>()
    val likeKolResp = MutableLiveData<Result<LikeKolViewModel>>()
    val voteResp = MutableLiveData<Result<VoteViewModel>>()
    val deletePostResp = MutableLiveData<Result<DeletePostViewModel>>()
    val atcResp = MutableLiveData<Result<AtcViewModel>>()
    val toggleFavoriteShopResp = MutableLiveData<Result<FavoriteShopViewModel>>()
    val trackAffiliateResp = MutableLiveData<Result<TrackAffiliateViewModel>>()

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

    fun getFeedFirstPage(firstPageCursor: String) {
        pagingHandler.resetPage()
        currentCursor = ""
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

    fun getFeedNextPage() {
        pagingHandler.nextPage()
        if (currentCursor.isEmpty()) {
            return
        }
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                getFeedNextDataResult()
            }
            if (results.hasNext) {
                currentCursor = results.cursor
            }
            getFeedNextPageResp.value = Success(results)
        }) {
            getFeedNextPageResp.value = Fail(it)
        }
    }

    fun doFavoriteShop(promotedShopViewModel: Data, adapterPosition: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                doFavoriteShopResult(promotedShopViewModel)
            }
            results.adapterPosition = adapterPosition
            doFavoriteShopResp.value = Success(results)
        }) {
            doFavoriteShopResp.value = Fail(it)
        }
    }

    fun doFollowKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                followKol(id, rowNumber)
            }
            followKolResp.value = Success(results)
        }) {
            followKolResp.value = Fail(it)
        }
    }

    fun doUnfollowKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                unfollowKol(id, rowNumber)
            }
            followKolResp.value = Success(results)
        }) {
            followKolResp.value = Fail(it)
        }
    }

    fun doLikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                likeKol(id, rowNumber)
            }
            likeKolResp.value = Success(results)
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doUnlikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                unlikeKol(id, rowNumber)
            }
            likeKolResp.value = Success(results)
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doFollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                followKolFromRecom(id, rowNumber, position)
            }
            followKolRecomResp.value = Success(results)
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doUnfollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                unfollowKolFromRecom(id, rowNumber, position)
            }
            followKolRecomResp.value = Success(results)
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doDeletePost(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                deletePost(id, rowNumber)
            }
            deletePostResp.value = Success(results)
        }) {
            deletePostResp.value = Fail(it)
        }
    }

    fun doVote(rowNumber: Int, pollId: String, optionId: String) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                vote(optionId, pollId, rowNumber)
            }
            voteResp.value = Success(results)
        }) {
            voteResp.value = Fail(it)
        }
    }

    fun doAtc(postTagItem: PostTagItem) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                atc(postTagItem)
            }
            atcResp.value = Success(results)
        }) {
            atcResp.value = Fail(it)
        }
    }

    fun doTrackAffiliate(url: String) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                trackAffiliate(url)
            }
            trackAffiliateResp.value = Success(results)
        }) {
            trackAffiliateResp.value = Fail(it)
        }
    }

    fun doToggleFavoriteShop(rowNumber: Int, adapterPosition: Int, shopId: String) {
        launchCatchError(block = {
            val results = withContext(Dispatchers.IO) {
                toggleFavoriteShop(rowNumber, adapterPosition, shopId)
            }
            toggleFavoriteShopResp.value = Success(results)
        }) {
            toggleFavoriteShopResp.value = Fail(it)
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
            val request = getDynamicFeedFirstPageUseCase.createObservable(
                    GetDynamicFeedFirstPageUseCase.createRequestParams(
                            userId,
                            "",
                            GetDynamicFeedUseCase.FeedV2Source.Feeds,
                            firstPageCursor, userSession.isLoggedIn))
                    .toBlocking().single()?: DynamicFeedFirstPageDomainModel()
            return request
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private fun getFeedNextDataResult(): DynamicFeedDomainModel{
        try {
            val request =  getDynamicFeedUseCase.createObservable(
                    GetDynamicFeedUseCase.createRequestParams(
                            userId,
                            currentCursor,
                            GetDynamicFeedUseCase.FeedV2Source.Feeds))
                    .toBlocking().single()?: DynamicFeedDomainModel()
            return request
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private fun doFavoriteShopResult(promotedShopViewModel: Data): FeedPromotedShopViewModel {
        try {
            val result = FeedPromotedShopViewModel()
            val PARAM_SHOP_DOMAIN = "shop_domain"
            val PARAM_SRC = "src"
            val PARAM_AD_KEY = "ad_key"
            val DEFAULT_VALUE_SRC = "fav_shop"
            val params = ToggleFavouriteShopUseCase.createRequestParam(promotedShopViewModel.shop.id)

            params.putString(PARAM_SHOP_DOMAIN, promotedShopViewModel.shop.domain)
            params.putString(PARAM_SRC, DEFAULT_VALUE_SRC)
            params.putString(PARAM_AD_KEY, promotedShopViewModel.adRefKey)
            val requestSuccess = doFavoriteShopUseCase.createObservable(params).toBlocking().single()
            val stringBuilder = StringBuilder()
            if (requestSuccess) {
                stringBuilder.append(MethodChecker.fromHtml(promotedShopViewModel.shop.name)).append(" ")
                if (promotedShopViewModel.isFavorit) {
                    stringBuilder.append(context.getString(R.string.shop_success_unfollow))
                } else {
                    stringBuilder.append(context.getString(R.string.shop_success_follow))
                }
            } else {
                stringBuilder.append(context.getString(R.string.msg_network_error))
            }
            result.promotedShopViewModel = promotedShopViewModel
            result.resultString = stringBuilder.toString()
            return result
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun followKol(id: Int, rowNumber: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            data.status = FollowKolPostGqlUseCase.PARAM_FOLLOW
            followKolPostGqlUseCase.clearRequest()
            val params = FollowKolPostGqlUseCase.getParam(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.getData() != null) {
                val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) data.isSuccess = true
                else {
                    data.errorMessage = context.getString(R.string
                            .default_request_error_unknown)
                }
            } else {
                data.errorMessage = ErrorHandler.getErrorMessage(context, Throwable())
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unfollowKol(id: Int, rowNumber: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            data.status = FollowKolPostGqlUseCase.PARAM_UNFOLLOW
            followKolPostGqlUseCase.clearRequest()
            val params = FollowKolPostGqlUseCase.getParam(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.getData() != null) {
                val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) data.isSuccess = true
                else {
                    data.errorMessage = context.getString(R.string
                            .default_request_error_unknown)
                }
            } else {
                data.errorMessage = ErrorHandler.getErrorMessage(context, Throwable())
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun likeKol(id: Int, rowNumber: Int): LikeKolViewModel {
        try {
            val data = LikeKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            val params = LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like)
            val isSuccess = likeKolPostUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            if (!isSuccess) {
                data.errorMessage = context.getString(R.string.default_request_error_unknown)
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unlikeKol(id: Int, rowNumber: Int): LikeKolViewModel {
        try {
            val data = LikeKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            val params = LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike)
            val isSuccess = likeKolPostUseCase.createObservable(params).toBlocking().single()
            data.isSuccess = isSuccess
            if (!isSuccess) {
                data.errorMessage = context.getString(R.string.default_request_error_unknown)
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun followKolFromRecom(id: Int, rowNumber: Int, position: Int): FollowKolViewModel{
        try {
            val data = FollowKolViewModel()
            data.status = FollowKolPostGqlUseCase.PARAM_FOLLOW
            data.position = position
            data.rowNumber = rowNumber
            val params = FollowKolPostGqlUseCase.getParam(id, data.status)
            followKolPostGqlUseCase.clearRequest()
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.getData() != null) {
                val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                    data.isSuccess = true
                    data.isFollow = true
                }
                else {
                    data.errorMessage = context.getString(R.string
                            .default_request_error_unknown)
                }
            } else {
                data.errorMessage = ErrorHandler.getErrorMessage(context, Throwable())
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unfollowKolFromRecom(id: Int, rowNumber: Int, position: Int): FollowKolViewModel{
        try {
            val data = FollowKolViewModel()
            data.status = FollowKolPostGqlUseCase.PARAM_UNFOLLOW
            data.position = position
            data.rowNumber = rowNumber
            val params = FollowKolPostGqlUseCase.getParam(id, data.status)
            followKolPostGqlUseCase.clearRequest()
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.getData() != null) {
                val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                    data.isSuccess = true
                    data.isFollow = false
                }
                else {
                    data.errorMessage = context.getString(R.string
                            .default_request_error_unknown)
                }
            } else {
                data.errorMessage = ErrorHandler.getErrorMessage(context, Throwable())
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun vote(optionId: String, pollId: String, rowNumber: Int): VoteViewModel {
        try {
            val data = VoteViewModel()
            data.optionId = optionId
            data.rowNumber = rowNumber
            val params = SendVoteUseCase.createParamsV1(pollId, optionId)
            val response = sendVoteUseCase.createObservable(params).toBlocking().single()
            data.voteModel = response
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun deletePost(id: Int, rowNumber: Int): DeletePostViewModel {
        try {
            val data = DeletePostViewModel()
            data.id = id
            data.rowNumber = rowNumber
            val params = DeletePostUseCase.createRequestParams(id.toString())
            val isSuccess = deletePostUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            if (!isSuccess) {
                data.errorMessage = context.getString(R.string.default_request_error_unknown)
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun atc(postTagItem: PostTagItem): AtcViewModel {
        try {
            val data = AtcViewModel()
            data.applink = postTagItem.applink
            if (!postTagItem.shop.isEmpty()) {
                val params = AddToCartUseCase.getMinimumParams(postTagItem.id, postTagItem.shop[0].shopId)
                val result = atcUseCase.createObservable(params).toBlocking().single()
                data.isSuccess = result.data.success == 0
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun toggleFavoriteShop(rowNumber: Int, adapterPosition: Int, shopId: String): FavoriteShopViewModel {
       try {
           val data = FavoriteShopViewModel()
           data.rowNumber = rowNumber
           data.adapterPosition = adapterPosition
           data.shopId = shopId
           val params = ToggleFavouriteShopUseCase.createRequestParam(shopId)
           val isSuccess = doFavoriteShopUseCase.createObservable(params).toBlocking().first()
           if (isSuccess) {
               data.isSuccess = isSuccess
           } else {
               data.errorMessage = ErrorHandler.getErrorMessage(context, RuntimeException())
           }
           return data
       } catch (e: Throwable) {
            throw e
       }
    }

    private fun trackAffiliate(url: String): TrackAffiliateViewModel {
        try {
            val data = TrackAffiliateViewModel()
            data.url = url
            val params = TrackAffiliateClickUseCase.createRequestParams(url)
            val isSuccess = trackAffiliateClickUseCase.createObservable(params).toBlocking().first()
            if (isSuccess) {
                data.isSuccess = isSuccess
            }
            return data
        } catch(e: Throwable) {
            throw e
        }
    }
}