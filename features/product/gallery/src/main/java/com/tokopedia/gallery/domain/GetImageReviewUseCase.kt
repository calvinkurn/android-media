package com.tokopedia.gallery.domain

import android.content.Context

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gallery.R
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.gallery.viewmodel.ImageReviewListModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.ArrayList
import java.util.HashMap

import rx.Observable

open class GetImageReviewUseCase(private val context: Context?,
                            private val graphqlUseCase: GraphqlUseCase) : UseCase<ImageReviewListModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ImageReviewListModel> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context?.resources,
                R.raw.gql_image_review), ImageReviewGqlResponse::class.java, requestParams.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map { graphqlResponse ->
                    val imageReviewGqlResponse = graphqlResponse.getData<ImageReviewGqlResponse>(ImageReviewGqlResponse::class.java)
                    ImageReviewListModel(
                            convertToImageReviewItemList(imageReviewGqlResponse),
                            imageReviewGqlResponse.productReviewImageListQuery!!.isHasNext
                    )
                }
    }

    private fun convertToImageReviewItemList(gqlResponse: ImageReviewGqlResponse): List<ImageReviewItem> {
        val reviewMap = HashMap<Int, ImageReviewGqlResponse.Review>()
        val imageMap = HashMap<Int, ImageReviewGqlResponse.Image>()

        val reviewImageListQueryResponse = gqlResponse.productReviewImageListQuery

        for (image in reviewImageListQueryResponse!!.detail!!.images!!) {
            imageMap[image.imageAttachmentID] = image
        }

        for (review in reviewImageListQueryResponse.detail!!.reviews!!) {
            reviewMap[review.reviewId] = review
        }

        val imageReviewItems = ArrayList<ImageReviewItem>()
        for (item in reviewImageListQueryResponse.list!!) {
            val image = imageMap[item.imageID]
            val review = reviewMap[item.reviewID]

            val imageReviewItem = ImageReviewItem()
            imageReviewItem.reviewId = item.reviewID.toString()
            imageReviewItem.imageUrlLarge = image?.uriLarge
            imageReviewItem.imageUrlThumbnail = image?.uriThumbnail
            imageReviewItem.formattedDate = review?.timeFormat!!.dateTimeFmt1
            imageReviewItem.rating = review.rating
            imageReviewItem.reviewerName = review.reviewer!!.fullName
            imageReviewItems.add(imageReviewItem)
        }

        return imageReviewItems
    }

    companion object {

        val KEY_PRODUCT_ID = "productID"
        val KEY_PAGE = "page"
        val KEY_TOTAL = "total"

        fun createRequestParams(page: Int,
                                total: Int,
                                productId: Int): RequestParams {

            val requestParams = RequestParams.create()
            requestParams.putInt(KEY_PRODUCT_ID, productId)
            requestParams.putInt(KEY_PAGE, page)
            requestParams.putInt(KEY_TOTAL, total)
            return requestParams
        }
    }
}
