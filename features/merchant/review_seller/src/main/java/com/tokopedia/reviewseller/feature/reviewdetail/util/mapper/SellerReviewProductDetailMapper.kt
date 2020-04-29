package com.tokopedia.reviewseller.feature.reviewdetail.util.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackFilterData
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface

object SellerReviewProductDetailMapper {

    fun mapToProductFeedbackDetailUiModel(productFeedbackDataPerProduct:
                                          ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct,
                                          userSession: UserSessionInterface): ProductFeedbackDetailUiModel {
        return ProductFeedbackDetailUiModel().apply {
            productFeedbackDetailList = mapToFeedbackUiModel(productFeedbackDataPerProduct, userSession)
            page = productFeedbackDataPerProduct.page ?: 0
            hasNext = productFeedbackDataPerProduct.hasNext
        }
    }

    fun mapToRatingBarUiModel(productFeedbackDataPerProduct: ProductFeedbackFilterData, oldData: List<RatingBarUiModel>): ProductReviewFilterUiModel {
        val ratingBarListUiModel = mutableListOf<RatingBarUiModel>()
        val totalAggregatRating: Int = productFeedbackDataPerProduct.aggregatedRating.sumBy { it.ratingCount }

        productFeedbackDataPerProduct.aggregatedRating.mapIndexed { index, it ->
            ratingBarListUiModel.add(
                    RatingBarUiModel(
                            ratingProgressBar = if (totalAggregatRating == 0) 0F else (it.ratingCount.toFloat() / totalAggregatRating.toFloat()) * 100,
                            ratingLabel = it.rating,
                            ratingCount = it.ratingCount,
                            ratingIsChecked = oldData.getOrNull(index)?.ratingIsChecked ?: false
                    )
            )
        }
        return ProductReviewFilterUiModel(ratingBarListUiModel)
    }

    fun mapToTopicUiModel(productFeedbackDataPerProduct: ProductFeedbackFilterData): TopicUiModel {
        val topicListUiModel = TopicUiModel(countFeedback = productFeedbackDataPerProduct.reviewCount)

        productFeedbackDataPerProduct.topics.map {
            topicListUiModel.apply {
                sortFilterItemList = mapToItemSortFilter(productFeedbackDataPerProduct.topics)
                countFeedback = productFeedbackDataPerProduct.reviewCount
            }
        }
        return topicListUiModel
    }

    fun mapToFeedbackUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct,
                             userSession: UserSessionInterface): List<FeedbackUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackUiModel>()
        productFeedbackDataPerProduct.list.map {
            val mapAttachment = mutableListOf<FeedbackUiModel.Attachment>()
            it.attachments.map { attachment ->
                mapAttachment.add(FeedbackUiModel.Attachment(
                        thumbnailURL = attachment.thumbnailURL,
                        fullSizeURL = attachment.fullSizeURL
                ))
            }
            feedbackListUiModel.add(
                    FeedbackUiModel(
                            attachments = mapAttachment,
                            autoReply = it.autoReply,
                            feedbackID = it.feedbackID,
                            rating = it.rating,
                            replyText = it.replyText,
                            replyTime = it.replyTime,
                            reviewText = it.reviewText,
                            isMoreReply = it.replyText?.length.orZero() >= 150,
                            reviewTime = it.reviewTime,
                            reviewerName = it.reviewerName,
                            variantName = it.variantName,
                            sellerUser = userSession.name
                    )
            )
        }

        return feedbackListUiModel
    }


    fun mapToRatingDetailOverallUiModel(productFeedbackDetailResponse:
                                        ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct,
                                        chipFilterString: String): OverallRatingDetailUiModel {
        return OverallRatingDetailUiModel().apply {
            productName = productFeedbackDetailResponse.productName
            ratingAvg = productFeedbackDetailResponse.ratingAverage
            reviewCount = productFeedbackDetailResponse.ratingCount
            chipFilter = chipFilterString
        }
    }

    fun mapToItemUnifyMenuOption(context: Context): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()

        itemUnifyList.apply {
            add(
                    ListItemUnify(
                            title = context.getString(R.string.change_product_label),
                            description = "")
            )
        }

        return itemUnifyList
    }

    fun mapToItemUnifyListFeedback(context: Context, isEmptyReply: Boolean): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        val iconSize = 24.toPx()

        itemUnifyList.apply {
            add(
                    if (!isEmptyReply) {
                        val iconList = ContextCompat.getDrawable(context, R.drawable.ic_pencil_edit)
                        ListItemUnify(title = context.getString(R.string.edit_review_label), description = "").apply {
                            listDrawable = iconList
                            listIconHeight = iconSize
                            listIconWidth = iconSize
                        }
                    } else {
                        val iconList = ContextCompat.getDrawable(context, R.drawable.ic_sent)
                        ListItemUnify(title = context.getString(R.string.review_reply_label), description = "").apply {
                            listDrawable = iconList
                            listIconHeight = iconSize
                            listIconWidth = iconSize
                        }
                    }
            )
            val iconReport = ContextCompat.getDrawable(context, R.drawable.ic_report_flag)
            add(
                    ListItemUnify(title = context.getString(R.string.report_label), description = "").apply {
                        listDrawable = iconReport
                        listIconHeight = iconSize
                        listIconWidth = iconSize
                    }
            )
        }

        return itemUnifyList
    }


    private fun mapToItemSortFilter(data: List<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.Topic>): ArrayList<Triple<SortFilterItem, Boolean, Int>> {
        val itemSortFilterList = ArrayList<Triple<SortFilterItem, Boolean, Int>>()
        val maxData = data.take(4)
        maxData.map {
            val sortFilter = SortFilterItem(
                    title = it.formatted,
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)

            itemSortFilterList.add(Triple(sortFilter, ReviewSellerConstant.TOPIC_POPULAR_UNSELECTED, it.count))
        }
        return itemSortFilterList
    }

    fun mapToItemImageSlider(attachmentList: List<FeedbackUiModel.Attachment>?): Pair<List<String>, List<String>> {
        val imageSlider = arrayListOf<String>()
        val thumbnailSlider = arrayListOf<String>()

        attachmentList?.map {
            thumbnailSlider.add(it.thumbnailURL.orEmpty())
            imageSlider.add(it.fullSizeURL.orEmpty())
        }

        return Pair(thumbnailSlider, imageSlider)
    }

}