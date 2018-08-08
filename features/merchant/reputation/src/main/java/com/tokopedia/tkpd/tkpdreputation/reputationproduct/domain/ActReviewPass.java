package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.ImageUpload;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nisie on 2/12/16.
 */
public class ActReviewPass implements Parcelable{

    private static final String PARAM_REPUTATION_ID = "reputation_id";
    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_REVIEW_MESSAGE = "review_message";
    private static final String PARAM_QUALITY_RATE = "rate_quality";
    private static final String PARAM_ACCURACY_RATE = "rate_accuracy";
    private static final String PARAM_RESPONSE_MESSAGE = "response_message";
    private static final String PARAM_BUYER_SELLER = "buyer_seller";
    private static final String PARAM_REPUTATION_SCORE = "reputation_score";
    private static final String PARAM_HAS_PRODUCT_REVIEW_PHOTO = "has_product_review_photo";
    private static final String PARAM_REVIEW_PHOTO_ALL = "product_review_photo_all";
    private static final String PARAM_REVIEW_PHOTO_OBJ = "product_review_photo_obj";
    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";
    private static final String PARAM_REPORT_MESSAGE = "text_message";

    private static final String PARAM_ATTACHMENT_ID = "attachment_id";
    private static final String PARAM_FILE_DESC = "file_desc";
    private static final String PARAM_IS_DELETED = "is_deleted";

    public static final java.lang.String NEW_IMAGE_INDICATOR = "image";

    private static final String PARAM_LIKE_STATUS = "like_status";

    String reputationId;
    String reviewId;
    String shopId;
    String productId;
    String reviewMessage;
    String qualityRate;
    String accuracyRate;
    String responseMessage;
    String reputationScore;
    String role;
    List<ImageUpload> imageUploads;
    GeneratedHost generatedHost;

    String postKey;
    String reportMessage;

    String token;

    List<ImageUpload> deletedImageUploads;

    String likeStatus;

    public ActReviewPass() {
    }

    protected ActReviewPass(Parcel in) {
        reputationId = in.readString();
        reviewId = in.readString();
        shopId = in.readString();
        productId = in.readString();
        reviewMessage = in.readString();
        qualityRate = in.readString();
        accuracyRate = in.readString();
        responseMessage = in.readString();
        reputationScore = in.readString();
        role = in.readString();
        imageUploads = in.createTypedArrayList(ImageUpload.CREATOR);
        generatedHost = in.readParcelable(GeneratedHost.class.getClassLoader());
        postKey = in.readString();
        reportMessage = in.readString();
        token = in.readString();
        deletedImageUploads = in.createTypedArrayList(ImageUpload.CREATOR);
    }

    public static final Creator<ActReviewPass> CREATOR = new Creator<ActReviewPass>() {
        @Override
        public ActReviewPass createFromParcel(Parcel in) {
            return new ActReviewPass(in);
        }

        @Override
        public ActReviewPass[] newArray(int size) {
            return new ActReviewPass[size];
        }
    };

    public String getReputationId() {
        return reputationId;
    }

    public void setReputationId(String reputationId) {
        this.reputationId = reputationId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public String getQualityRate() {
        return qualityRate;
    }

    public void setQualityRate(String qualityRate) {
        this.qualityRate = qualityRate;
    }

    public String getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(String accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }

    public List<ImageUpload> getImageUploads() {
        return imageUploads;
    }

    public void setImageUploads(List<ImageUpload> imageUploads) {
        this.imageUploads = imageUploads;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ImageUpload> getDeletedImageUploads() {
        return deletedImageUploads;
    }

    public void setDeletedImageUploads(List<ImageUpload> deletedImageUploads) {
        this.deletedImageUploads = deletedImageUploads;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    public Map<String, String> getInsertReputationReviewParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ACCURACY_RATE, getAccuracyRate());
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_QUALITY_RATE, getQualityRate());
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_REVIEW_MESSAGE, getReviewMessage());
        params.put(PARAM_SHOP_ID, getShopId());
        params.put(PARAM_HAS_PRODUCT_REVIEW_PHOTO, "0");
        return params;
    }

    public Map<String, String> getSkipParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_SHOP_ID, getShopId());
        return params;
    }

    public Map<String, String> getReplyParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_RESPONSE_MESSAGE, getResponseMessage());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_SHOP_ID, getShopId());
        return params;
    }

    public Map<String, String> getDeleteParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_SHOP_ID, getShopId());
        return params;
    }

    public Map<String, String> getInsertReputationParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_BUYER_SELLER, getRole());
        params.put(PARAM_REPUTATION_SCORE, getReputationScore());
        return params;
    }


    public Map<String, String> getInsertReputationReviewParamWithImages() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ACCURACY_RATE, getAccuracyRate());
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_QUALITY_RATE, getQualityRate());
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_REVIEW_MESSAGE, getReviewMessage());
        params.put(PARAM_SHOP_ID, getShopId());
        params.put(PARAM_HAS_PRODUCT_REVIEW_PHOTO, "1");
        params.put(PARAM_REVIEW_PHOTO_ALL, getReviewPhotos());
        params.put(PARAM_REVIEW_PHOTO_OBJ, getReviewPhotosObj());
        return params;
    }

    private String getReviewPhotosObj() {

        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : imageUploads) {
                JSONObject photoObj = new JSONObject();
                photoObj.put(PARAM_FILE_DESC, image.getDescription());
                photoObj.put(PARAM_IS_DELETED, "0");
                if (isNewImage(image.getImageId())) {
                    photoObj.put(PARAM_ATTACHMENT_ID, "0");
                } else {
                    photoObj.put(PARAM_ATTACHMENT_ID, image.getImageId());
                }
                reviewPhotos.put(image.getImageId(), photoObj);
            }
            for (ImageUpload image : deletedImageUploads) {
                JSONObject photoObj = new JSONObject();
                photoObj.put(PARAM_FILE_DESC, image.getDescription());
                photoObj.put(PARAM_IS_DELETED, "1");
                if (isNewImage(image.getImageId())) {
                    photoObj.put(PARAM_ATTACHMENT_ID, "0");
                } else {
                    photoObj.put(PARAM_ATTACHMENT_ID, image.getImageId());
                }
                reviewPhotos.put(image.getImageId(), photoObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    private boolean isNewImage(String imageId) {
        return imageId.startsWith(NEW_IMAGE_INDICATOR);
    }

    private String getReviewPhotos() {
        String allPhoto = "";
        for (int i = 0; i < imageUploads.size(); i++) {
            if (i == imageUploads.size() - 1) {
                allPhoto += imageUploads.get(i).getImageId();
            } else {
                allPhoto += imageUploads.get(i).getImageId() + "~";
            }
        }

        if (imageUploads.size() != 0 && deletedImageUploads.size() != 0) {
            allPhoto += "~";
        }

        for (int i = 0; i < deletedImageUploads.size(); i++) {
            if (i == imageUploads.size() - 1) {
                allPhoto += deletedImageUploads.get(i).getImageId();
            } else {
                allPhoto += deletedImageUploads.get(i).getImageId() + "~";
            }
        }
        return allPhoto;
    }

    public Map<String, String> getSubmitParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_POST_KEY, getPostKey());
        params.put(PARAM_FILE_UPLOADED, getFileUploaded());
        return params;
    }

    private String getFileUploaded() {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : imageUploads) {
                reviewPhotos.put(image.getImageId(), image.getPicObj());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    public Map<String, String> getEditParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ACCURACY_RATE, getAccuracyRate());
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_QUALITY_RATE, getQualityRate());
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_REVIEW_MESSAGE, getReviewMessage());
        params.put(PARAM_SHOP_ID, getShopId());
        params.put(PARAM_HAS_PRODUCT_REVIEW_PHOTO, "0");
        return params;
    }

    public Map<String, String> getEditParamWithImages() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ACCURACY_RATE, getAccuracyRate());
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_QUALITY_RATE, getQualityRate());
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_REVIEW_MESSAGE, getReviewMessage());
        params.put(PARAM_SHOP_ID, getShopId());
        params.put(PARAM_HAS_PRODUCT_REVIEW_PHOTO, checkHasPhoto());
        params.put(PARAM_REVIEW_PHOTO_ALL, getReviewPhotos());
        params.put(PARAM_REVIEW_PHOTO_OBJ, getReviewPhotosObj());
        return params;
    }

    private String checkHasPhoto() {
        return imageUploads.size() > 0 ? "1" : "0";
    }

    public Map<String, String> getReportParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_SHOP_ID, getShopId());
        params.put(PARAM_REPORT_MESSAGE, getReportMessage());
        return params;
    }

    public Map<String, String> getDeleteImageParam(int position) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_SHOP_ID, getShopId());
        params.put(PARAM_ATTACHMENT_ID, getImageUploads().get(position).getImageId());
        return params;
    }

    public Map<String, String> getLikeDislikeParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_LIKE_STATUS, getLikeStatus());
        params.put(PARAM_PRODUCT_ID, getProductId());
        params.put(PARAM_REVIEW_ID, getReviewId());
        params.put(PARAM_SHOP_ID, getShopId());
        return params;
    }
    public RequestParams getLikeDislikeRequestParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_LIKE_STATUS, getLikeStatus());
        requestParams.putString(PARAM_PRODUCT_ID, getProductId());
        requestParams.putString(PARAM_REVIEW_ID, getReviewId());
        requestParams.putString(PARAM_SHOP_ID, getShopId());
        return requestParams;
    }

    public RequestParams getDeleteCommentParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_REPUTATION_ID, getReputationId());
        requestParams.putString(PARAM_REVIEW_ID, getReviewId());
        requestParams.putString(PARAM_SHOP_ID, getShopId());
        return requestParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reputationId);
        dest.writeString(reviewId);
        dest.writeString(shopId);
        dest.writeString(productId);
        dest.writeString(reviewMessage);
        dest.writeString(qualityRate);
        dest.writeString(accuracyRate);
        dest.writeString(responseMessage);
        dest.writeString(reputationScore);
        dest.writeString(role);
        dest.writeTypedList(imageUploads);
        dest.writeParcelable(generatedHost, flags);
        dest.writeString(postKey);
        dest.writeString(reportMessage);
        dest.writeString(token);
        dest.writeTypedList(deletedImageUploads);
    }
}
