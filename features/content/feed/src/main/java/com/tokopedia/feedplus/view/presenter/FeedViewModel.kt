package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.SendReportUseCase
import com.tokopedia.feedcomponent.domain.usecase.WHITELIST_INTEREST
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.AtcViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.TrackAffiliateViewModel
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.NON_LOGIN_USER_ID
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.interest_pick_common.data.DataItem
import com.tokopedia.interest_pick_common.data.FeedUserOnboardingInterests
import com.tokopedia.interest_pick_common.data.OnboardingData
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.domain.usecase.SubmitInterestPickUseCase
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.interest_pick_common.view.viewmodel.SubmitInterestResponseViewModel
import com.tokopedia.kolcommon.data.pojo.FollowKolDomain
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-09-18
 */

private const val PARAM_SHOP_DOMAIN = "shop_domain"
private const val PARAM_SRC = "src"
private const val PARAM_AD_KEY = "ad_key"
private const val DEFAULT_VALUE_SRC = "fav_shop"

class FeedViewModel @Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getInterestPickUseCase: GetInterestPickUseCase,
    private val submitInterestPickUseCase: SubmitInterestPickUseCase,
    private val doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
    private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
    private val likeKolPostUseCase: LikeKolPostUseCase,
    private val atcUseCase: AddToCartUseCase,
    private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val sendTopAdsUseCase: SendTopAdsUseCase,
    private val playWidgetTools: PlayWidgetTools,
    private val getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase,
    private val getWhitelistNewUseCase: GetWhitelistNewUseCase,
    private val sendReportUseCase: SendReportUseCase
) : BaseViewModel(baseDispatcher.main) {

    companion object {
        const val PARAM_SOURCE_RECOM_PROFILE_CLICK = "click_recom_profile"
        const val PARAM_SOURCE_SEE_ALL_CLICK = "click_see_all"
    }

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else NON_LOGIN_USER_ID

    val onboardingResp = MutableLiveData<Result<OnboardingViewModel>>()
    val submitInterestPickResp = MutableLiveData<Result<SubmitInterestResponseViewModel>>()
    val getFeedFirstPageResp = MutableLiveData<Result<DynamicFeedFirstPageDomainModel>>()
    val getFeedNextPageResp = MutableLiveData<Result<DynamicFeedDomainModel>>()
    val doFavoriteShopResp = MutableLiveData<Result<FeedPromotedShopViewModel>>()
    val followKolResp = MutableLiveData<Result<FollowKolViewModel>>()
    val followKolRecomResp = MutableLiveData<Result<FollowKolViewModel>>()
    val likeKolResp = MutableLiveData<Result<LikeKolViewModel>>()
    val deletePostResp = MutableLiveData<Result<DeletePostViewModel>>()
    val atcResp = MutableLiveData<Result<AtcViewModel>>()
    val toggleFavoriteShopResp = MutableLiveData<Result<FavoriteShopViewModel>>()
    val trackAffiliateResp = MutableLiveData<Result<TrackAffiliateViewModel>>()
    val reportResponse = MutableLiveData<Result<DeletePostViewModel>>()


    private val _playWidgetModel = MutableLiveData<Result<CarouselPlayCardViewModel>>()
    val playWidgetModel: LiveData<Result<CarouselPlayCardViewModel>>
        get() = _playWidgetModel

    private var currentCursor = ""
    private val pagingHandler: PagingHandler = PagingHandler()

    fun getOnboardingData(source: String) {
        if(userId != NON_LOGIN_USER_ID) {
            getInterestPickUseCase.apply {
                clearRequest()
                addRequestWithParam(source)
            }.execute({
                onboardingResp.value = Success(it.convertToViewModel())
            }, {
                onboardingResp.value = Fail(it)
            })
        }
    }

    fun sendReport(
        positionInFeed: Int,
        contentId: Int,
        reasonType: String,
        reasonMessage: String,
        contentType: String
    ) {
        sendReportUseCase.createRequestParams(contentId, reasonType, reasonMessage, contentType)
        sendReportUseCase.execute(
            {
                val deleteModel = DeletePostViewModel(
                    contentId,
                    positionInFeed,
                    it.feedReportSubmit.errorMessage,
                    true
                )
                if (it.feedReportSubmit.errorMessage.isEmpty()) {
                    reportResponse.value = Success(deleteModel)
                } else {
                    reportResponse.value = Fail(Exception(it.feedReportSubmit.errorMessage))
                }
            },
            {
                reportResponse.value = Fail(it)
            }
        )
    }

    fun submitInterestPickData(
        dataList: List<InterestPickDataViewModel>,
        source: String,
        requestInt: Int
    ) {
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

    fun getFeedFirstPage() {
        pagingHandler.resetPage()
        currentCursor = ""
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getFeedFirstDataResult()
            }
            currentCursor = results.dynamicFeedDomainModel.cursor
            getFeedFirstPageResp.value = Success(results)

            if (shouldGetPlayWidget(results.dynamicFeedDomainModel)) {
                try {
                    val newCarouselModel = processPlayWidget()
                    _playWidgetModel.value = Success(newCarouselModel)
                } catch (e: Throwable) {
                    _playWidgetModel.value = Fail(e)
                }
            }
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
            val results = withContext(baseDispatcher.io) {
                getFeedDataResult()
            }
            if (results.hasNext) {
                currentCursor = results.cursor
            }
            getFeedNextPageResp.value = Success(results)

            if (shouldGetPlayWidget(results)) {
                try {
                    val newCarouselModel = processPlayWidget()
                    _playWidgetModel.value = Success(newCarouselModel)
                } catch (e: Throwable) {
                    _playWidgetModel.value = Fail(e)
                }
            }
        }) {
            getFeedNextPageResp.value = Fail(it)
        }
    }

    fun doFavoriteShop(promotedShopViewModel: Data, adapterPosition: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
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
            val results = withContext(baseDispatcher.io) {
                followKol(id, rowNumber)
            }
            followKolResp.value = Success(results)
        }) {
            followKolResp.value = Fail(it)
        }
    }

    fun doUnfollowKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                unfollowKol(id, rowNumber)
            }
            followKolResp.value = Success(results)
        }) {
            followKolResp.value = Fail(it)
        }
    }

    fun doLikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                likeKol(id, rowNumber)
            }
            likeKolResp.value = Success(results)
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doUnlikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                unlikeKol(id, rowNumber)
            }
            likeKolResp.value = Success(results)
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doFollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                followKolFromRecom(id, rowNumber, position)
            }
            followKolRecomResp.value = Success(results)
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doUnfollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                unfollowKolFromRecom(id, rowNumber, position)
            }
            followKolRecomResp.value = Success(results)
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doDeletePost(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                deletePost(id, rowNumber)
            }
            deletePostResp.value = Success(results)
        }) {
            deletePostResp.value = Fail(it)
        }
    }

    fun doAtc(postTagItem: PostTagItem) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                atc(postTagItem)
            }
            atcResp.value = Success(results)
        }) {
            atcResp.value = Fail(it)
        }
    }

    fun doTrackAffiliate(url: String) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                trackAffiliate(url)
            }
            trackAffiliateResp.value = Success(results)
        }) {
        }
    }

    fun doToggleFavoriteShop(rowNumber: Int, adapterPosition: Int, shopId: String) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                toggleFavoriteShop(rowNumber, adapterPosition, shopId)
            }
            toggleFavoriteShopResp.value = Success(results)
        }) {
            toggleFavoriteShopResp.value = Fail(it)
        }
    }

    fun doTopAdsTracker(
        url: String,
        shopId: String,
        shopName: String,
        imageUrl: String,
        isClick: Boolean
    ) {
        if (isClick) {
            sendTopAdsUseCase.hitClick(url, shopId, shopName, imageUrl)
        } else {
            sendTopAdsUseCase.hitImpressions(url, shopId, shopName, imageUrl)
        }
    }

    fun doAutoRefreshPlayWidget() {

        launchCatchError(block = {
            val newCarouselModel = processPlayWidget(isAutoRefresh = true)
            _playWidgetModel.value = Success(newCarouselModel)
        }, onError = {
            _playWidgetModel.value = Fail(it)
        })
    }

    private fun OnboardingData.convertToViewModel(): OnboardingViewModel =
        mappingOnboardingData(feedUserOnboardingInterests)

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

    private fun mappingOnboardingListData(pojoList: List<DataItem>): MutableList<InterestPickDataViewModel> {
        val dataList: MutableList<InterestPickDataViewModel> = mutableListOf()
        for (pojo in pojoList) {
            dataList.add(
                InterestPickDataViewModel(
                    pojo.id,
                    pojo.name,
                    pojo.image,
                    pojo.isSelected
                )
            )
        }
        return dataList
    }

    private suspend fun getFeedFirstDataResult(): DynamicFeedFirstPageDomainModel {
        return try {
            val feedResponseModel = getFeedDataResult()
            if (userSession.isLoggedIn) {
                val whiteListModel = getWhitelistNewUseCase.execute(type = WHITELIST_INTEREST)
                DynamicFeedFirstPageDomainModel(
                    feedResponseModel,
                    (whiteListModel.whitelist.error.isEmpty() && whiteListModel.whitelist.isWhitelist)
                )
            } else {
                DynamicFeedFirstPageDomainModel(feedResponseModel, false)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private suspend fun getFeedDataResult(): DynamicFeedDomainModel {
        try {
            return getDynamicFeedNewUseCase.execute(cursor = currentCursor)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private fun doFavoriteShopResult(promotedShopViewModel: Data): FeedPromotedShopViewModel {
        try {
            val result = FeedPromotedShopViewModel()
            val params =
                ToggleFavouriteShopUseCase.createRequestParam(promotedShopViewModel.shop.id)

            params.putString(PARAM_SHOP_DOMAIN, promotedShopViewModel.shop.domain)
            params.putString(PARAM_SRC, DEFAULT_VALUE_SRC)
            params.putString(PARAM_AD_KEY, promotedShopViewModel.adRefKey)
            val requestSuccess =
                doFavoriteShopUseCase.createObservable(params).toBlocking().single()
            result.isSuccess = requestSuccess
            result.promotedShopViewModel = promotedShopViewModel
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
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) data.isSuccess =
                    true
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
            val params =
                FollowKolPostGqlUseCase.getParam(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) data.isSuccess =
                    true
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
            val params =
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike)
            val isSuccess = likeKolPostUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun followKolFromRecom(id: Int, rowNumber: Int, position: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.status = FollowKolPostGqlUseCase.PARAM_FOLLOW
            data.position = position
            data.rowNumber = rowNumber
            val params = FollowKolPostGqlUseCase.getParam(id, data.status)
            followKolPostGqlUseCase.clearRequest()
            followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, data.status))
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                    data.isSuccess = true
                    data.isFollow = true
                }
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unfollowKolFromRecom(id: Int, rowNumber: Int, position: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.status = FollowKolPostGqlUseCase.PARAM_UNFOLLOW
            data.position = position
            data.rowNumber = rowNumber
            val params = FollowKolPostGqlUseCase.getParam(id, data.status)
            followKolPostGqlUseCase.clearRequest()
            followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, data.status))
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                    data.isSuccess = true
                    data.isFollow = false
                }
            }
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
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun atc(postTagItem: PostTagItem): AtcViewModel {
        try {
            val data = AtcViewModel()
            data.applink = postTagItem.applink
            if (postTagItem.shop.isNotEmpty()) {
                val params = AddToCartUseCase.getMinimumParams(
                    postTagItem.id, postTagItem.shop[0].shopId,
                    productName = postTagItem.text, price = postTagItem.price, userId = userId
                )
                val result = atcUseCase.createObservable(params).toBlocking().single()
                data.isSuccess = result.data.success == 1
                if (result.isStatusError()) {
                    data.errorMsg = result.errorMessage.firstOrNull() ?: ""
                }
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun toggleFavoriteShop(
        rowNumber: Int,
        adapterPosition: Int,
        shopId: String
    ): FavoriteShopViewModel {
        try {
            val data = FavoriteShopViewModel()
            data.rowNumber = rowNumber
            data.adapterPosition = adapterPosition
            data.shopId = shopId
            val params = ToggleFavouriteShopUseCase.createRequestParam(shopId)
            val isSuccess = doFavoriteShopUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
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
        } catch (e: Throwable) {
            throw e
        }
    }

    /**
     * Play Widget
     */
    fun updatePlayWidgetTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return

        val currentValue = _playWidgetModel.value
        if (currentValue is Success) {
            val model = currentValue.data.playWidgetUiModel
            _playWidgetModel.value = Success(
                data = currentValue.data.copy(
                    playWidgetUiModel = playWidgetTools.updateTotalView(model, channelId, totalView)
                )
            )
        }
    }

    private fun shouldGetPlayWidget(model: DynamicFeedDomainModel): Boolean {
        return model.postList.any { it is CarouselPlayCardViewModel }
    }

    private suspend fun processPlayWidget(isAutoRefresh: Boolean = false): CarouselPlayCardViewModel {
        val response = playWidgetTools.getWidgetFromNetwork(
            widgetType = PlayWidgetUseCase.WidgetType.Feeds,
            coroutineContext = baseDispatcher.io
        )
        val uiModel = playWidgetTools.mapWidgetToModel(response)
        return CarouselPlayCardViewModel(uiModel, isAutoRefresh)
    }
}