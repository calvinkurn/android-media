package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 18/07/17.
 */

public class LikeDislikeDomain {
    private boolean success;
    private String errMessage;
    private int errCode;

    private List<LikeDislikeReviewDomain> likeDislikeReviewDomain = new ArrayList<LikeDislikeReviewDomain>();

    public LikeDislikeDomain(List<LikeDislikeReviewDomain> likeDislikeReviewDomain) {
        this.likeDislikeReviewDomain = likeDislikeReviewDomain;
    }

    public LikeDislikeDomain() {

    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public List<LikeDislikeReviewDomain> getLikeDislikeReviewDomain() {
        return likeDislikeReviewDomain;
    }

    public void setLikeDislikeReviewDomain(List<LikeDislikeReviewDomain> likeDislikeReviewDomain) {
        this.likeDislikeReviewDomain = likeDislikeReviewDomain;
    }
}
