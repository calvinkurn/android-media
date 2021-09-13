package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.*
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.*
import rx.Subscriber
import java.util.*

/**
 * @author by nisie on 8/19/17.
 */
open class GetInboxReputationDetailSubscriber constructor(protected val viewListener: InboxReputationDetail.View?) :
    Subscriber<InboxReputationDetailDomain?>() {
    public override fun onCompleted() {}
    public override fun onError(e: Throwable) {
        viewListener!!.finishLoading()
        viewListener.onErrorGetInboxDetail(e)
    }

    public override fun onNext(inboxReputationDetailDomain: InboxReputationDetailDomain) {
        viewListener!!.finishLoading()
        viewListener.onSuccessGetInboxDetail(
            convertToReputationViewModel(inboxReputationDetailDomain.getInboxReputationDomain()).getList()
                .get(0),
            mappingToListItemViewModel(inboxReputationDetailDomain.getReviewDomain())
        )
    }

    private fun convertToReputationBadgeViewModel(reputationBadge: ReputationBadgeDomain?): ReputationBadgeUiModel {
        return ReputationBadgeUiModel(
            reputationBadge.getLevel(),
            reputationBadge.getSet()
        )
    }

    protected fun mappingToListItemViewModel(reviewDomain: ReviewDomain?): List<Visitable<*>?> {
        val list: MutableList<Visitable<*>?> = ArrayList()
        if (reviewDomain.getData() != null) {
            for (detailDomain: ReviewItemDomain? in reviewDomain.getData()) {
                list.add(
                    convertToInboxReputationDetailItemViewModel(
                        reviewDomain,
                        detailDomain
                    )
                )
            }
        }
        return list
    }

    private fun convertToInboxReputationDetailItemViewModel(
        reviewDomain: ReviewDomain?, itemDomain: ReviewItemDomain?
    ): Visitable<*> {
        return InboxReputationDetailItemUiModel(
            reviewDomain.getReputationId(),
            itemDomain.getProductData().getProductId().toString(),
            itemDomain.getProductData().getProductName(),
            itemDomain.getProductData().getProductImageUrl(),
            itemDomain.getProductData().getProductPageUrl(),
            itemDomain.getReviewData().getReviewId().toString(),
            reviewDomain.getUserData().getFullName(),
            if (TextUtils.isEmpty(
                    itemDomain.getReviewData().getReviewUpdateTime().getDateTimeFmt1()
                )
            ) itemDomain.getReviewData().getReviewCreateTime().getDateTimeFmt1() else itemDomain
                .getReviewData().getReviewUpdateTime().getDateTimeFmt1(),
            convertToImageAttachmentViewModel(itemDomain.getReviewData().getReviewImageUrl()),
            itemDomain.getReviewData().getReviewMessage(),
            itemDomain.getReviewData().getReviewRating(),
            itemDomain!!.isReviewHasReviewed(),
            itemDomain!!.isReviewIsEditable(),
            itemDomain!!.isReviewIsSkippable(),
            itemDomain!!.isReviewIsSkipped(),
            reviewDomain.getShopData().getShopId(),
            viewListener.getTab(),
            convertToReviewResponseViewModel(
                reviewDomain.getShopData(),
                itemDomain.getReviewData()
                    .getReviewResponse()
            ),
            itemDomain.getReviewData().isReviewAnonymity(),
            itemDomain.getProductData().getProductStatus() == PRODUCT_IS_DELETED,
            !TextUtils.isEmpty(
                itemDomain.getReviewData().getReviewUpdateTime()
                    .getDateTimeFmt1()
            ),
            reviewDomain.getShopData().getShopName(),
            reviewDomain.getUserData().getUserId(),
            itemDomain.getProductData().getProductStatus() == PRODUCT_IS_BANNED,
            itemDomain.getProductData().getProductStatus(),
            reviewDomain.getOrderId()
        )
    }

    private fun convertToReviewResponseViewModel(
        shopData: ShopDataDomain?,
        reviewResponse: ReviewResponseDomain?
    ): ReviewResponseUiModel? {
        if (reviewResponse != null && shopData != null) return ReviewResponseUiModel(
            reviewResponse.getResponseMessage(),
            reviewResponse.getResponseCreateTime().getDateTimeFmt1(),
            shopData.getShopName()
        ) else return null
    }

    private fun convertToImageAttachmentViewModel(reviewImageUrl: List<ImageAttachmentDomain?>?): ArrayList<ImageAttachmentUiModel> {
        val list: ArrayList<ImageAttachmentUiModel> = ArrayList()
        for (domain: ImageAttachmentDomain? in reviewImageUrl!!) {
            list.add(
                ImageAttachmentUiModel(
                    domain.getAttachmentId(),
                    domain.getDescription(),
                    domain.getUriThumbnail(),
                    domain.getUriLarge()
                )
            )
        }
        return list
    }

    protected fun convertToReputationViewModel(inboxReputationDomain: InboxReputationDomain?): InboxReputationUiModel {
        return InboxReputationUiModel(
            convertToInboxReputationList(inboxReputationDomain.getInboxReputation()),
            inboxReputationDomain.getPaging().isHasNext()
        )
    }

    private fun convertToInboxReputationList(inboxReputationDomain: List<InboxReputationItemDomain?>?): List<InboxReputationItemUiModel?> {
        val list: MutableList<InboxReputationItemUiModel?> = ArrayList()
        for (domain: InboxReputationItemDomain? in inboxReputationDomain!!) {
            list.add(
                InboxReputationItemUiModel(
                    domain.getReputationId().toString(),
                    domain.getRevieweeData().getRevieweeName(),
                    domain.getOrderData().getCreateTimeFmt(),
                    domain.getRevieweeData().getRevieweePicture(),
                    domain.getReputationData().getLockingDeadlineDays().toString(),
                    domain.getOrderData().getInvoiceRefNum(),
                    convertToReputationViewModel(domain.getReputationData()),
                    domain.getRevieweeData().getRevieweeRoleId(),
                    convertToBuyerReputationViewModel(
                        domain.getRevieweeData()
                            .getRevieweeBadgeCustomer()
                    ),
                    convertToSellerReputationViewModel(
                        domain.getRevieweeData()
                            .getRevieweeBadgeSeller()
                    ),
                    domain.getShopId(),
                    domain.getUserId()
                )
            )
        }
        return list
    }

    private fun convertToSellerReputationViewModel(revieweeBadgeSeller: RevieweeBadgeSellerDomain?): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            revieweeBadgeSeller.getTooltip(),
            revieweeBadgeSeller.getReputationScore(),
            revieweeBadgeSeller.getScore(),
            revieweeBadgeSeller.getMinBadgeScore(),
            revieweeBadgeSeller.getReputationBadgeUrl(),
            convertToReputationBadgeViewModel(revieweeBadgeSeller.getReputationBadge()),
            revieweeBadgeSeller.getIsFavorited()
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: RevieweeBadgeCustomerDomain?
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.getPositive(),
            revieweeBadgeCustomer.getNeutral(), revieweeBadgeCustomer.getNegative(),
            revieweeBadgeCustomer.getPositivePercentage(),
            revieweeBadgeCustomer.getNoReputation()
        )
    }

    private fun convertToReputationViewModel(reputationData: ReputationDataDomain?): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.getRevieweeScore(),
            reputationData.getRevieweeScoreStatus(),
            reputationData!!.isShowRevieweeScore(),
            reputationData.getReviewerScore(),
            reputationData.getReviewerScoreStatus(),
            reputationData!!.isEditable(),
            reputationData!!.isInserted(),
            reputationData!!.isLocked(),
            reputationData!!.isAutoScored(),
            reputationData!!.isCompleted(),
            reputationData!!.isShowLockingDeadline(),
            reputationData.getLockingDeadlineDays(),
            reputationData!!.isShowBookmark(),
            reputationData.getActionMessage()
        )
    }

    companion object {
        val PRODUCT_IS_DELETED: Int = 0
        val PRODUCT_IS_BANNED: Int = -2
    }
}