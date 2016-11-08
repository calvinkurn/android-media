package com.tokopedia.core.inboxreputation.listener;

import android.os.Bundle;

import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;

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
