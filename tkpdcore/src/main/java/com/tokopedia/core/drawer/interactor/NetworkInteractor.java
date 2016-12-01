package com.tokopedia.core.drawer.interactor;

import android.content.Context;

import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.var.NotificationItem;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public interface NetworkInteractor {

    void getProfileInfo(Context context, ProfileInfoListener listener);

    void getDeposit(Context context, DepositListener listener);

    void getNotification(Context context, NotificationListener listener);

    void getLoyalty(Context context, LoyaltyListener listener);

    void resetNotification(Context context, ResetNotificationListener listener);

    interface ProfileInfoListener {

        void onSuccess(DrawerVariable.DrawerHeader data);

        void onError(String message);
    }

    interface DepositListener {

        void onSuccess(String data);

        void onError(String message);
    }

    interface NotificationListener {

        void onSuccess(NotificationItem data);

        void onError(String message);
    }

    interface LoyaltyListener {

        void onSuccess(LoyaltyItem data);

        void onError(String message);
    }

    interface ResetNotificationListener {

        void onSuccess();

        void onError(String message);
    }

    void unsubscribe();
}
