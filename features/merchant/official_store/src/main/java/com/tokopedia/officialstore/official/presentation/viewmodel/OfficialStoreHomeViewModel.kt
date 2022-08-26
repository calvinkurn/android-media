package com.tokopedia.officialstore.official.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Event
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.officialstore.official.presentation.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.TopAdsHeadlineConstant.PAGE
import com.tokopedia.officialstore.TopAdsHeadlineConstant.SEEN_ADS
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.handleResult
import com.tokopedia.officialstore.official.data.mapper.OfficialStoreDynamicChannelComponentMapper
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.officialstore.official.presentation.OfficialStoreConfig
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
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
        private val officialStoreConfig: OfficialStoreConfig,
        private val topAdsAddressHelper: TopAdsAddressHelper,
) : BaseViewModel(dispatchers.main) {

    var currentSlug: String = ""
        private set
    var currentSlugDC: String = ""
        private set
    var isFeaturedShopAllowed: Boolean = false
        private set
    val impressedShop = mutableMapOf<String, MutableSet<String>>()
    var counterTitleShouldBeRendered = 0
    var PRODUCT_RECOMMENDATION_TITLE_SECTION = ""

    private var _officialStoreListVisitable = mutableListOf<Visitable<*>>()

    val officialStoreLiveData: LiveData<OfficialStoreDataModel>
        get() = _officialStoreLiveData
    private val _officialStoreLiveData: MutableLiveData<OfficialStoreDataModel> = MutableLiveData()

    val officialStoreError : LiveData<Throwable>
        get() = _officialStoreError
    private val _officialStoreError : MutableLiveData<Throwable> = MutableLiveData()

    val topAdsWishlistResult: LiveData<Result<WishlistModel>>
        get() = _topAdsWishlistResult

    private val _topAdsWishlistResult by lazy {
        MutableLiveData<Result<WishlistModel>>()
    }

    private val _recomUpdated = MutableLiveData<Event<Boolean>>()
    val recomUpdated : LiveData<Event<Boolean>> get() = _recomUpdated

    fun loadFirstData(category: Category?, location: String = "",
                      onBannerCacheStartLoad: () -> Unit = {},
                      onBannerCacheStopLoad: () -> Unit = {},
                      onBannerCloudStartLoad: () -> Unit = {},
                      onBannerCloudStopLoad: () -> Unit = {}) {
        launchCatchError(block = {
            Log.d("FrenzelDebugOS", "loadFirstData: ")
            val categoryId = category?.categoryId?.toIntOrNull() ?: 0
            currentSlug = "${category?.prefixUrl}${category?.slug}"
            currentSlugDC = category?.slug ?: ""
            onBannerCacheStartLoad.invoke()
            onBannerCloudStartLoad.invoke()
            getOfficialStoreBanners(currentSlug, true, category?.title, onBannerCacheStopLoad)
            getOfficialStoreBanners(currentSlug, false, category?.title, onBannerCloudStopLoad)
            getOfficialStoreBenefit()
            getOfficialStoreFeaturedShop(
                categoryId, category?.title
            )

            getOfficialStoreDynamicChannel(currentSlug, location)
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
                    val topAdsHeadlineData = if (isFeaturedShopAllowed && pageNumber == 1 && !recomData.first().isNullOrEmpty()){
                        getTopAdsHeadlineData(pageNumber + 1)
                    } else{
                        null
                    }
                    val recomDataWithTopAdsHeadlineData = ProductRecommendationWithTopAdsHeadline(recomData.first().first(), topAdsHeadlineData)
                    addRecomTitle(recomDataWithTopAdsHeadlineData.recommendationWidget.title)
                    addRecomProducts(recomDataWithTopAdsHeadlineData)

                }
            } catch (e: Throwable) {
                _officialStoreError.postValue(e)
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
            getTopAdsHeadlineUseCase.setParams(params, topAdsAddressHelper.getAddressData())
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
        categoryName: String?,
        onCompleteInvokeData: () -> Unit
    ) {
        withContext(dispatchers.io) {
            try {
                getOfficialStoreBannersUseCase.params =
                    GetOfficialStoreBannerUseCase.createParams(categoryId)
                val banner = getOfficialStoreBannersUseCase.executeOnBackground(isCache)
                onCompleteInvokeData.invoke()
                if (banner.banners.isNotEmpty()) {
                    OfficialHomeMapper.mappingBanners(
                        banner,
                        _officialStoreListVisitable,
                        categoryName,
                        officialStoreConfig.isEligibleForDisableMappingBanner()
                    ){
                        _officialStoreLiveData.postValue(it, isCache)
                    }
                }
            } catch (t: Throwable) {
                onCompleteInvokeData.invoke()
            }
        }
    }

    private suspend fun getOfficialStoreBenefit() {
        withContext(dispatchers.io) {
            if(!officialStoreConfig.isEligibleForDisableMappingBenefit()){
                try {
                    val benefits = getOfficialStoreBenefitUseCase.executeOnBackground()
                    OfficialHomeMapper.mappingBenefit(benefits, _officialStoreListVisitable){
                        _officialStoreLiveData.postValue(it)
                    }
                } catch (t: Throwable) {
                    _officialStoreError.postValue(t)
                }
            }
        }
    }

    private suspend fun getOfficialStoreFeaturedShop(categoryId: Int, categoryName: String?) {
        withContext(dispatchers.io) {
            if(!officialStoreConfig.isEligibleForDisableMappingOfficialFeaturedShop()){
                try {
                    getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.createParams(categoryId)
                    val featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
                    OfficialHomeMapper.mappingFeaturedShop(featuredShop, _officialStoreListVisitable, categoryName){
                        _officialStoreLiveData.postValue(it)
                    }
                } catch (t: Throwable) {
                    _officialStoreError.postValue(t)
                }
            }
        }
    }

    private fun getOfficialStoreDynamicChannel(channelType: String, location: String) {
        launchCatchError(coroutineContext, block = {
            getOfficialStoreDynamicChannelUseCase.setupParams(channelType, location)
            val result = getOfficialStoreDynamicChannelUseCase.executeOnBackground()

            OfficialHomeMapper.mappingDynamicChannel(result, _officialStoreListVisitable){
                _officialStoreLiveData.postValue(it)
            }

            result.forEach {
                //call external api
                if (it.channel.layout == DynamicChannelLayout.LAYOUT_FEATURED_SHOP) {
                    getDisplayTopAdsHeader(FeaturedShopDataModel(
                        OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(it.channel, 0))
                    )
                    isFeaturedShopAllowed = true
                }
                else if (it.channel.layout == DynamicChannelLayout.LAYOUT_BEST_SELLING){
                    fetchRecomWidgetData(it.channel.pageName,  it.channel.widgetParam, it.channel.id)
                }
            }
        }) {
            _officialStoreError.postValue(it)
        }
    }

    private fun addRecomTitle(title: String){
        PRODUCT_RECOMMENDATION_TITLE_SECTION = title
        if(counterTitleShouldBeRendered == 1){
            _officialStoreListVisitable.add(ProductRecommendationTitleDataModel(title))
            _officialStoreLiveData.postValue()
        }
    }

    private fun addRecomProducts(
        productRecommendationWithTopAdsHeadline: ProductRecommendationWithTopAdsHeadline
    ){
        OfficialHomeMapper.mappingRecomProducts(
            productRecommendationWithTopAdsHeadline, 
            _officialStoreListVisitable
        ){
            _officialStoreLiveData.postValue(it)
        }
        _recomUpdated.postValue(Event(true))
    }

    private suspend fun fetchRecomWidgetData(pageName: String, widgetParam: String, channelId: String) {
        try {
            if(!officialStoreConfig.isEligibleForDisableBestSellerWidget()){
                val result = getRecommendationUseCaseCoroutine.getData(
                    GetRecommendationRequestParam(
                        pageName = pageName,
                        queryParam = widgetParam
                    )
                )
                val bestSellerDataModel = bestSellerMapper.mappingRecommendationWidget(result.first().copy(channelId = channelId))
                OfficialHomeMapper.mappingRecomWidget(bestSellerDataModel, _officialStoreListVisitable){
                    _officialStoreLiveData.postValue(it)
                }
            }
        } catch (t: Throwable) {
            _officialStoreError.postValue(t)
        }
    }

    private fun getDisplayTopAdsHeader(featuredShopDataModel: FeaturedShopDataModel){
        launchCatchError(coroutineContext, block={
            if (!officialStoreConfig.isEligibleForDisableShopWidget()) {
                getDisplayHeadlineAds.createParams(featuredShopDataModel.channelModel.widgetParam)
                val data = getDisplayHeadlineAds.executeOnBackground()
                val updatedFeaturedShop = if (data.isEmpty()) {
                    isFeaturedShopAllowed = false
                    featuredShopDataModel.copy(
                        state = FeaturedShopDataModel.STATE_READY,
                        page = featuredShopDataModel.page
                    )
                } else {
                    featuredShopDataModel.copy(
                        channelModel = featuredShopDataModel.channelModel.copy(
                            channelGrids = data.mappingTopAdsHeaderToChannelGrid()
                        ),
                        state = FeaturedShopDataModel.STATE_READY,
                        page = featuredShopDataModel.page
                    )
                }

                OfficialHomeMapper.updateFeaturedShop(updatedFeaturedShop, _officialStoreListVisitable){
                    _officialStoreLiveData.postValue(it)
                }
            }
        }){
            if (!officialStoreConfig.isEligibleForDisableRemoveShopWidget()) {
                _officialStoreListVisitable.run {
                    removeAll {
                        it is FeaturedShopDataModel && it.channelModel.id == featuredShopDataModel.channelModel.id
                    }
                }
            }
        }
    }

    // ============================================================================================
    // ===================================== ADD/REMOVE WIDGET ====================================
    // ============================================================================================
    fun addLoadingMore(){
        _officialStoreListVisitable.add(OfficialLoadingMoreDataModel())
        _officialStoreLiveData.postValue()
    }

    fun removeRecomWidget(){
        if (!officialStoreConfig.isEligibleForDisableRemoveBestSellerWidget()) {
            _officialStoreListVisitable.run {
                removeAll {
                    it is BestSellerDataModel
                }
                _officialStoreLiveData.postValue(this)
            }
        }
    }

    fun removeFlashSale(){
        _officialStoreListVisitable.run {
            removeAll {
                it is DynamicChannelDataModel || it is ProductRecommendationDataModel
            }
            _officialStoreLiveData.postValue(this)
        }
    }

    fun removeRecommendation(){
        _officialStoreListVisitable.run {
            removeAll { it is ProductRecommendationDataModel || it is ProductRecommendationTitleDataModel }
            _officialStoreLiveData.postValue(this)
        }
    }

    fun removeTopAdsHeadlineWidget() {
        _officialStoreListVisitable.run {
            removeAll { it is OfficialTopAdsHeadlineDataModel }
            _officialStoreLiveData.postValue(this)
        }

    }

    fun resetState() {
        _officialStoreListVisitable.clear()
        _officialStoreListVisitable.add(OfficialHomeMapper.BANNER_POSITION, OfficialLoadingDataModel())
        _officialStoreLiveData.postValue()
    }

    private fun MutableLiveData<OfficialStoreDataModel>.postValue(
        updatedList: MutableList<Visitable<*>> = _officialStoreListVisitable,
        isCache: Boolean? = null
    ){
        if(updatedList != _officialStoreListVisitable){
            _officialStoreListVisitable = updatedList
        }
        this.postValue(OfficialStoreDataModel(_officialStoreListVisitable, isCache))
    }

    // ============================================================================================
    // ===================================== WISHLIST SECTION =====================================
    // ============================================================================================
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

    fun updateWishlist(wishlist: Boolean, position: Int){
        _officialStoreListVisitable.run {
            (getOrNull(position) as? ProductRecommendationDataModel)?.let { recom ->
                val newRecom = recom.copy(
                    productItem = recom.productItem.copy(isWishlist = wishlist)
                )
                this[position] = newRecom
                _officialStoreLiveData.postValue(this)
            }
        }
    }

    // ============================================================================================
    // ======================================== IMPRESSION ========================================
    // ============================================================================================
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

    fun resetIsFeatureShopAllowed() {
        isFeaturedShopAllowed = false
    }

    override fun onCleared() {
        super.onCleared()
        getRecommendationUseCase.unsubscribe()
        addWishListUseCase.unsubscribe()
        topAdsWishlishedUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
        getDisplayHeadlineAds.cancelJobs()
    }
}
