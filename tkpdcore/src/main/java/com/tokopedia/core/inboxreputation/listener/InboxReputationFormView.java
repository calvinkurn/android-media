package com.tokopedia.core.inboxreputation.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.inboxreputation.adapter.ImageUploadAdapter;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;

/**
 * Created by Nisie on 1/28/16.
 */
public interface InboxReputationFormView {

    View.OnClickListener onSubmitClickListener();

    String getMessageReview();

    void showMessageReviewError(int resId);

    String getAccuracyRating();

    String getQualityRating();

    void showRatingAccuracyError();

    void showRatingQualityError();

    void dismissProgressDialog();

    void showLoading();

    void setFormFromCache(ActReviewPass review);

    void onSuccessPostReview(Bundle resultData);

    void onSuccessEditReview(Bundle resultData);

    String getToken();

    ImageUploadAdapter getAdapter();

    void setActionsEnabled(boolean b);

    void removeError();

    InboxReputationDetailItem getInboxReputationDetail();

    Activity getActivity();

    InboxReputationItem getInboxReputation();

    void onFailedPostReview(Bundle resultData);

    void onFailedEditReview(Bundle resultData);
}
