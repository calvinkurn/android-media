package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.officialstore.TopAdsHeadlineConstant.PAGE
import com.tokopedia.officialstore.TopAdsHeadlineConstant.SEEN_ADS
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.handleResult
import com.tokopedia.officialstore.official.data.mapper.OfficialStoreDynamicChannelComponentMapper
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsHeadlineDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationWithTopAdsHeadline
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.VALUE_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.VALUE_ITEM
import com.tokopedia.topads.sdk.utils.VALUE_TEMPLATE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfficialStoreHomeViewModel @Inject constructor(
        private val getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase,
        private val getOfficialStoreBenefitUseCase: GetOfficialStoreBenefitUseCase,
        private val getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase,
        private val getOfficialStoreDynamicChannelUseCase: GetOfficialStoreDynamicChannelUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
        private val getDisplayHeadlineAds: GetDisplayHeadlineAds,
        private val getRecommendationUseCaseCoroutine: com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase,
        private val bestSellerMapper: BestSellerMapper,
        private val getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    var currentSlug: String = ""
        private set
    var currentSlugDC: String = ""
        private set

    var isFeaturedShopAllowed: Boolean = false
        private set

    val impressedShop = mutableMapOf<String, MutableSet<String>>()

    //Pair first -> should show error message
    //Pair second -> official store banner value
    val officialStoreBannersResult: LiveData<Pair<Boolean, Result<OfficialStoreBanners>>>
        get() = _officialStoreBannersResult

    val officialStoreBenefitsResult: LiveData<Result<OfficialStoreBenefits>>
        get() = _officialStoreBenefitResult

    val officialStoreFeaturedShopResult: LiveData<Result<OfficialStoreFeaturedShop>>
        get() = _officialStoreFeaturedShopResult

    val officialStoreDynamicChannelResult: LiveData<Result<List<OfficialStoreChannel>>>
        get() = _officialStoreDynamicChannelResult

    val topAdsWishlistResult: LiveData<Result<WishlistModel>>
        get() = _topAdsWishlistResult

    private val _officialStoreBannersResult by lazy {
        MutableLiveData<Pair<Boolean, Result<OfficialStoreBanners>>>()
    }

    private val _officialStoreBenefitResult by lazy {
        MutableLiveData<Result<OfficialStoreBenefits>>()
    }

    private val _officialStoreFeaturedShopResult by lazy {
        MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    }

    val featuredShopResult: LiveData<Result<FeaturedShopDataModel>>
        get() = _featuredShopResult
    val _featuredShopResult by lazy { MutableLiveData<Result<FeaturedShopDataModel>>() }

    val featuredShopRemove: LiveData<FeaturedShopDataModel>
        get() = _featuredShopRemove
    val _featuredShopRemove by lazy { MutableLiveData<FeaturedShopDataModel>() }

    private val _officialStoreDynamicChannelResult = MutableLiveData<Result<List<OfficialStoreChannel>>>()

    private val _productRecommendation = MutableLiveData<Result<ProductRecommendationWithTopAdsHeadline>>()
    val productRecommendation: LiveData<Result<ProductRecommendationWithTopAdsHeadline>>
        get() = _productRecommendation

    private val _topAdsWishlistResult by lazy {
        MutableLiveData<Result<WishlistModel>>()
    }

    private val _recomWidget = MutableLiveData<Result<BestSellerDataModel>>()
    val recomWidget: LiveData<Result<BestSellerDataModel>>
        get() = _recomWidget

    fun loadFirstData(category: Category?, location: String = "",
                      onBannerCacheStartLoad: () -> Unit = {},
                      onBannerCacheStopLoad: () -> Unit = {},
                      onBannerCloudStartLoad: () -> Unit = {},
                      onBannerCloudStopLoad: () -> Unit = {}) {
        launchCatchError(block = {
            val categoryId = category?.categoryId?.toIntOrNull() ?: 0
            currentSlug = "${category?.prefixUrl}${category?.slug}"
            currentSlugDC = category?.slug ?: ""
            onBannerCacheStartLoad.invoke()
            onBannerCloudStartLoad.invoke()
            _officialStoreBannersResult.value =
                Pair(false, getOfficialStoreBanners(currentSlug, true, onBannerCacheStopLoad))
            _officialStoreBannersResult.value =
                Pair(true, getOfficialStoreBanners(currentSlug, false, onBannerCloudStopLoad))
            _officialStoreBenefitResult.value = getOfficialStoreBenefit()
            _officialStoreFeaturedShopResult.value = getOfficialStoreFeaturedShop(categoryId)

            getOfficialStoreDynamicChannel(currentSlug, location)
        }) {
            _officialStoreBannersResult.value = Pair(true, Fail(it))
            _officialStoreBenefitResult.value = Fail(it)
            _officialStoreFeaturedShopResult.value = Fail(it)
        }
    }

    fun loadMoreProducts(categoryId: String, pageNumber: Int, pageName: String = "official-store") {
        launch {
            try {
                withContext(dispatchers.io) {
                    val recomData = getRecommendationUseCase.createObservable(
                        getRecommendationUseCase
                            .getOfficialStoreRecomParams(pageNumber, pageName, categoryId)
                    ).toBlocking()
                    if (isFeaturedShopAllowed && pageNumber == 1 && !recomData.first().isNullOrEmpty()){
                        val topAdsHeadlineData = getTopAdsHeadlineData(pageNumber + 1)
                        val recomDataWithTopAdsHeadlineData = ProductRecommendationWithTopAdsHeadline(recomData.first().first(), topAdsHeadlineData)
                        _productRecommendation.postValue(Success(recomDataWithTopAdsHeadlineData))
                    }else{
                        val recomDataWithoutTopAdsHeadlineData = ProductRecommendationWithTopAdsHeadline(recomData.first().first(), null)
                        _productRecommendation.postValue(Success(recomDataWithoutTopAdsHeadlineData))
                    }

                }
            } catch (e: Throwable) {
                _productRecommendation.value = Fail(e)
            }
        }
    }

    suspend fun getTopAdsHeadlineData(pageNumber: Int): OfficialTopAdsHeadlineDataModel? {
        return try {
            val params = getTopAdsHeadlineUseCase.createParams(
                userId = userSessionInterface.userId,
                page = pageNumber.toString(),
                src = PAGE,
                templateId = VALUE_TEMPLATE_ID,
                headlineProductCount = VALUE_HEADLINE_PRODUCT_COUNT,
                item = VALUE_ITEM,
                seenAds = getSeenShopAdsWidgetCount()
            )
            getTopAdsHeadlineUseCase.setParams(params)
            val data = getTopAdsHeadlineUseCase.executeOnBackground()
            OfficialTopAdsHeadlineDataModel(data)

        } catch (t: Throwable) {
            null
        }
    }

    private fun getSeenShopAdsWidgetCount(): String {
        var count = SEEN_ADS
        impressedShop.forEach {
            count += it.value.size
        }
        return count.toString()
    }

    private suspend fun getOfficialStoreBanners(
        categoryId: String,
        isCache: Boolean,
        onCompleteInvokeData: () -> Unit = {}
    ): Result<OfficialStoreBanners> {
        return withContext(dispatchers.io) {
            try {
                getOfficialStoreBannersUseCase.params =
                    GetOfficialStoreBannerUseCase.createParams(categoryId)
                val banner = getOfficialStoreBannersUseCase.executeOnBackground(isCache)
                banner.isCache = isCache
                onCompleteInvokeData.invoke()
                Success(banner)
            } catch (t: Throwable) {
                onCompleteInvokeData.invoke()
                Fail(t)
            }
        }
    }

    private suspend fun getOfficialStoreBenefit(): Result<OfficialStoreBenefits> {
        return withContext(dispatchers.io) {
            try {
                val benefits = getOfficialStoreBenefitUseCase.executeOnBackground()
                Success(benefits)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private suspend fun getOfficialStoreFeaturedShop(categoryId: Int): Result<OfficialStoreFeaturedShop> {
        return withContext(dispatchers.io) {
            try {
                getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.createParams(categoryId)
                val featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
                Success(featuredShop)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private fun getOfficialStoreDynamicChannel(channelType: String, location: String) {
        launchCatchError(coroutineContext, block = {
            getOfficialStoreDynamicChannelUseCase.setupParams(channelType, location)
            val result = getOfficialStoreDynamicChannelUseCase.executeOnBackground()
            _officialStoreDynamicChannelResult.postValue(Success(result))
            result.forEach {
                //call external api
                if (it.channel.layout == DynamicChannelLayout.LAYOUT_FEATURED_SHOP) {
                    getDisplayTopAdsHeader(FeaturedShopDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(it.channel, 0)))
                    isFeaturedShopAllowed = true
                }
                if (it.channel.layout == DynamicChannelLayout.LAYOUT_BEST_SELLING){
                    fetchRecomWidgetData(it.channel.pageName,  it.channel.widgetParam, it.channel.id)
                }
            }
        }){
            _officialStoreDynamicChannelResult.postValue(Fail(it))
        }
    }

    private suspend fun fetchRecomWidgetData(pageName: String, widgetParam: String, channelId: String) {
        try {
            val data = getRecommendationUseCaseCoroutine.getData(
                GetRecommendationRequestParam(
                    pageName = pageName,
                    queryParam = widgetParam
                )
            )
            val bestSellerDataModel = bestSellerMapper.mappingRecommendationWidget(data.first().copy(channelId = channelId))
            _recomWidget.value = Success(bestSellerDataModel)
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private suspend fun addTopAdsWishlist(model: RecommendationItem): Result<WishlistModel> {
        return withContext(dispatchers.io) {
            try {
                val params = RequestParams.create().apply {
                    putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
                }
                val dataTopAdsWishlist = topAdsWishlishedUseCase.createObservable(params).toBlocking()
                val topAdsWishList = dataTopAdsWishlist.first()

                Success(topAdsWishList)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    fun addWishlist(
        model: RecommendationItem,
        callback: (Boolean, Throwable?) -> Unit
    ) {
        if (model.isTopAds) {
            launchCatchError(block = {
                _topAdsWishlistResult.value = addTopAdsWishlist(model)
                _topAdsWishlistResult.value?.handleResult(callback)
            }) {
                callback.invoke(false, it)
            }
        } else {
            doAddWishlist(model, callback)
        }
    }

    fun addWishlistV2(
        model: RecommendationItem,
        wishlistV2ActionListener: WishlistV2ActionListener
    ) {
        if (model.isTopAds) {
            launchCatchError(block = {
                _topAdsWishlistResult.value = addTopAdsWishlist(model)
                _topAdsWishlistResult.value?.apply {
                    when (this) {
                        is Success -> wishlistV2ActionListener.onSuccessAddWishlist(
                            AddToWishlistV2Response.Data.WishlistAddV2(success = true), model.productId.toString())
                        is Fail -> wishlistV2ActionListener.onSuccessAddWishlist(
                            AddToWishlistV2Response.Data.WishlistAddV2(success = false), model.productId.toString())
                    }
                }
            }) {
                wishlistV2ActionListener.onErrorAddWishList(it, model.productId.toString())
            }
        } else {
            doAddWishlistV2(model, wishlistV2ActionListener)
        }
    }

    private fun doAddWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        addWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                callback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessAddWishlist(productId: String?) {
                callback.invoke(true, null)
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

            override fun onSuccessRemoveWishlist(productId: String?) {}

        })
    }

    private fun doAddWishlistV2(model: RecommendationItem, actionListener: WishlistV2ActionListener) {
        addToWishlistV2UseCase.setParams(model.productId.toString(), userSessionInterface.userId)
        addToWishlistV2UseCase.execute(
            onSuccess = { result ->
                 if (result is Success) actionListener.onSuccessAddWishlist(result.data, model.productId.toString())
            },
            onError = {
                actionListener.onErrorAddWishList(it, model.productId.toString())
            })
    }

    fun removeWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

            override fun onSuccessAddWishlist(productId: String?) {}

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                callback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                callback.invoke(true, null)
            }
        })
    }

    fun removeWishlistV2(model: RecommendationItem, wishlistV2ActionListener: WishlistV2ActionListener) {
        deleteWishlistV2UseCase.setParams(model.productId.toString(), userSessionInterface.userId)
        deleteWishlistV2UseCase.execute(
            onSuccess = { wishlistV2ActionListener.onSuccessRemoveWishlist(model.productId.toString()) },
            onError = { wishlistV2ActionListener.onErrorRemoveWishlist(it, model.productId.toString()) })
    }

    private fun getDisplayTopAdsHeader(featuredShopDataModel: FeaturedShopDataModel){
        launchCatchError(coroutineContext, block={
            getDisplayHeadlineAds.createParams(featuredShopDataModel.channelModel.widgetParam)
            val data = getDisplayHeadlineAds.executeOnBackground()
            if (data.isEmpty()) {
                _featuredShopResult.value = Success(
                    featuredShopDataModel.copy(
                        state = FeaturedShopDataModel.STATE_READY,
                        page = featuredShopDataModel.page
                    )
                )
                isFeaturedShopAllowed = false
            } else {
                _featuredShopResult.value = Success(
                    featuredShopDataModel.copy(
                        channelModel = featuredShopDataModel.channelModel.copy(
                            channelGrids = data.mappingTopAdsHeaderToChannelGrid()
                        ),
                        state = FeaturedShopDataModel.STATE_READY,
                        page = featuredShopDataModel.page
                    )
                )
            }
        }){
            _featuredShopRemove.value = featuredShopDataModel
        }
    }

    fun isLoggedIn() = userSessionInterface.isLoggedIn

    fun getUserId() = userSessionInterface.userId

    fun resetIsFeatureShopAllowed() {
        isFeaturedShopAllowed = false
    }

    fun recordShopWidgetImpression(channelId: String, shopId: String){
        val setOfImpressedShop = impressedShop[channelId]
        if (setOfImpressedShop.isNullOrEmpty()) {
            val newSet = mutableSetOf<String>()
            newSet.add(shopId)
            impressedShop[channelId] = newSet
        }else{
            setOfImpressedShop.add(shopId)
        }
    }

    fun resetShopWidgetImpressionCount() {
        impressedShop.clear()
    }

    override fun onCleared() {
        super.onCleared()
        getRecommendationUseCase.unsubscribe()
        addWishListUseCase.unsubscribe()
        addToWishlistV2UseCase.cancelJobs()
        topAdsWishlishedUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
        deleteWishlistV2UseCase.cancelJobs()
        getDisplayHeadlineAds.cancelJobs()
    }

}
