package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import android.util.SparseArray
import com.google.gson.Gson
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
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_INPUT
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_KEY
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_DOMAIN
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_ID
import com.tokopedia.product.detail.common.data.model.*
import com.tokopedia.product.detail.common.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.checkouttype.GetCheckoutTypeResponse
import com.tokopedia.product.detail.data.model.installment.InstallmentResponse
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailRequest
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.shop.ShopBadge
import com.tokopedia.product.detail.data.model.shop.ShopCodStatus
import com.tokopedia.product.detail.data.model.shop.ShopCommitment
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.data.model.talk.TalkList
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRICE
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.data.util.weightInKg
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.topads.sdk.domain.Xparams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
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
    val productInfoP2resp = MutableLiveData<ProductInfoP2>()
    val productInfoP3resp = MutableLiveData<ProductInfoP3>()
    val productVariantResp = MutableLiveData<Result<ProductVariant>>()
    val userId: String
        get() = userSessionInterface.userId

    val isUserHasShop: Boolean
        get() = userSessionInterface.hasShop()

    fun getProductInfo(productParams: ProductParams, forceRefresh: Boolean = false) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val paramsInfo = mapOf(PARAM_PRODUCT_ID to productParams.productId?.toInt(),
                        PARAM_SHOP_DOMAIN to productParams.shopDomain,
                        PARAM_PRODUCT_KEY to productParams.productName)
                val graphqlInfoRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_INFO],
                        ProductInfo.Response::class.java, paramsInfo)
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlInfoRequest), cacheStrategy)
            }
            val productInfoP1 = ProductInfoP1()
            var needRequestCod = false

            data.getSuccessData<ProductInfo.Response>().data?.let {
                productInfoP1.productInfo = it
                productInfoP1Resp.value = Success(productInfoP1)
                needRequestCod = it.shouldShowCod
            }

            //if fail, will not interrupt the product info
            val variantJob = async {
                try {
                    val paramsVariant = mapOf(PARAM_PRODUCT_ID to productInfoP1.productInfo.basic.id.toString())
                    val graphqlVariantRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_VARIANT], ProductDetailVariantResponse::class.java, paramsVariant)
                    val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                    graphqlRepository.getReseponse(listOf(graphqlVariantRequest), cacheStrategy).getSuccessData<ProductDetailVariantResponse>()
                } catch (t: Throwable) {
                    t
                }
            }


            val productInfoP2 = getProductInfoP2(productInfoP1.productInfo.basic.shopID,
                    productInfoP1.productInfo.basic.id, productInfoP1.productInfo.basic.price,
                    productInfoP1.productInfo.basic.condition, productInfoP1.productInfo.basic.name,
                    productInfoP1.productInfo.category.id,
                    forceRefresh)
            productInfoP2resp.value = productInfoP2
            val domain = productParams.shopDomain ?: productInfoP2.shopInfo?.shopCore?.domain
            ?: return@launchCatchError

            if (isUserSessionActive())
                productInfoP3resp.value = getProductInfoP3(productInfoP1.productInfo, domain, forceRefresh,
                        needRequestCod)

            try {
                val result = variantJob.await()
                if (result is Throwable) throw  result
                productVariantResp.value = Success((result as ProductDetailVariantResponse).data)
            } catch (e: Exception) {
                productVariantResp.value = Fail(e)
            }
        }) {
            it.printStackTrace()
            productInfoP1Resp.value = Fail(it)
        }
    }

    private suspend fun getProductInfoP2(shopId: Int, productId: Int, productPrice: Float,
                                         condition: String, productTitle: String, categoryId: String,
                                         forceRefresh: Boolean): ProductInfoP2 = withContext(Dispatchers.IO) {
        val productInfoP2 = ProductInfoP2()

        val shopParams = mapOf(PARAM_SHOP_IDS to listOf(shopId),
                PARAM_SHOP_FIELDS to DEFAULT_SHOP_FIELDS)
        val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

        val shopBadgeParams = mapOf(PARAM_SHOP_IDS to listOf(shopId))
        val shopBadgeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_BADGE],
                ShopBadge.Response::class.java, shopBadgeParams)

        val shopCommitmentParams = mapOf(ProductDetailCommonConstant.PARAM_SHOP_ID to shopId.toString(),
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

        val otherProductParams = mapOf(KEY_PARAM to String.format(PARAMS_OTHER_PRODUCT_TEMPLATE,
                shopId, productId))
        val otherProductRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_OTHER_PRODUCT],
                ProductOther.Response::class.java, otherProductParams)


        val shopCodParam = mapOf("shopID" to shopId.toString())
        val shopCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_COD_STATUS],
                ShopCodStatus.Response::class.java, shopCodParam)

        val requests = mutableListOf(shopRequest, ratingRequest, wishlistCountRequest, voucherRequest,
                shopBadgeRequest, shopCommitmentRequest, installmentRequest, imageReviewRequest,
                helpfulReviewRequest, latestTalkRequest, otherProductRequest, shopCodRequest, productPurchaseProtectionRequest)


        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                val result = (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
                if (result.data.isNotEmpty())
                    productInfoP2.shopInfo = result.data.first()
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

            if (gqlResponse.getError(ProductOther.Response::class.java)?.isNotEmpty() != true) {
                productInfoP2.productOthers = gqlResponse
                        .getData<ProductOther.Response>(ProductOther.Response::class.java).result.products
            }

            if (gqlResponse.getError(ShopCodStatus.Response::class.java)?.isNotEmpty() != true) {
                productInfoP2.shopCod = gqlResponse.getData<ShopCodStatus.Response>(ShopCodStatus.Response::class.java)
                        .result.shopCodStatus.isCod
            }

            if (gqlResponse.getError(ProductPurchaseProtectionInfo::class.java)?.isNotEmpty() != true) {
                productInfoP2.productPurchaseProtectionInfo =
                        gqlResponse.getData<ProductPurchaseProtectionInfo>(ProductPurchaseProtectionInfo::class.java)
            }

            productInfoP2
        } catch (t: Throwable) {
            // for testing
            productInfoP2
        }
    }

    private fun generateTopAdsParams(productInfo: ProductInfo): String {
        val xparams = Xparams().apply {
            product_id = productInfo.basic.id
            product_name = productInfo.basic.name
            source_shop_id = productInfo.basic.shopID
            if (productInfo.category.detail.size > 2)
                child_cat_id = productInfo.category.detail[2].id.toIntOrNull() ?: 0
        }

        return mapOf(TopAdsDisplay.KEY_ITEM to TopAdsDisplay.DEFAULT_TOTAL_ITEM,
                TopAdsDisplay.KEY_DEVICE to TopAdsDisplay.DEFAULT_DEVICE,
                PARAM_PAGE to 1,
                TopAdsDisplay.KEY_SRC to TopAdsDisplay.DEFAULT_SRC_PAGE,
                TopAdsDisplay.KEY_EP to TopAdsDisplay.DEFAULT_EP,
                TopAdsDisplay.KEY_XPARAMS to Gson().toJson(xparams),
                PARAM_USER_ID to userSessionInterface.userId).map { "${it.key}=${it.value}" }.joinToString("&")
    }


    private suspend fun getProductInfoP3(productInfo: ProductInfo, shopDomain: String,
                                         forceRefresh: Boolean, needRequestCod: Boolean)
            : ProductInfoP3 = withContext(Dispatchers.IO) {
        val productInfoP3 = ProductInfoP3()

        val isWishlistedParams = mapOf(PARAM_PRODUCT_ID to productInfo.basic.id.toString())
        val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                ProductInfo.WishlistStatus::class.java, isWishlistedParams)

        val estimationParams = mapOf(PARAM_RATE_EST_WEIGHT to productInfo.basic.weightInKg,
                PARAM_RATE_EST_SHOP_DOMAIN to shopDomain)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams, false)

        val getCheckoutTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_CHECKOUTTYPE],
                GetCheckoutTypeResponse::class.java)

        val affilateParams = mapOf(ParamAffiliate.PRODUCT_ID_PARAM to listOf(productInfo.basic.id),
                ParamAffiliate.SHOP_ID_PARAM to productInfo.basic.shopID,
                ParamAffiliate.INCLUDE_UI_PARAM to true)

        val affiliateRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE],
                TopAdsPdpAffiliateResponse::class.java, affilateParams)

        val requests = mutableListOf(isWishlistedRequest, estimationRequest,
                getCheckoutTypeRequest, affiliateRequest)

        if (GlobalConfig.isCustomerApp()) {
            val topadsParams = mapOf(KEY_PARAM to generateTopAdsParams(productInfo))
            val topAdsRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_DISPLAY_ADS],
                    TopAdsDisplayResponse::class.java, topadsParams)
            requests.add(topAdsRequest)
        }

        if (needRequestCod) {
            val userCodParams = mapOf("isPDP" to true)
            val userCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_USER_COD_STATUS],
                    UserCodStatus.Response::class.java, userCodParams)
            requests.add(userCodRequest)
        }

        try {
            val response = graphqlRepository.getReseponse(requests)

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true) {
                val ratesEstModel = response.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)?.data?.data
                productInfoP3.rateEstSummarizeText = ratesEstModel?.texts
            }

            if (response.getError(ProductInfo.WishlistStatus::class.java)?.isNotEmpty() != true)
                productInfoP3.isWishlisted = response.getData<ProductInfo.WishlistStatus>(ProductInfo.WishlistStatus::class.java)
                        .isWishlisted == true
            else
                productInfoP3.isWishlisted = true


            if (GlobalConfig.isCustomerApp()) {
                if (response.getError(TopAdsDisplayResponse::class.java)?.isNotEmpty() != true) {
                    productInfoP3.displayAds = response
                            .getData<TopAdsDisplayResponse>(TopAdsDisplayResponse::class.java).result
                }
            }

            if (response.getError(TopAdsPdpAffiliateResponse::class.java)?.isNotEmpty() != true) {
                productInfoP3.pdpAffiliate = response
                        .getData<TopAdsPdpAffiliateResponse>(TopAdsPdpAffiliateResponse::class.java)
                        .topAdsPDPAffiliate?.data?.affiliate?.firstOrNull()
            }

            if (response.getError(GetCheckoutTypeResponse::class.java)?.isNotEmpty() != true) {
                productInfoP3.isExpressCheckoutType = response
                        .getData<GetCheckoutTypeResponse>(GetCheckoutTypeResponse::class.java)
                        .getCartType.isExpress
            }

            if (needRequestCod && response.getError(UserCodStatus.Response::class.java)?.isNotEmpty() != true) {
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
        private const val PARAM_USER_ID = "user_id"
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
            const val DEFAULT_TOTAL_ITEM = 5
            const val DEFAULT_DEVICE = "android"
            const val DEFAULT_SRC_PAGE = "pdp"
            const val DEFAULT_EP = "product"
            const val KEY_ITEM = "item"
            const val KEY_DEVICE = "device"
            const val KEY_SRC = "src"
            const val KEY_EP = "ep"
            const val KEY_XPARAMS = "xparams"
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

}