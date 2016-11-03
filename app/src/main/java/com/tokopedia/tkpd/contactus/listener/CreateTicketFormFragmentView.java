package com.tokopedia.tkpd.contactus.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.contactus.model.contactusform.TicketFormData;
import com.tokopedia.tkpd.inboxreputation.model.ImageUpload;
import com.tokopedia.tkpd.network.NetworkErrorHelper;

import java.util.ArrayList;

/**
 * Created by nisie on 8/15/16.
 */
public interface CreateTicketFormFragmentView {
    String getTicketCategoryId();

    Activity getActivity();

    void showLoading();

    void finishLoading();

    void setResult(TicketFormData ticketFormData);

    String getMessage();

    String getString(int resId);

    void showError(String error);

    Bundle getArguments();

    ArrayList<ImageUpload> getAttachment();

    void removeErrorEmptyState();

    void showErrorEmptyState(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener);

}
