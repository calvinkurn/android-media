package com.tokopedia.review.feature.inbox.buyerreview.data.mapper;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.InboxReputationPojo;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.OrderData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.Paging;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.ReputationBadge;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.ReputationData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.RevieweeBuyerBadge;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.RevieweeData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.RevieweeShopBadge;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationItemDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.OrderDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.PagingDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/14/17.
 */

public class InboxReputationMapper implements Func1<Response<TokopediaWsV4Response>, InboxReputationDomain> {
    @Override
    public InboxReputationDomain call(Response<TokopediaWsV4Response> response) {

        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                InboxReputationPojo data = response.body().convertDataObj(InboxReputationPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = "";
            if (response.body() != null) {
                messageError = response.body().getErrorMessageJoined();
            }
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private InboxReputationDomain mappingToDomain(InboxReputationPojo data) {
        return new InboxReputationDomain(
                mappingToListInboxReputation(data.getInboxReputation()),
                mappingToPaging(data.getPaging())
        );
    }

    private PagingDomain mappingToPaging(@Nullable Paging paging) {
        if (paging != null)
            return new PagingDomain(
                    paging.isHasNext(),
                    paging.isHasPrev()
            );
        else
            return new PagingDomain(false, false);

    }

    private List<InboxReputationItemDomain> mappingToListInboxReputation(List<InboxReputation> inboxReputation) {
        List<InboxReputationItemDomain> list = new ArrayList<>();
        if (!inboxReputation.isEmpty()) {
            for (InboxReputation item : inboxReputation) {
                list.add(new InboxReputationItemDomain(
                        item.getInboxId(),
                        item.getShopId(),
                        item.getUserId(),
                        item.getReputationId(),
                        mappingToOrderData(item.getOrderData()),
                        mappingToRevieweeData(item.getRevieweeData()),
                        mappingToReputationData(item.getReputationData())
                ));
            }
        }
        return list;
    }

    private ReputationDataDomain mappingToReputationData(ReputationData reputationData) {
        return new ReputationDataDomain(
                reputationData.getRevieweeScore(),
                reputationData.getRevieweeScoreStatus(),
                reputationData.isShowRevieweeScore(),
                reputationData.getReviewerScore(),
                reputationData.getReviewerScoreStatus(),
                reputationData.isIsEditable(),
                reputationData.isIsInserted(),
                reputationData.isIsLocked(),
                reputationData.isIsAutoScored(),
                reputationData.isIsCompleted(),
                reputationData.isShowLockingDeadline(),
                reputationData.getLockingDeadlineDays(),
                reputationData.isShowBookmark(),
                reputationData.getActionMessage());
    }

    private OrderDataDomain mappingToOrderData(OrderData orderData) {
        return new OrderDataDomain(
                orderData.getInvoiceRefNum(),
                orderData.getCreateTimeFmt(),
                orderData.getInvoiceUrl()
        );
    }

    private RevieweeDataDomain mappingToRevieweeData(RevieweeData revieweeData) {
        return new RevieweeDataDomain(
                revieweeData.getRevieweeName(),
                revieweeData.getRevieweeUri(),
                revieweeData.getRevieweeRole(),
                revieweeData.getRevieweeRoleId(),
                revieweeData.getRevieweePicture(),
                mappingToRevieweeBadgeCustomer(revieweeData.getRevieweeBuyerBadge()),
                mappingToRevieweeBadgeSeller(revieweeData.getRevieweeShopBadge())
        );

    }

    private RevieweeBadgeCustomerDomain mappingToRevieweeBadgeCustomer(RevieweeBuyerBadge revieweeBadge) {
        return new RevieweeBadgeCustomerDomain(
                revieweeBadge.getPositive(),
                revieweeBadge.getNeutral(),
                revieweeBadge.getNegative(),
                revieweeBadge.getPositivePercentage(),
                revieweeBadge.getNoReputation()
        );
    }

    private RevieweeBadgeSellerDomain mappingToRevieweeBadgeSeller(RevieweeShopBadge revieweeBadge) {
        return new RevieweeBadgeSellerDomain(revieweeBadge.getTooltip(),
                revieweeBadge.getReputationScore(),
                revieweeBadge.getScore(),
                revieweeBadge.getMinBadgeScore(),
                revieweeBadge.getReputationBadgeUrl(),
                mappingToReputationBadge(revieweeBadge.getReputationBadge()));
    }

    private ReputationBadgeDomain mappingToReputationBadge(ReputationBadge reputationBadge) {
        return new ReputationBadgeDomain(
                reputationBadge.getLevel(),
                reputationBadge.getSet()
        );
    }
}
