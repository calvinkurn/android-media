package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.sendreview;


import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 9/4/17.
 */

public class SendReviewPass {

    String reviewId;
    String productId;
    String reputationId;
    String shopId;
    String rating;
    String reviewMessage;
    ArrayList<ImageUpload> listImage;
    List<ImageUpload> listDeleted;
    boolean shareFb;
    boolean anonymous;

    public SendReviewPass(String reviewId, String productId, String reputationId, String shopId,
                          String rating, String reviewMessage, ArrayList<ImageUpload> listImage,
                          List<ImageUpload> listDeleted, boolean shareFb, boolean anonymous) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.reputationId = reputationId;
        this.shopId = shopId;
        this.rating = rating;
        this.reviewMessage = reviewMessage;
        this.listImage = listImage;
        this.listDeleted = listDeleted;
        this.shareFb = shareFb;
        this.anonymous = anonymous;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getProductId() {
        return productId;
    }

    public String getReputationId() {
        return reputationId;
    }

    public String getShopId() {
        return shopId;
    }

    public String getRating() {
        return rating;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public ArrayList<ImageUpload> getListImage() {
        return listImage;
    }

    public boolean isShareFb() {
        return shareFb;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public List<ImageUpload> getListDeleted() {
        return listDeleted;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setReputationId(String reputationId) {
        this.reputationId = reputationId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public void setListImage(ArrayList<ImageUpload> listImage) {
        this.listImage = listImage;
    }

    public void setShareFb(boolean shareFb) {
        this.shareFb = shareFb;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void setListDeleted(ArrayList<ImageUpload> listDeleted) {
        this.listDeleted = listDeleted;
    }
}
