package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import android.util.SparseArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_KEY
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_DOMAIN
import com.tokopedia.product.detail.common.data.model.*
import com.tokopedia.product.detail.common.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.installment.InstallmentResponse
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.shop.ShopBadge
import com.tokopedia.product.detail.data.model.shop.ShopCommitment
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.data.model.talk.TalkList
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRICE
import com.tokopedia.product.detail.data.util.weightInKg
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.RatesEstimationModel
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
                                               private val restRepository: RestRepository,
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

    fun getProductInfo(productParams: ProductParams, resources: Resources) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val paramsInfo = mapOf(PARAM_PRODUCT_ID to productParams.productId?.toInt(),
                        PARAM_SHOP_DOMAIN to productParams.shopDomain,
                        PARAM_PRODUCT_KEY to productParams.productName)
                val graphqlInfoRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_INFO], ProductInfo.Response::class.java, paramsInfo)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlInfoRequest), cacheStrategy)
            }
            val productInfoP1 = ProductInfoP1()

            // for unsigned in jenkins
            /*val error = data.getError(ProductInfo.Response::class.java)
            if (error == null || error.isEmpty()){
                data.getData<ProductInfo.Response>(ProductInfo.Response::class.java).data?.let {
                    productInfoP1.productInfo =  it
                    productInfoP1Resp.value = Success(productInfoP1)
                }
            } else {
                throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
            }*/

            // for signed
            data.getSuccessData<ProductInfo.Response>().data?.let {
                productInfoP1.productInfo =  it
                productInfoP1Resp.value = Success(productInfoP1)
            }

            //if fail, will not interrupt the product info
            val variantJob = async {
                val paramsVariant = mapOf(PARAM_PRODUCT_ID to productInfoP1.productInfo.basic.id)
                val graphqlVariantRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_VARIANT], ProductDetailVariantResponse::class.java, paramsVariant)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlVariantRequest), cacheStrategy).getSuccessData<ProductDetailVariantResponse>()
            }

            val productInfoP2 = getProductInfoP2(productInfoP1.productInfo.basic.shopID,
                    productInfoP1.productInfo.basic.id,
                    productInfoP1.productInfo.basic.price, resources)
            productInfoP2resp.value = productInfoP2
            val domain = productParams.shopDomain ?: productInfoP2.shopInfo?.shopCore?.domain
            ?: return@launchCatchError
            productInfoP3resp.value = getProductInfoP3(productInfoP1.productInfo, domain, resources)

            try {
                productVariantResp.value = Success(variantJob.await().data!!)
            } catch (e: Exception) {
                //TODO just testing
                //productVariantResp.value = Fail(e)
                val gson = Gson()
                val productVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                        ProductDetailVariantResponse::class.java).data ?: ProductVariant()
                productVariantResp.value = Success(productVariant)
            }
        }) {
            //productInfoP1Resp.value = Fail(it)
            // for testing
            val gson = Gson()
            val productInfoP1 = ProductInfoP1()
            productInfoP1.productInfo = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_info_p1),
                    ProductInfo.Response::class.java).data ?: ProductInfo()
            productInfoP1Resp.value = Success(productInfoP1)

            val productVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                    ProductDetailVariantResponse::class.java).data ?: ProductVariant()
            productVariantResp.value = Success(productVariant)

            val productInfoP2 = getProductInfoP2(productInfoP1.productInfo.basic.shopID,
                    productInfoP1.productInfo.basic.id,
                    productInfoP1.productInfo.basic.price, resources)
            productInfoP2resp.value = productInfoP2
            val domain = productParams.shopDomain ?: productInfoP2.shopInfo?.shopCore?.domain
            ?: return@launchCatchError
            productInfoP3resp.value = getProductInfoP3(productInfoP1.productInfo, domain, resources)

        }
    }

    private suspend fun getProductInfoP2(shopId: Int, productId: Int, productPrice: Float, resources: Resources): ProductInfoP2 = withContext(Dispatchers.IO) {
        val productInfoP2 = ProductInfoP2()

        // gson for testing
        val gson = Gson()

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

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        try {
            val gqlResponse = graphqlRepository.getReseponse(listOf(shopRequest, ratingRequest,
                    wishlistCountRequest, voucherRequest, shopBadgeRequest, shopCommitmentRequest,
                    installmentRequest),
                    cacheStrategy)

            val result = if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
            } else {
                gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_shop_info_p2),
                        ShopInfo.Response::class.java).result
            }
            if (result.data.isNotEmpty())
                productInfoP2.shopInfo = result.data.first()

            if (gqlResponse.getError(ShopBadge.Response::class.java)?.isNotEmpty() != true){
                productInfoP2.shopBadge = gqlResponse.getData<ShopBadge.Response>(ShopBadge.Response::class.java)
                        .result.firstOrNull()
            }

            if (gqlResponse.getError(Rating.Response::class.java)?.isNotEmpty() != true)
                productInfoP2.rating = gqlResponse.getData<Rating.Response>(Rating.Response::class.java).data
            else
                productInfoP2.rating = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_rating_p2),
                        Rating.Response::class.java).data

            if (gqlResponse.getError(WishlistCount.Response::class.java)?.isNotEmpty() != true)
                productInfoP2.wishlistCount = gqlResponse.getData<WishlistCount.Response>(WishlistCount.Response::class.java)
                        .wishlistCount
            else
                productInfoP2.wishlistCount = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_wishlist_count),
                        WishlistCount.Response::class.java).wishlistCount

            if (gqlResponse.getError(MerchantVoucherQuery::class.java)?.isNotEmpty() != true) {
                productInfoP2.vouchers = ((gqlResponse.getData<MerchantVoucherQuery>(MerchantVoucherQuery::class.java))
                        .result?.vouchers?.toList()
                        ?: listOf()).map { MerchantVoucherViewModel(it) }
            } else {
                productInfoP2.vouchers = (gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_voucher),
                        MerchantVoucherQuery::class.java)?.result?.vouchers?.toList()
                        ?: listOf()).map { MerchantVoucherViewModel(it) }
            }

            if (gqlResponse.getError(ShopCommitment.Response::class.java)?.isNotEmpty() != true){
                val resp = gqlResponse.getData<ShopCommitment.Response>(ShopCommitment.Response::class.java).result
                if (resp.error.message.isBlank()){
                    productInfoP2.shopCommitment = resp.shopCommitment
                }
            }

            if (gqlResponse.getError(InstallmentResponse::class.java)?.isNotEmpty() != true){
                val resp = gqlResponse.getData<InstallmentResponse>(InstallmentResponse::class.java).result
                productInfoP2.minInstallment = resp.bank.flatMap { it.installmentList }.minBy { it.monthlyPrice }
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


    private suspend fun getProductInfoP3(productInfo: ProductInfo, shopDomain: String, resources: Resources): ProductInfoP3 = withContext(Dispatchers.IO) {
        val productInfoP3 = ProductInfoP3()
        val isWishlistedParams = mapOf(PARAM_PRODUCT_ID to productInfo.basic.id.toString())
        val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                ProductInfo.WishlistStatus::class.java, isWishlistedParams)

        val estimationParams = mapOf(PARAM_RATE_EST_WEIGHT to productInfo.basic.weightInKg, PARAM_RATE_EST_SHOP_DOMAIN to shopDomain)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams)

        val imageReviewParams = mapOf(PARAM_PRODUCT_ID to productInfo.basic.id, PARAM_PAGE to 1, PARAM_TOTAL to DEFAULT_NUM_IMAGE_REVIEW)
        val imageReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_IMAGE_REVIEW],
                ImageReviewGqlResponse::class.java, imageReviewParams)

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

        val helpfulReviewParams = mapOf(PARAM_PRODUCT_ID to productInfo.basic.id)
        val helpfulReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_MOST_HELPFUL_REVIEW], Review.Response::class.java,
                helpfulReviewParams)

        val latestTalkParams = mapOf(PARAM_PRODUCT_ID to productInfo.basic.id)
        val latestTalkRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_LATEST_TALK],
                TalkList.Response::class.java, latestTalkParams)

        val otherProductParams = mapOf(KEY_PARAM to String.format(PARAMS_OTHER_PRODUCT_TEMPLATE,
                productInfo.basic.shopID, productInfo.basic.id))
        val otherProductRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_OTHER_PRODUCT],
                ProductOther.Response::class.java, otherProductParams)

        val topadsParams = mapOf(KEY_PARAM to generateTopAdsParams(productInfo))
        val topAdsRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_DISPLAY_ADS],
                TopAdsDisplayResponse::class.java, topadsParams)

        val affilateParams = mapOf(ParamAffiliate.PRODUCT_ID_PARAM to listOf(productInfo.basic.id),
                ParamAffiliate.SHOP_ID_PARAM to productInfo.basic.shopID,
                ParamAffiliate.INCLUDE_UI_PARAM to true)
        val affiliateRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE],
                TopAdsPdpAffiliateResponse::class.java, affilateParams)

        try {
            val gson = Gson()
            val response = graphqlRepository.getReseponse(listOf(isWishlistedRequest, estimationRequest,
                    imageReviewRequest, helpfulReviewRequest, latestTalkRequest, topAdsRequest, otherProductRequest,
                    affiliateRequest))

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true) {
                productInfoP3.rateEstimation = response.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)
                        .data.ratesEstimation.firstOrNull()
            } else {
                productInfoP3.rateEstimation = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_rate_estimation),
                        RatesEstimationModel.Response::class.java).data.ratesEstimation.first()
            }

            if (response.getError(ProductInfo.WishlistStatus::class.java)?.isNotEmpty() != true)
                productInfoP3.isWishlisted = response.getData<ProductInfo.WishlistStatus>(ProductInfo.WishlistStatus::class.java)
                        .isWishlisted == true
            else
                productInfoP3.isWishlisted = true

            if (response.getError(ImageReviewGqlResponse::class.java)?.isNotEmpty() != true)
                productInfoP3.imageReviews = response.getData<ImageReviewGqlResponse>(ImageReviewGqlResponse::class.java)
                        .toImageReviewItemList()
            else
                productInfoP3.imageReviews = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_image_review),
                        ImageReviewGqlResponse::class.java).toImageReviewItemList()

            if (response.getError(Review.Response::class.java)?.isNotEmpty() != true)
                productInfoP3.helpfulReviews = response.getData<Review.Response>(Review.Response::class.java)
                        .productMostHelpfulReviewQuery.list
            else
                productInfoP3.helpfulReviews = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_most_helpful_review),
                        Review.Response::class.java).productMostHelpfulReviewQuery.list

            if (response.getError(TalkList.Response::class.java)?.isNotEmpty() != true) {
                productInfoP3.latestTalk = response.getData<TalkList.Response>(TalkList.Response::class.java)
                        .result.data.talks.firstOrNull() ?: Talk()
            }

            if (response.getError(TopAdsDisplayResponse::class.java)?.isNotEmpty() != true) {
                productInfoP3.displayAds = response
                        .getData<TopAdsDisplayResponse>(TopAdsDisplayResponse::class.java).result
            }

            if (response.getError(ProductOther.Response::class.java)?.isNotEmpty() != true) {
                productInfoP3.productOthers = response
                        .getData<ProductOther.Response>(ProductOther.Response::class.java).result.products
            }

            if (response.getError(TopAdsPdpAffiliateResponse::class.java)?.isNotEmpty() != true){
                productInfoP3.pdpAffiliate = response
                        .getData<TopAdsPdpAffiliateResponse>(TopAdsPdpAffiliateResponse::class.java)
                        .topAdsPDPAffiliate?.data?.affiliate?.firstOrNull()
            }

        } catch (t: Throwable) {
        }
        productInfoP3
    }

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val bodyMap = AuthUtil.generateParamsNetwork(userId, userSessionInterface.deviceId,
                    mapOf("shop_id" to shopID))
            val request = RestRequest.Builder(ProductDetailConstant.BASE_REST_URL+ProductDetailConstant.PATH_FAVORITE_SHOP_ACTION,
                    object :TypeToken<DataResponse<PostRestResponse>>() {}.type)
                    .setRequestType(RequestType.POST)
                    .setBody(bodyMap).build()
            val result = withContext(Dispatchers.IO){ restRepository.getResponse(request)}
            if (result.isError)
                throw MessageErrorException(result.errorBody)
            else
                onSuccess(result.getData<DataResponse<PostRestResponse>>().data.isSuccess)
        }){ onError(it)}
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