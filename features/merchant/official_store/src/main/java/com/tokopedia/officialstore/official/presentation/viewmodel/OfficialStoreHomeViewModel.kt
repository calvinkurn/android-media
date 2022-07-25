package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.OfficialHomeMapper
import com.tokopedia.officialstore.TopAdsHeadlineConstant.PAGE
import com.tokopedia.officialstore.TopAdsHeadlineConstant.SEEN_ADS
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.handleResult
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.mapper.OfficialStoreDynamicChannelComponentMapper
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.*
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
        private val dispatchers: CoroutineDispatchers,
        private val listener: FeaturedShopListener
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val BANNER_POSITION = 0
        private const val BENEFIT_POSITION = 1
        private const val FEATURE_SHOP_POSITION = 2
        private const val RECOM_WIDGET_POSITION = 3
        private const val WIDGET_NOT_FOUND = -1
    }

    var currentSlug: String = ""
        private set
    var currentSlugDC: String = ""
        private set

    var isFeaturedShopAllowed: Boolean = false
        private set

    val impressedShop = mutableMapOf<String, MutableSet<String>>()

    private var _officialStoreListVisitable = mutableListOf<Visitable<*>>()

    val officialStoreLiveData: LiveData<OfficialStoreDataModel>
        get() = _officialStoreLiveData
    private val _officialStoreLiveData: MutableLiveData<OfficialStoreDataModel> = MutableLiveData(OfficialStoreDataModel(
        dataList = _officialStoreListVisitable
    ))

    val officialStoreError : LiveData<Throwable>
        get() = _officialStoreError
    private val _officialStoreError : MutableLiveData<Throwable> = MutableLiveData()

    val topAdsWishlistResult: LiveData<Result<WishlistModel>>
        get() = _topAdsWishlistResult

    val featuredShopRemove: LiveData<FeaturedShopDataModel>
        get() = _featuredShopRemove
    val _featuredShopRemove by lazy { MutableLiveData<FeaturedShopDataModel>() }

    private val _productRecommendation = MutableLiveData<Result<ProductRecommendationWithTopAdsHeadline>>()
    val productRecommendation: LiveData<Result<ProductRecommendationWithTopAdsHeadline>>
        get() = _productRecommendation

    private val _topAdsWishlistResult by lazy {
        MutableLiveData<Result<WishlistModel>>()
    }

    private val _recomWidget = MutableLiveData<Result<BestSellerDataModel>>()
    val recomWidget: LiveData<Result<BestSellerDataModel>>
        get() = _recomWidget

    fun loadFirstDataRevamp(category: Category?, location: String = "",
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
            getOfficialStoreBannersRevamp(currentSlug, true, category?.title, onBannerCacheStopLoad)
            getOfficialStoreBannersRevamp(currentSlug, false, category?.title, onBannerCloudStopLoad)
            getOfficialStoreBenefitRevamp()
            getOfficialStoreFeaturedShopRevamp(
                categoryId, category?.title, listener
            )

            getOfficialStoreDynamicChannelRevamp(currentSlug, location)
        }) {
            _officialStoreError.value = it
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

    private suspend fun getOfficialStoreBannersRevamp(
        categoryId: String,
        isCache: Boolean,
        categoryName: String?,
        onCompleteInvokeData: () -> Unit
    ) {
        withContext(dispatchers.io) {
            try {
                getOfficialStoreBannersUseCase.params =
                    GetOfficialStoreBannerUseCase.createParams(categoryId)
                val banner = getOfficialStoreBannersUseCase.executeOnBackground(isCache)
                banner.isCache = isCache
                onCompleteInvokeData.invoke()
                if (banner.banners.isNotEmpty()) {
                    val newList = mutableListOf<Visitable<*>>()
                    val officialBanner = OfficialBannerDataModel(banner.banners, categoryName.toEmptyStringIfNull())
                    _officialStoreListVisitable.forEach {
                        if (it is OfficialBannerDataModel) {
                            newList.add(officialBanner)
                        }
                        else if (it !is OfficialLoadingMoreDataModel && it !is OfficialLoadingDataModel){
                            newList.add(it)
                        }
                    }
                    val isOfficialBannerDataNotExist = _officialStoreListVisitable.indexOfFirst { it is OfficialBannerDataModel } == OfficialHomeMapper.WIDGET_NOT_FOUND
                    if (isOfficialBannerDataNotExist) {
                        // baru to be checked
                        newList.add(BANNER_POSITION, officialBanner)
                    }
                    _officialStoreListVisitable = newList
                    _officialStoreLiveData.value = OfficialStoreDataModel(_officialStoreListVisitable)
                }
            } catch (t: Throwable) {
                onCompleteInvokeData.invoke()
            }
        }
    }

    private suspend fun getOfficialStoreBenefitRevamp() {
        withContext(dispatchers.io) {
            try {
                val benefits = getOfficialStoreBenefitUseCase.executeOnBackground()
                val newList = mutableListOf<Visitable<*>>()
                val benefit = OfficialBenefitDataModel(benefits.benefits)
                _officialStoreListVisitable.toMutableList().forEach {
                    if(it is OfficialBenefitDataModel) {
                        newList.add(benefit)
                    }
                    else {
                        newList.add(it)
                    }
                }
                val isBenefitNotExisted = newList.indexOfFirst { it is OfficialBenefitDataModel } == OfficialHomeMapper.WIDGET_NOT_FOUND
                if(isBenefitNotExisted) {
                    if(newList.size > OfficialHomeMapper.BENEFIT_POSITION) {
                        newList.add(OfficialHomeMapper.BENEFIT_POSITION, benefit)
                    }
                    else {
                        newList.add(benefit)
                    }
                }
                _officialStoreListVisitable = newList
                _officialStoreLiveData.value = OfficialStoreDataModel(_officialStoreListVisitable)
            } catch (t: Throwable) {
                _officialStoreError.value = t
            }
        }
    }

    private suspend fun getOfficialStoreFeaturedShopRevamp(
        categoryId: Int, categoryName: String?, listener: FeaturedShopListener
    ) {
        withContext(dispatchers.io) {
            try {
                getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.createParams(categoryId)
                val featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
                val newList = mutableListOf<Visitable<*>>()
                val officialFeaturedShop = OfficialFeaturedShopDataModel(
                    featuredShop.featuredShops,
                    featuredShop.header,
                    categoryName.toEmptyStringIfNull(),
                    listener
                )
                _officialStoreListVisitable.forEach {
                    if(it is OfficialFeaturedShopDataModel) {
                        newList.add(officialFeaturedShop)
                    }
                    else {
                        newList.add(it)
                    }
                }
                val isOfficialFeaturedShopNotExisted = newList.indexOfFirst { it is OfficialFeaturedShopDataModel } == OfficialHomeMapper.WIDGET_NOT_FOUND
                if (isOfficialFeaturedShopNotExisted && newList.size > OfficialHomeMapper.FEATURE_SHOP_POSITION) {
                    newList.add(OfficialHomeMapper.FEATURE_SHOP_POSITION, officialFeaturedShop)
                }
                else {
                    newList.add(officialFeaturedShop)
                }
                _officialStoreListVisitable = newList
                _officialStoreLiveData.value = OfficialStoreDataModel(_officialStoreListVisitable)
            } catch (t: Throwable) {
                _officialStoreError.value = t
            }
        }
    }

    private fun getOfficialStoreDynamicChannelRevamp(channelType: String, location: String) {
        launchCatchError(coroutineContext, block = {
            getOfficialStoreDynamicChannelUseCase.setupParams(channelType, location)
            val result = getOfficialStoreDynamicChannelUseCase.executeOnBackground()
            val newList = OfficialHomeMapper.mappingDynamicChannel(result, _officialStoreListVisitable)

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

            _officialStoreListVisitable = newList
            _officialStoreLiveData.value = OfficialStoreDataModel(_officialStoreListVisitable)
        }){
            _officialStoreError.value = it
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
            _recomWidget.value = Fail(t)
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
        launch(dispatchers.main) {
            addToWishlistV2UseCase.setParams(model.productId.toString(), userSessionInterface.userId)
            val result = withContext(dispatchers.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2ActionListener.onSuccessAddWishlist(result.data, model.productId.toString())
            } else if (result is Fail) {
                wishlistV2ActionListener.onErrorAddWishList(result.throwable, model.productId.toString())
            }
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
        launch(dispatchers.main) {
            deleteWishlistV2UseCase.setParams(model.productId.toString(), userSessionInterface.userId)
            val result = withContext(dispatchers.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2ActionListener.onSuccessRemoveWishlist(result.data, model.productId.toString())
            } else if (result is Fail) {
                wishlistV2ActionListener.onErrorRemoveWishlist(result.throwable, model.productId.toString())
            }
        }
    }

    private fun getDisplayTopAdsHeader(featuredShopDataModel: FeaturedShopDataModel){
//        launchCatchError(coroutineContext, block={
//            getDisplayHeadlineAds.createParams(featuredShopDataModel.channelModel.widgetParam)
//            val data = getDisplayHeadlineAds.executeOnBackground()
//            if (data.isEmpty()) {
//                _featuredShopResult.value = Success(
//                    featuredShopDataModel.copy(
//                        state = FeaturedShopDataModel.STATE_READY,
//                        page = featuredShopDataModel.page
//                    )
//                )
//                isFeaturedShopAllowed = false
//            } else {
//                _featuredShopResult.value = Success(
//                    featuredShopDataModel.copy(
//                        channelModel = featuredShopDataModel.channelModel.copy(
//                            channelGrids = data.mappingTopAdsHeaderToChannelGrid()
//                        ),
//                        state = FeaturedShopDataModel.STATE_READY,
//                        page = featuredShopDataModel.page
//                    )
//                )
//            }
//        }){
//            _featuredShopRemove.value = featuredShopDataModel
//        }
    }

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
        topAdsWishlishedUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
        getDisplayHeadlineAds.cancelJobs()
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    private fun updateWidget(visitable: Visitable<*>, position: Int) {
        val newMainNavList = _officialStoreListVisitable
        newMainNavList[position] = visitable
        _officialStoreListVisitable = newMainNavList
        _officialStoreLiveData.postValue(_officialStoreLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    private fun addWidgetList(visitables: List<Visitable<*>>, position: Int) {
        val newMainNavList = _officialStoreListVisitable
        newMainNavList.addAll(position, visitables)
        _officialStoreListVisitable = newMainNavList
        _officialStoreLiveData.postValue(_officialStoreLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    private fun deleteWidget(visitable: Visitable<*>) {
        val newMainNavList = _officialStoreListVisitable.toMutableList()
        newMainNavList.remove(visitable)
        _officialStoreListVisitable = newMainNavList
        _officialStoreLiveData.postValue(_officialStoreLiveData.value?.copy(dataList = newMainNavList))
    }

    private fun deleteWidgetList(visitables: List<Visitable<*>>) {
        val newMainNavList = _officialStoreListVisitable.toMutableList()
        newMainNavList.removeAll(visitables)
        _officialStoreListVisitable = newMainNavList
        _officialStoreLiveData.postValue(_officialStoreLiveData.value?.copy(dataList = newMainNavList))
    }

    private inline fun <reified T> findWidget(
        predicate: (T?) -> Boolean = {true},
        actionOnFound: (T, Int) -> Unit) {
        _officialStoreListVisitable.withIndex().filter { it.value is T && predicate.invoke(it.value as? T) }.let {
            for(visitable in it) {
                if (visitable.value is T) {
                    actionOnFound.invoke(visitable.value as T, visitable.index)
                }
            }
        }
    }
}
