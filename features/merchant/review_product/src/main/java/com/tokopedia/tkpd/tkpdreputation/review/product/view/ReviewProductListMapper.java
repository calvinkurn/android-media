package com.tokopedia.tkpd.tkpdreputation.review.product.view;

import android.text.TextUtils;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ImageAttachmentUiModel;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Owner;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.ReviewImageAttachment;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ReviewProductListMapper {

    public static final int LIKE_STATUS_ACTIVE = 1;

    @Inject
    public ReviewProductListMapper() {
    }

    public List<ReviewProductModel> map(DataResponseReviewProduct dataResponseReviewProduct, String userId) {
        List<ReviewProductModel> reviewModelContents = new ArrayList<>();
        for(Review review : dataResponseReviewProduct.getList()){
            ReviewProductModelContent productReviewModelContent = new ReviewProductModelContent();
            productReviewModelContent.setResponseCreateTime(review.getReviewResponse().getResponseTime().getDateTimeFmt1());
            productReviewModelContent.setResponseMessage(review.getReviewResponse().getResponseMessage());
            productReviewModelContent.setReviewCanReported(isReviewCanReported(userId, review));
            productReviewModelContent.setReputationId(String.valueOf(review.getReputationId()));
            productReviewModelContent.setProductId(String.valueOf(dataResponseReviewProduct.getProduct().getProductId()));
            productReviewModelContent.setReviewerId(String.valueOf(review.getUser().getUserId()));
            productReviewModelContent.setReviewAttachment(generateImageAttachmentModel(review));
            productReviewModelContent.setReviewIsAnonymous(review.getReviewAnonymous() == 1);
            productReviewModelContent.setReviewStar(review.getProductRating());
            productReviewModelContent.setSellerName(dataResponseReviewProduct.getOwner().getShop().getShopName());
            productReviewModelContent.setReviewTime(getReviewCreateTime(review));
            productReviewModelContent.setReviewMessage(review.getReviewMessage());
            productReviewModelContent.setReviewerName(review.getUser().getFullName());
            productReviewModelContent.setReviewHasReplied(!TextUtils.isEmpty(review.getReviewResponse().getResponseMessage()));
            productReviewModelContent.setSellerRepliedOwner(isUserOwnedReplied(userId, dataResponseReviewProduct.getOwner()));
            productReviewModelContent.setShopId(String.valueOf(dataResponseReviewProduct.getOwner().getShop().getShopId()));
            productReviewModelContent.setLikeStatus(review.getLikeStatus() == LIKE_STATUS_ACTIVE);
            productReviewModelContent.setTotalLike(review.getTotalLike());
            productReviewModelContent.setLogin(!TextUtils.isEmpty(userId));
            productReviewModelContent.setReviewId(String.valueOf(review.getReviewId()));
            reviewModelContents.add(productReviewModelContent);
        }
        return reviewModelContents;
    }

    public List<ReviewProductModel> map(DataResponseReviewHelpful dataResponseReviewHelpful, String userId, String productId){
        List<ReviewProductModel> reviewModelContents = new ArrayList<>();
        for(Review review : dataResponseReviewHelpful.getList()){
            ReviewProductModelContent productReviewModelContent = new ReviewProductModelContent();
            productReviewModelContent.setResponseCreateTime(review.getReviewResponse().getResponseTime().getDateTimeFmt1());
            productReviewModelContent.setResponseMessage(review.getReviewResponse().getResponseMessage());
            productReviewModelContent.setReviewCanReported(isReviewCanReported(userId, review));
            productReviewModelContent.setReputationId(String.valueOf(review.getReputationId()));
            productReviewModelContent.setReviewerId(String.valueOf(review.getUser().getUserId()));
            productReviewModelContent.setReviewAttachment(generateImageAttachmentModel(review));
            productReviewModelContent.setReviewIsAnonymous(review.getReviewAnonymous() == 1);
            productReviewModelContent.setReviewStar(review.getReviewStar());
            productReviewModelContent.setSellerName(dataResponseReviewHelpful.getOwner().getShop().getShopName());
            productReviewModelContent.setReviewTime(getReviewCreateTime(review));
            productReviewModelContent.setReviewMessage(review.getReviewMessage());
            productReviewModelContent.setReviewerName(review.getUser().getFullName());
            productReviewModelContent.setReviewHasReplied(!TextUtils.isEmpty(review.getReviewResponse().getResponseMessage()));
            productReviewModelContent.setSellerRepliedOwner(isUserOwnedReplied(userId, dataResponseReviewHelpful.getOwner()));
            productReviewModelContent.setShopId(String.valueOf(dataResponseReviewHelpful.getOwner().getShop().getShopId()));
            productReviewModelContent.setLikeStatus(review.getLikeStatus() == LIKE_STATUS_ACTIVE);
            productReviewModelContent.setTotalLike(review.getTotalLike());
            productReviewModelContent.setReviewId(String.valueOf(review.getReviewId()));
            productReviewModelContent.setLogin(!TextUtils.isEmpty(userId));
            productReviewModelContent.setProductId(productId);
            productReviewModelContent.setHelpful(true);
            reviewModelContents.add(productReviewModelContent);
        }
        return reviewModelContents;
    }

    private boolean isUserOwnedReplied(String userId, Owner owner) {
        return userId.equals(String.valueOf(owner.getUser().getUserId()));
    }

    private List<ImageAttachmentUiModel> generateImageAttachmentModel(Review review) {
        List<ImageAttachmentUiModel> imageAttachmentUiModels = new ArrayList<>();
        for(ReviewImageAttachment reviewImageAttachment :review.getReviewImageAttachment()){
            ImageAttachmentUiModel imageAttachmentUiModel = new ImageAttachmentUiModel(reviewImageAttachment.getAttachmentId(),
                    reviewImageAttachment.getDescription(), reviewImageAttachment.getUriThumbnail(), reviewImageAttachment.getUriLarge());
            imageAttachmentUiModels.add(imageAttachmentUiModel);
        }
        return imageAttachmentUiModels;
    }

    private boolean isReviewCanReported(String userId, Review review) {
        return !TextUtils.isEmpty(userId) && !userId.equals(String.valueOf(review.getUser().getUserId()));
    }

    private String getReviewCreateTime(Review review) {
        if(!TextUtils.isEmpty(review.getReviewUpdateTime().getDateTimeFmt1())){
            return review.getReviewUpdateTime().getDateTimeFmt1();
        }else {
            return review.getReviewCreateTime().getDateTimeFmt1();
        }
    }

}
