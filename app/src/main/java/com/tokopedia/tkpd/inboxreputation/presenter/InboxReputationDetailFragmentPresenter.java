package com.tokopedia.tkpd.inboxreputation.presenter;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.tkpd.inboxreputation.model.ImageUpload;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Nisie on 1/26/16.
 */
public interface InboxReputationDetailFragmentPresenter {

    void initData();

    void refreshList(boolean isUsingSlave);

    void getSingleInboxReputation(String invoiceId, boolean isUsingSlave);

    void getProductList(boolean isUsingSlave);

    Map<String,String> getProductListParam(boolean isUsingSlave);

    int getSmiley(String status);

    void onUsernameClicked(InboxReputationItem inboxReputation);

    String getMessageForSmileyFromOpponent(InboxReputationItem inboxReputation);

    void afterPostForm(Bundle bundle);

    void skipReview(ActReviewPass pass , int position);

    void onEditReview(InboxReputationItem inboxReputation, InboxReputationDetail inboxReputationDetail, int position);

    void onGiveReview(InboxReputationItem inboxReputation, InboxReputationDetail inboxReputationDetail, int position);

    void deleteResponse(ActReviewPass paramDelete, int position);

    void postReputation(ActReviewPass pass);

    String getMessageForSmileyForOpponent(String smiley);

    void onGiveReply(InboxReputationItem inboxReputation, InboxReputationDetail list, int position);

    String getSmileyString(String string);

    void updateCacheSkippedReview(String reputationId, int smiley);

    void updateCacheDeletedResponse(String reputationId, int productPosition);

    void postReport(ActReviewPass pass);

    void onPreviewImageClicked(int position, ArrayList<ImageUpload> list);

    void onDestroyView();

    ActReviewPass getSmileyParam(InboxReputationItem inboxReputation, String status);

    ActReviewPass getSkipParam(InboxReputationItem inboxReputation, InboxReputationDetailItem inboxReputationDetail);

    ActReviewPass getReportParam(InboxReputationDetailItem inboxReputationDetail, String message);
}
