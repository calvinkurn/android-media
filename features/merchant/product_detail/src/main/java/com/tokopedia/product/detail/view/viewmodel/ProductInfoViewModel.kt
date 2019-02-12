package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import android.util.SparseArray
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
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
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfoP1
import com.tokopedia.product.detail.data.model.ProductInfoP2
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.estimasiongkir.RatesEstimationModel
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.product.Rating
import com.tokopedia.product.detail.data.model.product.WishlistCount
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.model.talk.ProductTalkQuery
import com.tokopedia.product.detail.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.product.detail.data.util.weightInKg
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class ProductInfoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val userSessionInterface: UserSessionInterface,
                                               private val rawQueries: Map<String, String>,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productInfoP1Resp = MutableLiveData<Result<ProductInfoP1>>()
    val productInfoP2resp = MutableLiveData<ProductInfoP2>()
    val productInfoP3resp = MutableLiveData<ProductInfoP3>()

    fun getProductInfo(productInfoQuery: String, productVariantQuery: String, productParams: ProductParams, resources: Resources) {

        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val paramsInfo = mapOf(PARAM_PRODUCT_ID to productParams.productId,
                        PARAM_SHOP_DOMAIN to productParams.shopDomain,
                        PARAM_PRODUCT_KEY to productParams.productName)
                val graphqlInfoRequest = GraphqlRequest(productInfoQuery, ProductInfo.Response::class.java, paramsInfo)

                val paramsVariant = mapOf(PARAM_PRODUCT_ID to productParams.productId)
                val graphqlVariantRequest = GraphqlRequest(productVariantQuery, ProductDetailVariantResponse::class.java, paramsVariant)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlInfoRequest, graphqlVariantRequest), cacheStrategy)
            }
            val productInfoP1 = ProductInfoP1()
            productInfoP1.productInfo = data.getSuccessData<ProductInfo.Response>().data

            //if fail, will not interrupt the product info
            try {
                val productVariant = data.getSuccessData<ProductDetailVariantResponse>()
                productInfoP1.productVariant = productVariant.data
            } catch (e: Throwable) {
                // productVariantResp.value = Fail(e)
                //FOR Testing
                val gson = Gson()
                val responseVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                        ProductDetailVariantResponse::class.java)
                productInfoP1.productVariant = responseVariant.data
            }
            productInfoP1Resp.value = Success(productInfoP1)
            val productInfoP2 = getProductInfoP2(productInfoP1.productInfo.basic.shopID, productInfoP1.productInfo.basic.id, resources)
            productInfoP2resp.value = productInfoP2
            val domain = productParams.shopDomain ?: productInfoP2.shopInfo?.shopCore?.domain ?: return@launchCatchError
            productInfoP3resp.value = getProductInfoP3(productInfoP1.productInfo.basic.id, domain,
                    productInfoP1.productInfo.basic.weightInKg, resources)
        }) {
            //productInfoResp.value = Fail(it)
            // for testing
            val gson = Gson()
            val response = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_info_p1),
                    ProductInfo.Response::class.java)
            val productInfoP1 = ProductInfoP1()
            productInfoP1.productInfo = response.data

            //FOR Testing only, remove all below code after testing
            val responseVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                    ProductDetailVariantResponse::class.java)
            productInfoP1.productVariant = responseVariant.data
            productInfoP1Resp.value = Success(productInfoP1)
            val productInfoP2 = getProductInfoP2(response.data.basic.shopID, response.data.basic.id, resources)
            productInfoP2resp.value = productInfoP2
            val domain = productParams.shopDomain ?: productInfoP2.shopInfo?.shopCore?.domain ?: return@launchCatchError
            productInfoP3resp.value = getProductInfoP3(response.data.basic.id, domain,
                    response.data.basic.weightInKg, resources)
        }
    }

    private suspend fun getProductInfoP2(shopId: Int, productId: Int, resources: Resources): ProductInfoP2
            = withContext(Dispatchers.IO){
        val productInfoP2 = ProductInfoP2()

        // gson for testing
        val gson = Gson()

        val shopParams = mapOf(PARAM_SHOP_IDS to listOf(shopId),
                PARAM_SHOP_FIELDS to listOf<String>())
        val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

        val ratingParams = mapOf(PARAM_PRODUCT_ID to productId)
        val ratingRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_RATING],
                Rating.Response::class.java, ratingParams)

        val wishlistCountRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_COUNT],
                WishlistCount.Response::class.java, ratingParams)

        val voucherParams = mapOf(GetMerchantVoucherListUseCase.SHOP_ID to shopId,
                GetMerchantVoucherListUseCase.NUM_VOUCHER to DEFAULT_NUM_VOUCHER)
        val voucherRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_VOUCHER],
                MerchantVoucherQuery::class.java, voucherParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        try {
            val gqlResponse = graphqlRepository.getReseponse(listOf(shopRequest, ratingRequest,
                    wishlistCountRequest, voucherRequest), cacheStrategy)

            val result = if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true){
                (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
            } else {
                gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_shop_info_p2),
                        ShopInfo.Response::class.java).result
            }
            if (result.data.isNotEmpty())
                productInfoP2.shopInfo = result.data.first()

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

            if (gqlResponse.getError(MerchantVoucherQuery::class.java)?.isNotEmpty() != true){
                productInfoP2.vouchers = ((gqlResponse.getData<MerchantVoucherQuery>(MerchantVoucherQuery::class.java))
                        .result?.vouchers?.toList() ?: listOf()).map { MerchantVoucherViewModel(it) }
            } else {
                productInfoP2.vouchers = (gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_voucher),
                        MerchantVoucherQuery::class.java)?.result?.vouchers?.toList() ?: listOf()).map { MerchantVoucherViewModel(it) }
            }

            productInfoP2
        } catch (t: Throwable){
            // for testing
            productInfoP2
        }
    }


    private suspend fun getProductInfoP3(productId: Int, shopDomain: String,
                                         weightInKg: Float, resources: Resources): ProductInfoP3
            = withContext(Dispatchers.IO){
        val productInfoP3 = ProductInfoP3()
        val isWishlistedParams = mapOf(PARAM_PRODUCT_ID to productId.toString())
        val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                ProductInfo.WishlistStatus::class.java, isWishlistedParams)

        val estimationParams = mapOf(PARAM_RATE_EST_WEIGHT to weightInKg, PARAM_RATE_EST_SHOP_DOMAIN to shopDomain)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams)

        val imageReviewParams = mapOf(PARAM_PRODUCT_ID to productId, PARAM_PAGE to 1, PARAM_TOTAL to DEFAULT_NUM_IMAGE_REVIEW)
        val imageReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_IMAGE_REVIEW],
                ImageReviewGqlResponse::class.java, imageReviewParams)

        fun ImageReviewGqlResponse.toImageReviewItemList(): List<ImageReviewItem>{
            val images = SparseArray<ImageReviewGqlResponse.Image>()
            val reviews = SparseArray<ImageReviewGqlResponse.Review>()

            productReviewImageListQuery?.detail?.images?.forEach { images.put(it.imageAttachmentID, it) }
            productReviewImageListQuery?.detail?.reviews?.forEach { reviews.put(it.reviewId, it) }

            return productReviewImageListQuery?.list?.map {
                val image = images[it.imageID]
                val review = reviews[it.reviewID]
                ImageReviewItem(it.reviewID.toString(), review.timeFormat?.dateTimeFmt1,
                        review.reviewer?.fullName, image.uriThumbnail,
                        image.uriLarge, review.rating) } ?: listOf()

        }

        val helpfulReviewParams = mapOf(PARAM_PRODUCT_ID to productId)
        val helpfulReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_MOST_HELPFUL_REVIEW], Review.Response::class.java,
                helpfulReviewParams)

        val latestTalkParams = mapOf(PARAM_PRODUCT_ID to productId.toString())
        val latestTalkRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_LATEST_TALK],
                ProductTalkQuery.Response::class.java, latestTalkParams)

        try {
            val gson = Gson()
            val response = graphqlRepository.getReseponse(listOf(isWishlistedRequest, estimationRequest,
                    imageReviewRequest, helpfulReviewRequest, latestTalkRequest))

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true){
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

            if (response.getError(ProductTalkQuery.Response::class.java)?.isNotEmpty() != true){
                productInfoP3.latestTalk = response.getData<ProductTalkQuery.Response>(ProductTalkQuery.Response::class.java)
                        .productTalkQuery
            } else {
                productInfoP3.latestTalk = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_latest_talk),
                        ProductTalkQuery.Response::class.java).productTalkQuery
            }

        } catch (t: Throwable){}
        productInfoP3
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId.isNotEmpty()

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"
        private const val PARAM_PRODUCT_KEY = "productKey"

        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"

        private const val PARAM_RATE_EST_SHOP_DOMAIN = "domain"
        private const val PARAM_RATE_EST_WEIGHT = "weight"

        private const val PARAM_PAGE = "page"
        private const val PARAM_TOTAL = "total"

        private const val DEFAULT_NUM_VOUCHER = 3
        private const val DEFAULT_NUM_IMAGE_REVIEW = 4
    }



}