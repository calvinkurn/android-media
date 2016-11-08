package com.tokopedia.core.rescenter.inbox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tokopedia.core.rescenter.inbox.activity.InboxResCenterActivity;

import java.util.List;

/**
 * Created on 3/30/16.
 */
public interface ResCenterPresenter {

    void setLocalyticFlow(@NonNull Context context);

    void initAnalytics(@NonNull Context context);

    void clearNotif(@NonNull Context context,
                    @NonNull Intent intent);

    void initFragmentList(@NonNull Context context,
                          @NonNull List<InboxResCenterActivity.Model> list);

    void onActionVar(Context context);
}
