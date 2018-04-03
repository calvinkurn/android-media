package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

import java.util.ArrayList;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewRequestModel {
    private String postKey;
    private int reviewId;
    private boolean isValidateSuccess;

    String uploadHost;
    String serverId;

    private String attachmentString;
    private ArrayList<ImageUpload> listUpload;
    private int isSubmitSuccess;

    public SendReviewRequestModel() {
        this.postKey = "";
        this.reviewId = 0;
        this.isValidateSuccess = false;
        this.uploadHost = "";
        this.serverId = "";
        this.isSubmitSuccess= 0;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setValidateSuccess(boolean validateSuccess) {
        isValidateSuccess = validateSuccess;
    }

    public String getPostKey() {
        return postKey;
    }

    public int getReviewId() {
        return reviewId;
    }

    public boolean isValidateSuccess() {
        return isValidateSuccess;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getAttachmentString() {
        return attachmentString;
    }

    public void setAttachmentString(String attachmentString) {
        this.attachmentString = attachmentString;
    }

    public void setListUpload(ArrayList<ImageUpload> listUpload) {
        this.listUpload = listUpload;
    }

    public ArrayList<ImageUpload> getListUpload() {
        return listUpload;
    }

    public void setIsSubmitSuccess(int isSubmitSuccess) {
        this.isSubmitSuccess = isSubmitSuccess;
    }

    public int getIsSubmitSuccess() {
        return isSubmitSuccess;
    }


}
