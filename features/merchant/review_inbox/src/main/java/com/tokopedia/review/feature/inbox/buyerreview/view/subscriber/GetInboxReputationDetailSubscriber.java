package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationItemDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ImageAttachmentDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewItemDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewResponseDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageAttachmentUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReputationBadgeUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReviewResponseUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 8/19/17.
 */

public class GetInboxReputationDetailSubscriber extends Subscriber<InboxReputationDetailDomain> {

    public static final int PRODUCT_IS_DELETED = 0;
    public static final int PRODUCT_IS_BANNED = -2;
    protected final InboxReputationDetail.View viewListener;

    public GetInboxReputationDetailSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoading();
        viewListener.onErrorGetInboxDetail(e);
    }

    @Override
    public void onNext(InboxReputationDetailDomain inboxReputationDetailDomain) {
        viewListener.finishLoading();
        viewListener.onSuccessGetInboxDetail(
                convertToReputationViewModel(inboxReputationDetailDomain.getInboxReputationDomain
                        ()).getList().get(0),
                mappingToListItemViewModel(inboxReputationDetailDomain.getReviewDomain())
        );
    }

    private ReputationBadgeUiModel convertToReputationBadgeViewModel(ReputationBadgeDomain reputationBadge) {
        return new ReputationBadgeUiModel(reputationBadge.getLevel(),
                reputationBadge.getSet());
    }

    protected List<Visitable> mappingToListItemViewModel(ReviewDomain
                                                                 reviewDomain) {
        List<Visitable> list = new ArrayList<>();
        if (reviewDomain.getData() != null) {
            for (ReviewItemDomain detailDomain : reviewDomain.getData()) {
                list.add(convertToInboxReputationDetailItemViewModel(reviewDomain,
                        detailDomain));
            }
        }
        return list;

    }

    private Visitable convertToInboxReputationDetailItemViewModel(
            ReviewDomain reviewDomain, ReviewItemDomain itemDomain) {
        return new InboxReputationDetailItemUiModel(
                reviewDomain.getReputationId(),
                String.valueOf(itemDomain.getProductData().getProductId()),
                itemDomain.getProductData().getProductName(),
                itemDomain.getProductData().getProductImageUrl(),
                itemDomain.getProductData().getProductPageUrl(),
                String.valueOf(itemDomain.getReviewData().getReviewId()),
                reviewDomain.getUserData().getFullName(),
                TextUtils.isEmpty(itemDomain.getReviewData().getReviewUpdateTime().getDateTimeFmt1()) ?
                        itemDomain.getReviewData().getReviewCreateTime().getDateTimeFmt1() : itemDomain
                        .getReviewData().getReviewUpdateTime().getDateTimeFmt1(),
                convertToImageAttachmentViewModel(itemDomain.getReviewData().getReviewImageUrl()),
                itemDomain.getReviewData().getReviewMessage(),
                itemDomain.getReviewData().getReviewRating(),
                itemDomain.isReviewHasReviewed(),
                itemDomain.isReviewIsEditable(),
                itemDomain.isReviewIsSkippable(),
                itemDomain.isReviewIsSkipped(),
                reviewDomain.getShopData().getShopId(),
                viewListener.getTab(),
                convertToReviewResponseViewModel(reviewDomain.getShopData(),
                        itemDomain.getReviewData()
                                .getReviewResponse()),
                itemDomain.getReviewData().isReviewAnonymity(),
                itemDomain.getProductData().getProductStatus() == PRODUCT_IS_DELETED,
                !TextUtils.isEmpty(itemDomain.getReviewData().getReviewUpdateTime()
                        .getDateTimeFmt1()),
                reviewDomain.getShopData().getShopName(),
                reviewDomain.getUserData().getUserId(),
                itemDomain.getProductData().getProductStatus() == PRODUCT_IS_BANNED,
                itemDomain.getProductData().getProductStatus(),
                reviewDomain.getOrderId()
                );
    }

    private ReviewResponseUiModel convertToReviewResponseViewModel(@Nullable ShopDataDomain shopData,
                                                                   @Nullable ReviewResponseDomain
                                                                             reviewResponse) {
        if (reviewResponse != null && shopData != null)
            return new ReviewResponseUiModel(
                    reviewResponse.getResponseMessage(),
                    reviewResponse.getResponseCreateTime().getDateTimeFmt1(),
                    shopData.getShopName()
            );
        else return null;
    }

    private ArrayList<ImageAttachmentUiModel>
    convertToImageAttachmentViewModel(List<ImageAttachmentDomain> reviewImageUrl) {
        ArrayList<ImageAttachmentUiModel> list = new ArrayList<>();
        for (ImageAttachmentDomain domain : reviewImageUrl) {
            list.add(new ImageAttachmentUiModel(
                    domain.getAttachmentId(),
                    domain.getDescription(),
                    domain.getUriThumbnail(),
                    domain.getUriLarge()
            ));
        }
        return list;
    }


    protected InboxReputationUiModel convertToReputationViewModel(InboxReputationDomain inboxReputationDomain) {
        return new InboxReputationUiModel
                (convertToInboxReputationList(inboxReputationDomain.getInboxReputation()),
                        inboxReputationDomain.getPaging().isHasNext()
                );
    }

    private List<InboxReputationItemUiModel> convertToInboxReputationList(List<InboxReputationItemDomain> inboxReputationDomain) {
        List<InboxReputationItemUiModel> list = new ArrayList<>();
        for (InboxReputationItemDomain domain : inboxReputationDomain) {
            list.add(new InboxReputationItemUiModel(
                    String.valueOf(domain.getReputationId()),
                    domain.getRevieweeData().getRevieweeName(),
                    domain.getOrderData().getCreateTimeFmt(),
                    domain.getRevieweeData().getRevieweePicture(),
                    String.valueOf(domain.getReputationData().getLockingDeadlineDays()),
                    domain.getOrderData().getInvoiceRefNum(),
                    convertToReputationViewModel(domain.getReputationData()),
                    domain.getRevieweeData().getRevieweeRoleId(),
                    convertToBuyerReputationViewModel(domain.getRevieweeData()
                            .getRevieweeBadgeCustomer()),
                    convertToSellerReputationViewModel(domain.getRevieweeData()
                            .getRevieweeBadgeSeller()),
                    domain.getShopId(),
                    domain.getUserId()));
        }
        return list;
    }

    private RevieweeBadgeSellerUiModel convertToSellerReputationViewModel(RevieweeBadgeSellerDomain revieweeBadgeSeller) {
        return new RevieweeBadgeSellerUiModel(revieweeBadgeSeller.getTooltip(),
                revieweeBadgeSeller.getReputationScore(),
                revieweeBadgeSeller.getScore(),
                revieweeBadgeSeller.getMinBadgeScore(),
                revieweeBadgeSeller.getReputationBadgeUrl(),
                convertToReputationBadgeViewModel(revieweeBadgeSeller.getReputationBadge()), revieweeBadgeSeller.getIsFavorited());
    }

    private RevieweeBadgeCustomerUiModel convertToBuyerReputationViewModel(
            RevieweeBadgeCustomerDomain revieweeBadgeCustomer) {
        return new RevieweeBadgeCustomerUiModel(revieweeBadgeCustomer.getPositive(),
                revieweeBadgeCustomer.getNeutral(), revieweeBadgeCustomer.getNegative(),
                revieweeBadgeCustomer.getPositivePercentage(),
                revieweeBadgeCustomer.getNoReputation());
    }

    private ReputationDataUiModel convertToReputationViewModel(ReputationDataDomain reputationData) {
        return new ReputationDataUiModel(reputationData.getRevieweeScore(),
                reputationData.getRevieweeScoreStatus(),
                reputationData.isShowRevieweeScore(),
                reputationData.getReviewerScore(),
                reputationData.getReviewerScoreStatus(),
                reputationData.isEditable(),
                reputationData.isInserted(),
                reputationData.isLocked(),
                reputationData.isAutoScored(),
                reputationData.isCompleted(),
                reputationData.isShowLockingDeadline(),
                reputationData.getLockingDeadlineDays(),
                reputationData.isShowBookmark(),
                reputationData.getActionMessage());
    }
}
