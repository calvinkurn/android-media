package com.tokopedia.review.feature.reviewdetail.util.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackFilterData
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.review.feature.reviewdetail.view.model.*
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
            reviewCount = productFeedbackDataPerProduct.reviewCount
        }
    }

    fun mapToRatingBarUiModel(productFeedbackDataPerProduct: ProductFeedbackFilterData, oldData: List<RatingBarUiModel>): ProductReviewFilterUiModel {
        val ratingBarListUiModel = mutableListOf<RatingBarUiModel>()
        val totalAggregateRating: Int = productFeedbackDataPerProduct.aggregatedRating.sumBy { it.ratingCount.orZero() }

        productFeedbackDataPerProduct.aggregatedRating.mapIndexed { index, it ->
            ratingBarListUiModel.add(
                    RatingBarUiModel(
                            ratingProgressBar = if (totalAggregateRating == 0) 0F else (it.ratingCount.toFloat() / totalAggregateRating.toFloat()) * 100,
                            ratingLabel = it.rating,
                            ratingCount = it.ratingCount.orZero(),
                            ratingIsChecked = oldData.getOrNull(index)?.ratingIsChecked ?: false
                    )
            )
        }
        return ProductReviewFilterUiModel(ratingBarListUiModel)
    }

    fun mapToTopicUiModel(productFeedbackDataPerProduct: ProductFeedbackFilterData, oldData: List<SortFilterItemWrapper>): TopicUiModel {
        val topicListUiModel = TopicUiModel(countFeedback = productFeedbackDataPerProduct.reviewCount)

        productFeedbackDataPerProduct.topics.map {
            topicListUiModel.apply {
                sortFilterItemList = mapToItemSortFilter(productFeedbackDataPerProduct.topics, oldData)
                countFeedback = productFeedbackDataPerProduct.reviewCount
            }
        }
        return topicListUiModel
    }

    fun mapToFeedbackUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct?,
                             userSession: UserSessionInterface): List<FeedbackUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackUiModel>()
        productFeedbackDataPerProduct?.list?.map {
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
                            sellerUser = userSession.name,
                            isKejarUlasan = it.isKejarUlasan
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


    private fun mapToItemSortFilter(data: List<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.Topic>, oldData: List<SortFilterItemWrapper>): ArrayList<SortFilterItemWrapper> {
        val itemSortFilterList = ArrayList<SortFilterItemWrapper>()
        val maxItemSortFilter = 6
        val updatedData = updateNewDataWithOldData(data,oldData)
        val maxData = updatedData.take(maxItemSortFilter)
        maxData.map {
            val sortFilter = SortFilterItem(
                    title = it.first.formatted.orEmpty(),
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)

            itemSortFilterList.add(SortFilterItemWrapper(sortFilter, it.second, it.first.count.orZero(), it.first.title.orEmpty()))
        }
        return itemSortFilterList
    }

    private fun updateNewDataWithOldData(newData: List<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.Topic>, oldData: List<SortFilterItemWrapper>): List<Pair<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.Topic,Boolean>> {
        val updatedData = newData.mapIndexed { index, topic ->
            val lastState = oldData.getOrNull(index)?.isSelected ?: false
            topic to lastState
        }

        return updatedData
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

    fun mapToItemSortTopic(): List<SortItemUiModel> {
        val data = ReviewConstants.mapSortReviewDetail()
        val list = arrayListOf<SortItemUiModel>()

        data.map {
            if(list.size == 0) {
                list.add(SortItemUiModel(title = it.value, isSelected = true))
            } else {
                list.add(SortItemUiModel(title = it.value, isSelected = false))
            }
        }

        return list
    }

}