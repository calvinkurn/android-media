package com.tokopedia.product.detail.usecase

import android.util.Log
import android.util.SparseArray
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.common.data.model.product.WishlistCount
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationResponse
import com.tokopedia.product.detail.data.model.installment.InstallmentResponse
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailRequest
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureResponse
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.data.model.talk.TalkList
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP2GeneralUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                         private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2General>() {

    companion object {
        fun createParams(shopId: Int, productId: Int, productPrice: Int,
                         condition: String, productTitle: String, categoryId: Int, catalogId: String,
                         userId: Int, forceRefresh: Boolean, minOrder: Int, warehouseId: String?, isVariant: Boolean): RequestParams =
                RequestParams.create().apply {
                    putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
                    putInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putInt(ProductDetailCommonConstant.PARAM_PRICE, productPrice)
                    putString(ProductDetailCommonConstant.PARAM_CONDITION, condition)
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_TITLE, productTitle)
                    putInt(ProductDetailCommonConstant.PARAM_CATEGORY_ID, categoryId)
                    putString(ProductDetailCommonConstant.PARAM_CATALOG_ID, catalogId)
                    putInt(ProductDetailCommonConstant.PARAM_USER_ID, userId)
                    putBoolean(ProductDetailCommonConstant.FORCE_REFRESH, forceRefresh)
                    putInt(ProductDetailCommonConstant.PARAM_MIN_ORDER, minOrder)
                    putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, warehouseId)
                    putBoolean(ProductDetailCommonConstant.PARAM_IS_VARIANT, isVariant)
                }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductInfoP2General {
        val productInfoP2 = ProductInfoP2General()

        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val productId = requestParams.getInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, 0)
        val productPrice = requestParams.getInt(ProductDetailCommonConstant.PARAM_PRODUCT_PRICE, 0)
        val condition = requestParams.getString(ProductDetailCommonConstant.PARAM_CONDITION, "")
        val productTitle = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_TITLE, "")
        val categoryId = requestParams.getInt(ProductDetailCommonConstant.PARAM_CATEGORY_ID, 0)
        val catalogId = requestParams.getString(ProductDetailCommonConstant.PARAM_CATALOG_ID, "")
        val userId = requestParams.getInt(ProductDetailCommonConstant.PARAM_USER_ID, 0)
        val forceRefresh = requestParams.getBoolean(ProductDetailCommonConstant.FORCE_REFRESH, false)
        val minOrder = requestParams.getInt(ProductDetailCommonConstant.PARAM_MIN_ORDER, 0)
        val warehouseId = requestParams.getString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, null)
        val isVariant = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_IS_VARIANT, false)

        val paramsVariant = mapOf(
                ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId.toString(),
                ProductDetailConstant.PARAM_OPTION to mapOf(
                        ProductDetailConstant.KEY_USER_ID to userId.toString(),
                        ProductDetailConstant.PARAM_INCLUDE_CAMPAIGN to true,
                        ProductDetailCommonConstant.PARAM_WAREHOUSE_ID to warehouseId,
                        ProductDetailCommonConstant.PARAM_INCLUDE_WAREHOUSE to true))
        val variantRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_VARIANT],
                ProductDetailVariantCommonResponse::class.java, paramsVariant)

        val shopBadgeParams = mapOf(ProductDetailCommonConstant.PARAM_SHOP_IDS to listOf(shopId))
        val shopBadgeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_BADGE],
                ShopBadge.Response::class.java, shopBadgeParams)

        val shopCommitmentParams = mapOf(ProductDetailCommonConstant.PARAM_SHOP_ID to shopId.toString(),
                ProductDetailConstant.PARAM_PRICE to productPrice)
        val shopCommitmentRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_COMMITMENT],
                ShopCommitment.Response::class.java, shopCommitmentParams)

        val ratingParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId)
        val ratingRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_RATING],
                Rating.Response::class.java, ratingParams)

        val wishlistCountRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_COUNT],
                WishlistCount.Response::class.java, ratingParams)

        val voucherParams = mapOf(GetMerchantVoucherListUseCase.SHOP_ID to shopId,
                GetMerchantVoucherListUseCase.NUM_VOUCHER to ProductDetailCommonConstant.DEFAULT_NUM_VOUCHER)
        val voucherRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_VOUCHER],
                MerchantVoucherQuery::class.java, voucherParams)

        val installmentParams = mapOf(ProductDetailConstant.PARAM_PRICE to productPrice, "qty" to 1)
        val installmentRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_INSTALLMENT],
                InstallmentResponse::class.java, installmentParams)

        val imageReviewParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId, ProductDetailCommonConstant.PARAM_PAGE to 1, ProductDetailCommonConstant.PARAM_TOTAL to ProductDetailCommonConstant.DEFAULT_NUM_IMAGE_REVIEW)
        val imageReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_IMAGE_REVIEW],
                ImageReviewGqlResponse::class.java, imageReviewParams)

        val productPPParams = mapOf("param" to PPItemDetailRequest(productId = productId, shopId = shopId, userId = userId, categoryId = categoryId,
                condition = condition.toLowerCase(), productTitle = productTitle, price = productPrice))
        val productPurchaseProtectionRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_PP],
                ProductPurchaseProtectionInfo::class.java, productPPParams)


        val helpfulReviewParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId)
        val helpfulReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_MOST_HELPFUL_REVIEW], Review.Response::class.java,
                helpfulReviewParams)

        val latestTalkParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId)
        val latestTalkRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_LATEST_TALK],
                TalkList.Response::class.java, latestTalkParams)

        val discussionMostHelpfulParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId.toString(),
            ProductDetailCommonConstant.PARAM_SHOP_ID to shopId.toString())
        val discussionMostHelpfulRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_DISCUSSION_MOST_HELPFUL],
            DiscussionMostHelpfulResponseWrapper::class.java, discussionMostHelpfulParams)

        val shopFeatureParam = mapOf(ProductDetailCommonConstant.PARAM_SHOP_ID to shopId.toString())
        val shopFeatureRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_FEATURE],
                ShopFeatureResponse::class.java, shopFeatureParam)

        val productCatalogParams = mapOf(ProductDetailCommonConstant.PARAM_CATALOG_ID to catalogId)
        val productCatalogRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_CATALOG],
                ProductSpecificationResponse::class.java, productCatalogParams)

        val pdpFinancingRecommendationParam = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_PRICE to productPrice,
                ProductDetailCommonConstant.PARAM_PRODUCT_QUANTITY to minOrder)
        val pdpFinancingRecommendationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PDP_FINANCING_RECOMMENDATION],
                PDPInstallmentRecommendationResponse::class.java, pdpFinancingRecommendationParam)

        val pdpFinancingCalculationParam = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_PRICE to productPrice,
                ProductDetailCommonConstant.PARAM_PRODUCT_QUANTITY to minOrder)
        val pdpFinancingCalculationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PDP_FINANCING_CALCULATION],
                FinancingDataResponse::class.java, pdpFinancingCalculationParam)

        val requests = mutableListOf(ratingRequest, wishlistCountRequest, voucherRequest,
                shopBadgeRequest, shopCommitmentRequest, installmentRequest, imageReviewRequest,
                helpfulReviewRequest, latestTalkRequest, discussionMostHelpfulRequest, productPurchaseProtectionRequest, shopFeatureRequest, productCatalogRequest, pdpFinancingRecommendationRequest, pdpFinancingCalculationRequest)

        if (isVariant) {
            requests.add(variantRequest)
        }

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (gqlResponse.getError(ProductDetailVariantCommonResponse::class.java)?.isNotEmpty() != true && isVariant) {
                productInfoP2.variantResp = gqlResponse.getData<ProductDetailVariantCommonResponse>(ProductDetailVariantCommonResponse::class.java).data
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

            if (gqlResponse.getError(DiscussionMostHelpfulResponseWrapper::class.java)?.isNotEmpty() != true) {
                productInfoP2.discussionMostHelpful = gqlResponse.getData<DiscussionMostHelpfulResponseWrapper>(DiscussionMostHelpfulResponseWrapper::class.java).discussionMostHelpful
            }

            if (gqlResponse.getError(ProductPurchaseProtectionInfo::class.java)?.isNotEmpty() != true) {
                productInfoP2.productPurchaseProtectionInfo =
                        gqlResponse.getData(ProductPurchaseProtectionInfo::class.java)
            }

            if (gqlResponse.getError(ShopFeatureResponse::class.java)?.isNotEmpty() != true) {
                val shopFeatureResponse =
                        gqlResponse.getData<ShopFeatureResponse>(ShopFeatureResponse::class.java)
                productInfoP2.shopFeature = shopFeatureResponse.shopFeature.data
            }

            if (gqlResponse.getError(ProductSpecificationResponse::class.java)?.isNotEmpty() != true) {
                val productSpesification: ProductSpecificationResponse = gqlResponse.getData(ProductSpecificationResponse::class.java)
                productInfoP2.productSpecificationResponse = productSpesification
            }

            if (gqlResponse.getError(PDPInstallmentRecommendationResponse::class.java)?.isNotEmpty() != true) {
                val installmentRecommendationData: PDPInstallmentRecommendationResponse =
                        gqlResponse.getData(PDPInstallmentRecommendationResponse::class.java)
                productInfoP2.productFinancingRecommendationData = installmentRecommendationData
            }

            if (gqlResponse.getError(FinancingDataResponse::class.java)?.isNotEmpty() != true) {
                val financingCalculationData: FinancingDataResponse = gqlResponse.getData(FinancingDataResponse::class.java)
                productInfoP2.productFinancingCalculationData = financingCalculationData
            }

        } catch (t: Throwable) {
            Timber.d(t)
        }

        return productInfoP2
    }

    private fun ImageReviewGqlResponse.toImageReviewItemList(): List<ImageReviewItem> {
        val images = SparseArray<ImageReviewGqlResponse.Image>()
        val reviews = SparseArray<ImageReviewGqlResponse.Review>()
        val hasNext = productReviewImageListQuery?.isHasNext ?: false

        productReviewImageListQuery?.detail?.images?.forEach { images.put(it.imageAttachmentID, it) }
        productReviewImageListQuery?.detail?.reviews?.forEach { reviews.put(it.reviewId, it) }

        return productReviewImageListQuery?.list?.map {
            val image = images[it.imageID]
            val review = reviews[it.reviewID]
            ImageReviewItem(it.reviewID.toString(), review.timeFormat?.dateTimeFmt1,
                    review.reviewer?.fullName, image.uriThumbnail,
                    image.uriLarge, review.rating, hasNext, productReviewImageListQuery?.detail?.imageCount)
        } ?: listOf()

    }
}