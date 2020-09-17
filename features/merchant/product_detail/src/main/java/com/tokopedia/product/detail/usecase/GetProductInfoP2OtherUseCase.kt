package com.tokopedia.product.detail.usecase

import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.product.detail.view.util.doActionIfNotNull
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yehezkiel on 27/07/20
 */
class GetProductInfoP2OtherUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                       private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2Other>() {
    companion object {
        fun createParams(productId: Int, shopId: Int): RequestParams =
                RequestParams.create().apply {
                    putInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
                }
    }

    private var requestParams: RequestParams = RequestParams.EMPTY
    private var forceRefresh: Boolean = false

    suspend fun executeOnBackground(requestParams: RequestParams, forceRefresh:Boolean) : ProductInfoP2Other{
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductInfoP2Other {
        val p2GeneralData = ProductInfoP2Other()
        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val productId = requestParams.getInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, 0)

        //region Review
        val imageReviewParams = generateImageReviewParam(productId)
        val imageReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_IMAGE_REVIEW],
                ImageReviewGqlResponse::class.java, imageReviewParams)

        val helpfulReviewParams = generateProductIdParam(productId)
        val helpfulReviewRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_MOST_HELPFUL_REVIEW],
                Review.Response::class.java, helpfulReviewParams)
        //endregion

        //region Discussion/Talk
        val discussionMostHelpfulParams = generateDiscussionMosthelpfulParam(productId.toString(), shopId.toString())
        val discussionMostHelpfulRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_DISCUSSION_MOST_HELPFUL],
                DiscussionMostHelpfulResponseWrapper::class.java, discussionMostHelpfulParams)
        //endregion


        val requests = mutableListOf(imageReviewRequest, helpfulReviewRequest, discussionMostHelpfulRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            //region Review
            if (gqlResponse.getError(Review.Response::class.java)?.isNotEmpty() != true) {
                gqlResponse.doActionIfNotNull<Review.Response> {
                    p2GeneralData.helpfulReviews = it.productMostHelpfulReviewQuery.list
                }
            }

            if (gqlResponse.getError(ImageReviewGqlResponse::class.java)?.isNotEmpty() != true) {
                gqlResponse.doActionIfNotNull<ImageReviewGqlResponse> {
                    p2GeneralData.imageReviews = DynamicProductDetailMapper.generateImageReviewUiData(it)
                }
            }
            //endregion

            //region Discussion
            if (gqlResponse.getError(DiscussionMostHelpfulResponseWrapper::class.java)?.isNotEmpty() != true) {
                gqlResponse.doActionIfNotNull<DiscussionMostHelpfulResponseWrapper> {
                    p2GeneralData.discussionMostHelpful = it.discussionMostHelpful
                }
            } else {
                p2GeneralData.discussionMostHelpful = DiscussionMostHelpful()
            }
            //endregion

        } catch (e: Throwable) {
            Timber.d(e)
        }
        return p2GeneralData
    }

    private fun generateImageReviewParam(productId: Int): Map<String, Any> {
        return mapOf(
                ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId,
                ProductDetailCommonConstant.PARAM_PAGE to 1,
                ProductDetailCommonConstant.PARAM_TOTAL to ProductDetailCommonConstant.DEFAULT_NUM_IMAGE_REVIEW)
    }

    private fun generateProductIdParam(productId: Int): Map<String, Any> {
        return mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId)
    }

    private fun generateDiscussionMosthelpfulParam(productId: String, shopId: String): Map<String, Any> {
        return mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId,
                ProductDetailCommonConstant.PARAM_SHOP_ID to shopId)
    }


}