package com.tokopedia.home_recom.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCPMDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.util.RecomServerLogger
import com.tokopedia.home_recom.util.RecomServerLogger.TOPADS_RECOM_PAGE_HIT_DYNAMIC_SLOTTING
import com.tokopedia.home_recom.util.RecomServerLogger.TOPADS_RECOM_PAGE_IS_NOT_ADS
import com.tokopedia.home_recom.util.RecomServerLogger.TOPADS_RECOM_PAGE_TIMEOUT_EXCEEDED
import com.tokopedia.home_recom.util.RecommendationRollenceController
import com.tokopedia.home_recom.util.Response
import com.tokopedia.home_recom.util.mapDataModel
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import rx.Subscriber
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param userSessionInterface the handler of user session
 * @param getRecommendationUseCase use case for Recommendation Widget
 * @param addWishListUseCase use case for add wishlist
 * @param removeWishListUseCase use case for remove wishlist
 * @param topAdsWishlishedUseCase use case for add wishlist topads product item
 * @param dispatcher the dispatcher for coroutine
 */
@SuppressLint("SyntheticAccessor")
open class RecommendationPageViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val getPrimaryProductUseCase: GetPrimaryProductUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val getTopadsIsAdsUseCase: GetTopadsIsAdsUseCase,
        private val getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase,
        private val dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()) {

    companion object {
        const val PARAM_TXSC = "txsc"
        const val PARAM_JOB_TIMEOUT = 1000L
        const val PARAM_SUCCESS_200 = 200
        const val PARAM_SUCCESS_300 = 300
        const val POS_PRODUCT_ANCHOR = 0
        const val POS_CPM = 1
        const val HEADLINE_PARAM_RECOM = "device=android&ep=cpm&headline_product_count=3&item=3&src=recom_google&st=product&template_id=2%2C3%2C4&page=1&q=&user_id="
        const val QUERY_PARAMS_GOOGLE_SHOPPING = "ref=googleshopping"
    }
    /**
     * public variable
     */
    val recommendationListLiveData : LiveData<List<HomeRecommendationDataModel>> get() = _recommendationListLiveData
    private val _recommendationListLiveData = MutableLiveData<List<HomeRecommendationDataModel>>()

    val addToCartLiveData : LiveData<Response<ProductInfoDataModel>> get() = _addToCartLiveData
    private val _addToCartLiveData = MutableLiveData<Response<ProductInfoDataModel>>()

    val buyNowLiveData : LiveData<Response<ProductInfoDataModel>> get() = _buyNowLiveData
    private val _buyNowLiveData = MutableLiveData<Response<ProductInfoDataModel>>()

    /**
     * [getRecommendationList] is the void for get recommendation widgets from the network
     * @param productId product Id from deeplink
     */
    fun getRecommendationList(
            productId: String,
            queryParam: String) {
        launch (dispatcher.getIODispatcher()) {
            try{
                var result = awaitAll(
                        asyncCatchError(dispatcher.getIODispatcher(), block = {
                            getPrimaryProductUseCase.setParameter(productId, queryParam)
                            getPrimaryProductUseCase.executeOnBackground()
                        }) {
                            null
                        },
                        asyncCatchError(dispatcher.getIODispatcher(), block = {
                            val params = getRecommendationUseCase.getRecomParams(
                                    pageNumber = 1,
                                    productIds = listOf(productId),
                                    pageName = "recom_1,recom_2,recom_3",
                                    queryParam = queryParam
                            )
                            getRecommendationUseCase.createObservable(params).toBlocking().first()
                        }) {
                            throw it
                        }
                ) as MutableList<Any?>
                if (eligibleToShowHeadlineCPM(queryParam)) {
                    val newParams = HEADLINE_PARAM_RECOM + userSessionInterface.userId
                    val topadsHeadlineResult = asyncCatchError(dispatcher.getIODispatcher(), block = {
                        getTopAdsHeadlineUseCase.setParams(newParams)
                        getTopAdsHeadlineUseCase.executeOnBackground()
                    }) {
                        throw it
                    }
                    result.add(topadsHeadlineResult.await())
                }
                if (result.isNotEmpty() && !result.all { it == null }) {
                    var anchorProductInfoEntity: PrimaryProductEntity? = null
                    var recommendationWidgets: List<RecommendationWidget>? = listOf()
                    var topAdsHeadlineResponse: TopAdsHeadlineResponse? = null
                    result.forEach {
                        when (it) {
                            is PrimaryProductEntity -> anchorProductInfoEntity = it
                            is List<*> -> recommendationWidgets = it as? List<RecommendationWidget>
                            is TopAdsHeadlineResponse -> topAdsHeadlineResponse = it
                        }
                    }
                    val listVisitable = mutableListOf<HomeRecommendationDataModel>()
                    val anchorProductInfo = anchorProductInfoEntity?.productRecommendationProductDetail?.data?.get(0)?.recommendation?.getOrNull(0)
                    val recommendationMappingWidget = recommendationWidgets?.mapDataModel()
                            ?: listOf()
                    if (anchorProductInfo == null && recommendationMappingWidget.isEmpty()) {
                        listVisitable.add(RecommendationErrorDataModel(Exception()))
                    } else {
                        //append data based on queue

                        //1. append product primary
                        listVisitable.add(ProductInfoDataModel(anchorProductInfo))
                        //2. append CPM based on rollence
                        topAdsHeadlineResponse?.let {
                            if (it.displayAds.data.size != 0)
                                listVisitable.add(
                                        RecommendationCPMDataModel(
                                                topAdsHeadlineResponse = it,
                                                parentPosition = POS_CPM))
                        }
                        //3. append recom list
                        listVisitable.addAll(recommendationMappingWidget)
                    }
                    _recommendationListLiveData.postValue(listVisitable)
                    getProductTopadsStatus(productId, queryParam)
                } else {
                    _recommendationListLiveData.postValue(listOf(RecommendationErrorDataModel(TimeoutException())))
                }

            } catch (t: Exception) {
                _recommendationListLiveData.postValue(listOf(RecommendationErrorDataModel(t)))
            }
        }
    }

    private fun eligibleToShowHeadlineCPM(queryParam: String): Boolean {
        if (!RecommendationRollenceController.isRecommendationCPMRollenceVariant()) return false
        if (queryParam.contains(QUERY_PARAMS_GOOGLE_SHOPPING)) return true
        return false
    }

    fun getProductTopadsStatus(
            productId: String,
            queryParam: String) {
        launchCatchError(coroutineContext, block = {
            var adsStatus = TopadsIsAdsQuery()
            RecomServerLogger.logServer(
                tag = TOPADS_RECOM_PAGE_HIT_DYNAMIC_SLOTTING,
                productId = productId,
                queryParam = queryParam
            )
            val job = withTimeoutOrNull(PARAM_JOB_TIMEOUT) {
                getTopadsIsAdsUseCase.setParams(
                        productId = productId,
                        productKey = "",
                        shopDomain = "",
                        urlParam = queryParam,
                        pageName = ""
                )
                adsStatus = getTopadsIsAdsUseCase.executeOnBackground()
                val dataList = recommendationListLiveData.value as MutableList
                val productRecom = dataList?.firstOrNull { it is ProductInfoDataModel }
                val errorCode = adsStatus.data.status.error_code
                if (errorCode in PARAM_SUCCESS_200..PARAM_SUCCESS_300) {
                    (productRecom as? ProductInfoDataModel)?.productDetailData?.let {
                        val topadsProduct = adsStatus.data.productList[0]
                        it.isTopads = topadsProduct.isCharge
                        it.clickUrl = topadsProduct.clickUrl
                        it.trackerImageUrl = topadsProduct.product.image.m_url

                        val itemIndex = dataList.indexOf(productRecom)
                        dataList[itemIndex] = productRecom

                        _recommendationListLiveData.postValue(dataList)
                    }
                } else {
                    RecomServerLogger.logServer(
                        tag = TOPADS_RECOM_PAGE_IS_NOT_ADS,
                        reason = "Error code $errorCode",
                        productId = productId,
                        queryParam = queryParam
                    )
                }
            }
            if (job == null) RecomServerLogger.logServer(
                tag = TOPADS_RECOM_PAGE_TIMEOUT_EXCEEDED,
                productId = productId,
                queryParam = queryParam
            )
        }) {
            it.printStackTrace()
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    /**
     * [addWishlist] is the void for handling adding wishlist item
     * @param model the recommendation item product is clicked
     * @param callback the callback for handling [added or removed, throwable] to UI
     */
    fun addWishlist(productId: String, wishlistUrl: String, isTopAds: Boolean, callback: ((Boolean, Throwable?) -> Unit)){
        if(isTopAds && wishlistUrl.isNotEmpty()){
            val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, wishlistUrl)
            topAdsWishlishedUseCase.execute(params, object : Subscriber<WishlistModel>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    callback.invoke(false, e)
                }

                override fun onNext(wishlistModel: WishlistModel) {
                    if (wishlistModel.data != null && wishlistModel.data.isSuccess) {
                        callback.invoke(true, null)
                    } else {
                        callback.invoke(false, Throwable())
                    }
                }
            })
        } else {
            addWishListUseCase.createObservable(productId, userSessionInterface.userId, object: WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    callback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    callback.invoke(true, null)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    // do nothing
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    // do nothing
                }
            })
        }
    }

    /**
     * [addWishlist] is the void for handling removing wishlist item
     * @param productId id of product want to remove wishlist
     */
    fun removeWishlist(productId: String, wishlistCallback: (Boolean, Throwable?) -> Unit){
        removeWishListUseCase.createObservable(productId, userSessionInterface.userId, object: WishListActionListener{
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // do nothing
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // do nothing
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                wishlistCallback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                wishlistCallback.invoke(true, null)
            }
        })
    }

    fun onAddToCart(productInfoDataModel: ProductInfoDataModel){
        productInfoDataModel.productDetailData?.let { productDetailData ->
            val addToCartRequestParams = AddToCartRequestParams()
            addToCartRequestParams.productId = productDetailData.id
            addToCartRequestParams.shopId = productDetailData.shop.id
            addToCartRequestParams.quantity = productDetailData.minOrder
            addToCartRequestParams.notes = ""
            addToCartRequestParams.productName = productDetailData.name
            addToCartRequestParams.category = productDetailData.categoryBreadcrumbs
            addToCartRequestParams.price = productDetailData.price
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
            addToCartUseCase.createObservable(requestParams)
                    .subscribeOn(dispatcher.getSchedulerIO())
                    .observeOn(dispatcher.getSchedulerMain())
                    .subscribe(object : Subscriber<AddToCartDataModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            _addToCartLiveData.postValue(Response.error(e))
                        }

                        override fun onNext(addToCartResult: AddToCartDataModel) {
                            if (addToCartResult.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartResult.data.success == 1) {
                                _addToCartLiveData.postValue(Response.success(productInfoDataModel))
                            } else {
                                _addToCartLiveData.postValue(Response.error(Throwable(addToCartResult.errorMessage.firstOrNull())))
                            }
                        }
                    })
        }
    }

    fun onBuyNow(productInfoDataModel: ProductInfoDataModel){
        productInfoDataModel.productDetailData?.let { productDetailData ->
            val addToCartRequestParams = AddToCartRequestParams()
            addToCartRequestParams.productId = productDetailData.id.toLong()
            addToCartRequestParams.shopId = productDetailData.shop.id
            addToCartRequestParams.quantity = productDetailData.minOrder
            addToCartRequestParams.notes = ""
            addToCartRequestParams.atcFromExternalSource = AtcFromExternalSource.ATC_FROM_DISCOVERY
            addToCartRequestParams.productName = productDetailData.name
            addToCartRequestParams.category = productDetailData.categoryBreadcrumbs
            addToCartRequestParams.price = productDetailData.price
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
            addToCartUseCase.createObservable(requestParams)
                    .subscribeOn(dispatcher.getSchedulerIO())
                    .observeOn(dispatcher.getSchedulerMain())
                    .subscribe(object : Subscriber<AddToCartDataModel>() {
                        override fun onCompleted() {}

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            _buyNowLiveData.postValue(Response.error(e))
                        }

                        override fun onNext(addToCartResult: AddToCartDataModel) {
                            if (addToCartResult.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartResult.data.success == 1) {
                                _buyNowLiveData.postValue(Response.success(productInfoDataModel))
                            } else {
                                _buyNowLiveData.postValue(Response.error(Throwable(addToCartResult.errorMessage.firstOrNull())))
                            }
                        }
                    })
        }
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

}