package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.support.annotation.Nullable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemViewModel implements
        Visitable<InboxReputationDetailTypeFactory> {

    public static final int IS_LIKED = 1;
    private final String reviewId;
    private final boolean reviewHasReviewed;
    private final boolean reviewIsEditable;
    private final boolean reviewIsSkippable;
    private final int shopId;
    private final ReviewResponseViewModel reviewResponseViewModel;
    private final int reputationId;
    private final boolean reviewIsEdited;
    private final int reviewerId;
    private final boolean isProductBanned;
    String productId;
    String productName;
    String productAvatar;
    String reviewerName;
    String revieweeName;
    String reviewTime;
    ArrayList<ImageAttachmentViewModel> reviewAttachment;
    String review;
    int reviewStar;
    boolean reviewIsSkipped;
    int tab;
    private String productUrl;
    private boolean reviewIsAnonymous;
    private boolean isProductDeleted;
    private int productStatus;

    public InboxReputationDetailItemViewModel(int reputationId, String productId, String productName,
                                              String productAvatar,
                                              String productUrl,
                                              String reviewId,
                                              String reviewerName, String reviewTime,
                                              ArrayList<ImageAttachmentViewModel> reviewAttachment,
                                              String review, int reviewStar,
                                              boolean reviewHasReviewed,
                                              boolean reviewIsEditable,
                                              boolean reviewIsSkippable,
                                              boolean reviewIsSkipped,
                                              int shopId,
                                              int tab,
                                              @Nullable ReviewResponseViewModel
                                                      reviewResponseViewModel,
                                              boolean reviewIsAnonymous,
                                              boolean isProductDeleted,
                                              boolean reviewIsEdited,
                                              String revieweeName,
                                              int reviewerId,
                                              boolean isProductBanned,
                                              int productStatus) {
        this.reputationId = reputationId;
        this.productId = productId;
        this.productName = productName;
        this.productAvatar = productAvatar;
        this.productUrl = productUrl;
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.reviewTime = reviewTime;
        this.reviewAttachment = reviewAttachment;
        this.review = review;
        this.reviewStar = reviewStar;
        this.reviewIsSkipped = reviewIsSkipped;
        this.reviewHasReviewed = reviewHasReviewed;
        this.reviewIsEditable = reviewIsEditable;
        this.reviewIsSkippable = reviewIsSkippable;
        this.shopId = shopId;
        this.tab = tab;
        this.reviewResponseViewModel = reviewResponseViewModel;
        this.reviewIsAnonymous = reviewIsAnonymous;
        this.isProductDeleted = isProductDeleted;
        this.reviewIsEdited = reviewIsEdited;
        this.revieweeName = revieweeName;
        this.reviewerId = reviewerId;
        this.isProductBanned = isProductBanned;
        this.productStatus = productStatus;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductAvatar() {
        return productAvatar;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public ArrayList<ImageAttachmentViewModel> getReviewAttachment() {
        return reviewAttachment;
    }

    public String getReview() {
        return review;
    }

    public int getReviewStar() {
        return reviewStar;
    }

    public boolean isReviewSkipped() {
        return reviewIsSkipped;
    }

    public boolean isReviewHasReviewed() {
        return reviewHasReviewed;
    }

    public boolean isReviewIsEditable() {
        return reviewIsEditable;
    }

    public boolean isReviewIsSkippable() {
        return reviewIsSkippable;
    }

    public int getShopId() {
        return shopId;
    }

    public int getTab() {
        return tab;
    }

    public ReviewResponseViewModel getReviewResponseViewModel() {
        return reviewResponseViewModel;
    }

    public int getReputationId() {
        return reputationId;
    }

    @Override
    public int type(InboxReputationDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public boolean isReviewIsAnonymous() {
        return reviewIsAnonymous;
    }

    public void setReviewIsAnonymous(boolean reviewIsAnonymous) {
        this.reviewIsAnonymous = reviewIsAnonymous;
    }

    public boolean isProductDeleted() {
        return isProductDeleted;
    }

    public void setProductDeleted(boolean isProductDeleted) {
        this.isProductDeleted = isProductDeleted;
    }

    public boolean isReviewIsEdited() {
        return reviewIsEdited;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public boolean isProductBanned() {
        return isProductBanned;
    }

    public int getproductStatus() {
        return productStatus;
    }
}
