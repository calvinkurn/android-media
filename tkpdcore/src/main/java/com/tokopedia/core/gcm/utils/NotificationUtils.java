package com.tokopedia.core.gcm.utils;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.session.presenter.SessionView;

/**
 * Created by alvarisi on 1/12/17.
 */

public class NotificationUtils {
    public static Intent configureGeneralIntent(Intent intent){
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);
        return intent;
    }

    public static Intent configurePromoIntent(Intent intent, Bundle data){
        if (data.getInt("keylogin1", -99) != -99) {
            intent.putExtra(
                    com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY,
                    data.getInt("keylogin1")
            );
            intent.putExtra(
                    SessionView.MOVE_TO_CART_KEY,
                    data.getInt("keylogin2")
            );
        }
        return intent;
    }
}
