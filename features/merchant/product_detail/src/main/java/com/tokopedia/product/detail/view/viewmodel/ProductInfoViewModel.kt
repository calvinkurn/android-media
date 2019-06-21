package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import android.util.SparseArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_INPUT
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_KEY
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_DOMAIN
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_ID
import com.tokopedia.product.detail.common.data.model.product.*
import com.tokopedia.product.detail.common.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.checkouttype.GetCheckoutTypeResponse
import com.tokopedia.product.detail.data.model.installment.InstallmentResponse
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailRequest
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCodStatus
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureResponse
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.data.model.talk.TalkList
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRICE
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.data.util.origin
import com.tokopedia.product.detail.data.util.weightInKg
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModel
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class ProductInfoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val userSessionInterface: UserSessionInterface,
                                               private val rawQueries: Map<String, String>,
                                               private val addWishListUseCase: AddWishListUseCase,
                                               private val removeWishlistUseCase: RemoveWishListUseCase,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productInfoP1Resp = MutableLiveData<Result<ProductInfoP1>>()
    val p2ShopDataResp = MutableLiveData<ProductInfoP2ShopData>()
    val p2General = MutableLiveData<ProductInfoP2General>()
    val p2Login = MutableLiveData<ProductInfoP2Login>()
    val productInfoP3resp = MutableLiveData<ProductInfoP3>()

    val loadOtherProduct = MutableLiveData<RequestDataState<List<ProductOther>>>()
    val loadTopAdsProduct = MutableLiveData<RequestDataState<RecommendationModel>>()

    var multiOrigin : WarehouseInfo = WarehouseInfo()
    val userId: String
        get() = userSessionInterface.userId

    val isUserHasShop: Boolean
        get() = userSessionInterface.hasShop()

    var lazyNeedForceUpdate = false

    fun getProductInfo(productParams: ProductParams, forceRefresh: Boolean = false) {
        if (forceRefresh){
            loadOtherProduct.value = null
            loadTopAdsProduct.value = null
            lazyNeedForceUpdate = true
        }
        launchCatchError(block = {
            val cacheStrategy = GraphqlCacheStrategy
                .Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
            val data = withContext(Dispatchers.IO) {
                val paramsInfo = mapOf(PARAM_PRODUCT_ID to productParams.productId?.toInt(),
                        PARAM_SHOP_DOMAIN to productParams.shopDomain,
                        PARAM_PRODUCT_KEY to productParams.productName)
                val graphqlInfoRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_INFO],
                    ProductInfo.Response::class.java, paramsInfo)
                graphqlRepository.getReseponse(listOf(graphqlInfoRequest), cacheStrategy)
            }
            val productInfoP1 = ProductInfoP1()
            var needRequestCod = false

            data.getSuccessData<ProductInfo.Response>().data?.let {
                productInfoP1.productInfo = it
                productInfoP1Resp.value = Success(productInfoP1)
                needRequestCod = it.shouldShowCod
            }

            val p2ShopDeferred = getProductInfoP2ShopAsync(productInfoP1.productInfo.basic.shopID,
                    productInfoP1.productInfo.basic.id.toString(), forceRefresh)

            val p2GeneralDeferred = getProductInfoP2GeneralAsync(productInfoP1.productInfo.basic.shopID,
                    productInfoP1.productInfo.basic.id, productInfoP1.productInfo.basic.price,
                    productInfoP1.productInfo.basic.condition, productInfoP1.productInfo.basic.name,
                    productInfoP1.productInfo.category.id,
                    forceRefresh)

            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive()){
                getProductInfoP2LoginAsync(productInfoP1.productInfo.basic.id,
                        productInfoP1.productInfo.basic.shopID, forceRefresh)
            } else null

            p2ShopDataResp.value = p2ShopDeferred.await()
            p2General.value = p2GeneralDeferred.await()
            p2LoginDeferred?.let { p2Login.value = it.await() }

            p2ShopDataResp.value?.let {
                multiOrigin = it.nearestWarehouse.warehouseInfo
                val domain = productParams.shopDomain ?: it.shopInfo?.shopCore?.domain
                ?: return@launchCatchError

                if (isUserSessionActive())
                    productInfoP3resp.value = getProductInfoP3(productInfoP1.productInfo, domain, forceRefresh,
                            needRequestCod, if (multiOrigin.isFulfillment) multiOrigin.origin else null)
            }

        }) {
            it.printStackTrace()
            productInfoP1Resp.value = Fail(it)
        }
    }


    private suspend fun getProductInfoP2ShopAsync(shopId: Int, productId: String,
                                          forceRefresh: Boolean = false): Deferred<ProductInfoP2ShopData>  {
        return async(Dispatchers.IO) {
            val shopParams = mapOf(PARAM_SHOP_IDS to listOf(shopId),
                    PARAM_SHOP_FIELDS to DEFAULT_SHOP_FIELDS)
            val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

            val nearestWarehouseParam = mapOf("productIds" to listOf(productId))
            val nearestWarehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                    MultiOriginWarehouse.Response::class.java, nearestWarehouseParam)

            val shopCodParam = mapOf("shopID" to shopId.toString())
            val shopCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_COD_STATUS],
                    ShopCodStatus.Response::class.java, shopCodParam)

            val p2Shop = ProductInfoP2ShopData()

            val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()

            val requests = mutableListOf(shopRequest, shopCodRequest, nearestWarehouseRequest)

            try {
                val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

                if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                    val result = (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
                    if (result.data.isNotEmpty())
                        p2Shop.shopInfo = result.data.first()
                }

                if (gqlResponse.getError(ShopCodStatus.Response::class.java)?.isNotEmpty() != true) {
                    p2Shop.shopCod = gqlResponse.getData<ShopCodStatus.Response>(ShopCodStatus.Response::class.java)
                            .result.shopCodStatus.isCod
                }

                if (gqlResponse.getError(MultiOriginWarehouse.Response::class.java)?.isNotEmpty() != true){
                    gqlResponse.getData<MultiOriginWarehouse.Response>(MultiOriginWarehouse.Response::class.java)
                            .result.data.firstOrNull()?.let { p2Shop.nearestWarehouse = it }
                }
            } catch (t: Throwable) {}
            p2Shop
        }
    }

    private suspend fun getProductInfoP2GeneralAsync(shopId: Int, productId: Int, productPrice: Float,
                                         condition: String, productTitle: String, categoryId: String,
                                         forceRefresh: Boolean): Deferred<ProductInfoP2General>{
        return async (Dispatchers.IO) {
            val productInfoP2 = ProductInfoP2General()

            val paramsVariant = mapOf(PARAM_PRODUCT_ID to productId.toString())
            val variantRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_VARIANT],
                    ProductDetailVariantResponse::class.java, paramsVariant)

            val shopBadgeParams = mapOf(PARAM_SHOP_IDS to listOf(shopId))
            val shopBadgeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_BADGE],
                    ShopBadge.Response::class.java, shopBadgeParams)

            val shopCommitmentParams = mapOf(PARAM_SHOP_ID to shopId.toString(),
                    PARAM_PRICE to productPrice.toInt())
            val shopCommitmentRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_COMMITMENT],
                    ShopCommitment.Response::class.java, shopCommitmentParams)

            val ratingParams = mapOf(PARAM_PRODUCT_ID to productId)
            val ratingRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_RATING],
                    Rating.Response::class.java, ratingParams)

            val wishlistCountRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_COUNT],
                    WishlistCount.Response::class.java, ratingParams)

            val voucherParams = mapOf(GetMerchantVoucherListUseCase.SHOP_ID to shopId,
                    GetMerchantVoucherListUseCase.NUM_VOUCHER to DEFAULT_NUM_VOUCHER)
            val voucherRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_VOUCHER],
                    MerchantVoucherQuery::class.java, voucherParams)

            val installmentParams = mapOf(PARAM_PRICE to productPrice, "qty" to 1)
            val installmentRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_INSTALLMENT],
                    InstallmentResponse::class.java, installmentParams)

            val imageReviewParams = mapOf(PARAM_PRODUCT_ID to productId, PARAM_PAGE to 1, PARAM_TOTAL to DEFAULT_NUM_IMAGE_REVIEW)
            val imageReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_IMAGE_REVIEW],
                    ImageReviewGqlResponse::class.java, imageReviewParams)

            val ppParam = PPItemDetailRequest()

            ppParam.productId = productId
            ppParam.shopId = shopId
            if (!TextUtils.isEmpty(userSessionInterface.userId)) {
                ppParam.userId = userSessionInterface.userId.toInt()
            } else {
                ppParam.userId = 0
            }

            if (!TextUtils.isEmpty(categoryId)) {
                ppParam.categoryId = categoryId.toInt()
            } else {
                ppParam.categoryId = 0
            }

            ppParam.condition = condition.toLowerCase()
            ppParam.productTitle = productTitle
            ppParam.price = productPrice.toInt()

            val productPPParams = mapOf("param" to ppParam)

            val productPurchaseProtectionRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_PP],
                    ProductPurchaseProtectionInfo::class.java, productPPParams)

            fun ImageReviewGqlResponse.toImageReviewItemList(): List<ImageReviewItem> {
                val images = SparseArray<ImageReviewGqlResponse.Image>()
                val reviews = SparseArray<ImageReviewGqlResponse.Review>()

                productReviewImageListQuery?.detail?.images?.forEach { images.put(it.imageAttachmentID, it) }
                productReviewImageListQuery?.detail?.reviews?.forEach { reviews.put(it.reviewId, it) }

                return productReviewImageListQuery?.list?.map {
                    val image = images[it.imageID]
                    val review = reviews[it.reviewID]
                    ImageReviewItem(it.reviewID.toString(), review.timeFormat?.dateTimeFmt1,
                            review.reviewer?.fullName, image.uriThumbnail,
                            image.uriLarge, review.rating)
                } ?: listOf()

            }

            val helpfulReviewParams = mapOf(PARAM_PRODUCT_ID to productId)
            val helpfulReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_MOST_HELPFUL_REVIEW], Review.Response::class.java,
                    helpfulReviewParams)

            val latestTalkParams = mapOf(PARAM_PRODUCT_ID to productId)
            val latestTalkRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_LATEST_TALK],
                    TalkList.Response::class.java, latestTalkParams)

            val shopFeatureParam = mapOf("shopID" to shopId.toString())
            val shopFeatureRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_FEATURE],
                    ShopFeatureResponse::class.java, shopFeatureParam)

            val requests = mutableListOf(variantRequest, ratingRequest, wishlistCountRequest, voucherRequest,
                    shopBadgeRequest, shopCommitmentRequest, installmentRequest, imageReviewRequest,
                    helpfulReviewRequest, latestTalkRequest, productPurchaseProtectionRequest, shopFeatureRequest)


            val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
            try {
                val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

                if (gqlResponse.getError(ProductDetailVariantResponse::class.java)?.isNotEmpty() != true){
                    productInfoP2.variantResp = gqlResponse.getData<ProductDetailVariantResponse>(ProductDetailVariantResponse::class.java).data
                }

                if (gqlResponse.getError(ShopBadge.Response::class.java)?.isNotEmpty() != true) {
                    productInfoP2.shopBadge = gqlResponse.getData<ShopBadge.Response>(ShopBadge.Response::class.java)
                            .result.firstOrNull()
                }

                if (gqlResponse.getError(Rating.Response::class.java)?.isNotEmpty() != true)
                    productInfoP2.rating = gqlResponse.getData<Rating.Response>(Rating.Response::class.java).data

                if (gqlResponse.getError(WishlistCount.Response::class.java)?.isNotEmpty() != true)
                    productInfoP2.wishlistCount = gqlResponse.getData<WishlistCount.Response>(WishlistCount.Response::class.java)
                            .wishlistCount

                if (gqlResponse.getError(MerchantVoucherQuery::class.java)?.isNotEmpty() != true) {
                    productInfoP2.vouchers = ((gqlResponse.getData<MerchantVoucherQuery>(MerchantVoucherQuery::class.java))
                            .result?.vouchers?.toList()
                            ?: listOf()).map { MerchantVoucherViewModel(it) }
                }

                if (gqlResponse.getError(ShopCommitment.Response::class.java)?.isNotEmpty() != true) {
                    val resp = gqlResponse.getData<ShopCommitment.Response>(ShopCommitment.Response::class.java).result
                    if (resp.error.message.isBlank()) {
                        productInfoP2.shopCommitment = resp.shopCommitment
                    }
                }

                if (gqlResponse.getError(InstallmentResponse::class.java)?.isNotEmpty() != true) {
                    val resp = gqlResponse.getData<InstallmentResponse>(InstallmentResponse::class.java).result
                    productInfoP2.minInstallment = resp.installmentMinimum
                }

                if (gqlResponse.getError(ImageReviewGqlResponse::class.java)?.isNotEmpty() != true)
                    productInfoP2.imageReviews = gqlResponse.getData<ImageReviewGqlResponse>(ImageReviewGqlResponse::class.java)
                            .toImageReviewItemList()

                if (gqlResponse.getError(Review.Response::class.java)?.isNotEmpty() != true)
                    productInfoP2.helpfulReviews = gqlResponse.getData<Review.Response>(Review.Response::class.java)
                            .productMostHelpfulReviewQuery.list

                if (gqlResponse.getError(TalkList.Response::class.java)?.isNotEmpty() != true) {
                    productInfoP2.latestTalk = gqlResponse.getData<TalkList.Response>(TalkList.Response::class.java)
                            .result.data.talks.firstOrNull() ?: Talk()
                }

                if (gqlResponse.getError(ProductPurchaseProtectionInfo::class.java)?.isNotEmpty() != true) {
                    productInfoP2.productPurchaseProtectionInfo =
                            gqlResponse.getData<ProductPurchaseProtectionInfo>(ProductPurchaseProtectionInfo::class.java)
                }

                if (gqlResponse.getError(ShopFeatureResponse::class.java)?.isNotEmpty() != true) {
                    val shopFeatureResponse =
                            gqlResponse.getData<ShopFeatureResponse>(ShopFeatureResponse::class.java)
                    productInfoP2.shopFeature = shopFeatureResponse.shopFeature.data
                }
            } catch (t: Throwable) {}
            productInfoP2
        }
    }

    private suspend fun getProductInfoP2LoginAsync(productId: Int, shopId: Int, forceRefresh: Boolean = false)
            : Deferred<ProductInfoP2Login>{
        return async(Dispatchers.IO) {
            val p2Login = ProductInfoP2Login()

            val isWishlistedParams = mapOf(PARAM_PRODUCT_ID to productId.toString())
            val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                    ProductInfo.WishlistStatus::class.java, isWishlistedParams)

            val getCheckoutTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_CHECKOUTTYPE],
                    GetCheckoutTypeResponse::class.java)

            val affilateParams = mapOf(ParamAffiliate.PRODUCT_ID_PARAM to listOf(productId),
                    ParamAffiliate.SHOP_ID_PARAM to shopId,
                    ParamAffiliate.INCLUDE_UI_PARAM to true)

            val affiliateRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE],
                    TopAdsPdpAffiliateResponse::class.java, affilateParams)

            val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
            try {
                val response = graphqlRepository.getReseponse(listOf(isWishlistedRequest, getCheckoutTypeRequest,
                        affiliateRequest), cacheStrategy)

                if (response.getError(ProductInfo.WishlistStatus::class.java)?.isNotEmpty() != true)
                    p2Login.isWishlisted = response.getData<ProductInfo.WishlistStatus>(ProductInfo.WishlistStatus::class.java)
                            .isWishlisted == true
                else
                    p2Login.isWishlisted = true


                if (response.getError(TopAdsPdpAffiliateResponse::class.java)?.isNotEmpty() != true) {
                    p2Login.pdpAffiliate = response
                            .getData<TopAdsPdpAffiliateResponse>(TopAdsPdpAffiliateResponse::class.java)
                            .topAdsPDPAffiliate.data.affiliate.firstOrNull()
                }

                if (response.getError(GetCheckoutTypeResponse::class.java)?.isNotEmpty() != true) {
                    p2Login.cartType = response
                            .getData<GetCheckoutTypeResponse>(GetCheckoutTypeResponse::class.java)
                            .getCartType.data.cartType
                }
            } catch (t: Throwable){}

            p2Login
        }
    }

    private fun generateTopAdsParams(productInfo: ProductInfo): Map<String,Any> {
        return mapOf(
                TopAdsDisplay.KEY_USER_ID to userSessionInterface.userId.toInt(),
                TopAdsDisplay.KEY_PAGE_NAME to TopAdsDisplay.DEFAULT_PAGE_NAME,
                TopAdsDisplay.KEY_PAGE_NUMBER to TopAdsDisplay.DEFAULT_PAGE_NUMBER,
                TopAdsDisplay.KEY_XDEVICE to TopAdsDisplay.DEFAULT_DEVICE,
                TopAdsDisplay.KEY_XSOURCE to TopAdsDisplay.DEFAULT_SRC_PAGE,
                TopAdsDisplay.KEY_PRODUCT_ID to productInfo.basic.id.toString()
        )

    }

    private suspend fun getProductInfoP3(productInfo: ProductInfo, shopDomain: String,
                                         forceRefresh: Boolean, needRequestCod: Boolean, origin: String?)
            : ProductInfoP3 = withContext(Dispatchers.IO) {
        val productInfoP3 = ProductInfoP3()

        val estimationParams = mapOf(PARAM_RATE_EST_WEIGHT to productInfo.basic.weightInKg,
                PARAM_RATE_EST_SHOP_DOMAIN to shopDomain, "origin" to origin)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams, false)

        val requests = mutableListOf(estimationRequest)

        if (needRequestCod){
            val userCodParams = mapOf("isPDP" to true)
            val userCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_USER_COD_STATUS],
                    UserCodStatus.Response::class.java, userCodParams)
            requests.add(userCodRequest)
        }

        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()

        try {
            val response = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true) {
                val ratesEstModel = response.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)?.data?.data
                ratesEstModel?.texts?.shopCity = ratesEstModel?.shop?.cityName ?: ""
                productInfoP3.rateEstSummarizeText = ratesEstModel?.texts
            }


            if (needRequestCod && response.getError(UserCodStatus.Response::class.java)?.isNotEmpty() != true){
                productInfoP3.userCod = response.getData<UserCodStatus.Response>(UserCodStatus.Response::class.java)
                        .result.userCodStatus.isCod
            }

        } catch (t: Throwable) {
            t.printStackTrace()
        }
        productInfoP3
    }

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val param = mapOf(PARAM_INPUT to JsonObject().apply {
                addProperty(PARAM_SHOP_ID, shopID)
            })

            val request = GraphqlRequest(rawQueries[RawQueryKeyConstant.MUTATION_FAVORITE_SHOP],
                DataFollowShop::class.java, param)
            val result = withContext(Dispatchers.IO) { graphqlRepository.getReseponse(listOf(request)) }

            onSuccess(result.getSuccessData<DataFollowShop>().followShop.isSuccess)
        }) { onError(it) }
    }

    fun removeWishList(productId: String,
                       onSuccessRemoveWishlist: ((productId: String?) -> Unit)?,
                       onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        removeWishlistUseCase.createObservable(productId,
            userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                onErrorRemoveWishList?.invoke(errorMessage)
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                onSuccessRemoveWishlist?.invoke(productId)
            }
        })
    }

    fun addWishList(productId: String,
                    onErrorAddWishList: ((errorMessage: String?) -> Unit)?,
                    onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        addWishListUseCase.createObservable(productId,
            userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                onErrorAddWishList?.invoke(errorMessage)
            }

            override fun onSuccessAddWishlist(productId: String?) {
                onSuccessAddWishlist?.invoke(productId)
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                // no op
            }
        })
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId.isNotEmpty()

    companion object {

        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"

        private const val PARAM_RATE_EST_SHOP_DOMAIN = "domain"
        private const val PARAM_RATE_EST_WEIGHT = "weight"

        private const val PARAM_PAGE = "page"
        private const val PARAM_TOTAL = "total"
        private const val PARAM_CONDITION = "condition"
        private const val PARAM_PRODUCT_TITLE = "productTitle"

        private const val DEFAULT_NUM_VOUCHER = 3
        private const val DEFAULT_NUM_IMAGE_REVIEW = 4

        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status")

        private const val KEY_PARAM = "params"

        private const val PARAMS_OTHER_PRODUCT_TEMPLATE = "device=android&source=other_product&shop_id=%d&-id=%d"

        object TopAdsDisplay {
            const val KEY_USER_ID = "userID"
            const val KEY_PAGE_NAME = "pageName"
            const val KEY_XDEVICE = "xDevice"
            const val DEFAULT_DEVICE = "android"
            const val DEFAULT_SRC_PAGE = "recommen_pdp"
            const val KEY_PRODUCT_ID = "productIDs"
            const val KEY_XSOURCE = "xSource"
            const val KEY_PAGE_NUMBER = "pageNumber"
            const val DEFAULT_PAGE_NUMBER = 1
            const val DEFAULT_PAGE_NAME = "pdp"
        }

        private object ParamAffiliate {
            const val SHOP_ID_PARAM = "shopId"
            const val PRODUCT_ID_PARAM = "productId"
            const val INCLUDE_UI_PARAM = "includeUI"
        }
    }

    override fun clear() {
        super.clear()
        removeWishlistUseCase.unsubscribe()
        addWishListUseCase.unsubscribe()
    }

    fun loadMore() {
        val product = (productInfoP1Resp.value ?: return) as? Success ?: return
        launch {
            val otherProductDef = if ((loadOtherProduct.value as? Loaded)?.data as? Success == null) {
                loadOtherProduct.value = Loading
                doLoadOtherProduct(product.data.productInfo)
            } else null

            val topAdsProductDef = if (GlobalConfig.isCustomerApp() && isUserSessionActive() &&
                    (loadTopAdsProduct.value as? Loaded)?.data as? Success == null){
                loadTopAdsProduct.value = Loading
                doLoadTopAdsProduct(product.data.productInfo)

            } else null

            otherProductDef?.await()?.let { loadOtherProduct.value = it }
            topAdsProductDef?.await()?.let {
                val recommendationModel = RecommendationEntityMapper.mappingToRecommendationModel((it.data as? Success)?.data?.get(0) ?: return@launch)
                loadTopAdsProduct.value = Loaded(Success(recommendationModel))
            }
            lazyNeedForceUpdate = false
        }
    }

    private fun doLoadOtherProduct(productInfo: ProductInfo) = async(Dispatchers.IO) {
        val otherProductParams = mapOf(KEY_PARAM to String.format(PARAMS_OTHER_PRODUCT_TEMPLATE,
                productInfo.basic.shopID, productInfo.basic.id))
        val otherProductRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_OTHER_PRODUCT],
                ProductOther.Response::class.java, otherProductParams)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (lazyNeedForceUpdate) CacheType.ALWAYS_CLOUD
                else CacheType.CACHE_FIRST).build()

        try {
            Loaded(Success(graphqlRepository.getReseponse(listOf(otherProductRequest), cacheStrategy)
                    .getSuccessData<ProductOther.Response>().result.products))
        } catch (t: Throwable){
            Loaded(Fail(t))
        }
    }

    private fun doLoadTopAdsProduct(productInfo: ProductInfo) = async(Dispatchers.IO) {
        val topadsParams = generateTopAdsParams(productInfo)
        val topAdsRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_RECOMMEN_PRODUCT],
                RecomendationEntity::class.java, topadsParams)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (lazyNeedForceUpdate) CacheType.ALWAYS_CLOUD
                else CacheType.CACHE_FIRST).build()

        try {
            Loaded(Success(graphqlRepository.getReseponse(listOf(topAdsRequest), cacheStrategy)
                    .getSuccessData<RecomendationEntity>().productRecommendationWidget?.data ?: emptyList()))
        } catch (t: Throwable){
            Loaded(Fail(t))
        }
    }

}