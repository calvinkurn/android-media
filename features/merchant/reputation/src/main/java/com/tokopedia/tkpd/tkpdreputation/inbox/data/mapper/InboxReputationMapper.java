package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.InboxReputationPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.OrderData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.Paging;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.ReputationBadge;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.ReputationData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.RevieweeBuyerBadge;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.RevieweeData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox.RevieweeShopBadge;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.OrderDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.PagingDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationBadgeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeDataDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/14/17.
 */

public class InboxReputationMapper implements Func1<Response<TkpdResponse>, InboxReputationDomain> {
    @Override
    public InboxReputationDomain call(Response<TkpdResponse> response) {

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
            String messageError = ErrorHandler.getErrorMessage(response);
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
