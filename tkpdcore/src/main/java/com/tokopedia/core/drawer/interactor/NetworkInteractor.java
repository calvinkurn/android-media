package com.tokopedia.core.drawer.interactor;

import android.content.Context;

import com.tokopedia.core.drawer.model.DrawerHeader;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.drawer.var.NotificationItem;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public interface NetworkInteractor {

    void getProfileInfo(Context context, ProfileInfoListener listener);

    void getDeposit(Context context, DepositListener listener);

    void getNotification(Context context, NotificationListener listener);

    void getLoyalty(Context context, LoyaltyListener listener);

    void getTokoCash(Context context, TopCashListener listener);

    void updateTokoCash(Context context, TopCashListener listener);

    void resetNotification(Context context, ResetNotificationListener listener);

    void clearWalletCache();

    interface ProfileInfoListener {

        void onSuccess(DrawerHeader data);

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

    interface TopCashListener {
        void onSuccess(TopCashItem topCashItem);

        void onError(String message);

        void onTokenExpire();
    }

    void unsubscribe();
}
