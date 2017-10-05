package com.tokopedia.core.drawer.interactor;

import android.content.Context;

import com.tokopedia.core.drawer.var.NotificationItem;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public interface NetworkInteractor {

    void getNotification(Context context, NotificationListener listener);


    interface NotificationListener {

        void onSuccess(NotificationItem data);

        void onError(String message);
    }

    void unsubscribe();
}
