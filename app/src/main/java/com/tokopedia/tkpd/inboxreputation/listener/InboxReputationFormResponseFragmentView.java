package com.tokopedia.tkpd.inboxreputation.listener;

import android.os.Bundle;

import com.tokopedia.tkpd.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;

/**
 * Created by Nisie on 6/7/16.
 */
public interface InboxReputationFormResponseFragmentView {
    void showLoading();

    InboxReputationItem getInboxReputation();

    InboxReputationDetailItem getInboxReputationDetail();

    void onSuccessPostResponse(Bundle resultData);

    void onFailedPostResponse(Bundle resultData);

    void setActionsEnabled(boolean isEnabled);
}
