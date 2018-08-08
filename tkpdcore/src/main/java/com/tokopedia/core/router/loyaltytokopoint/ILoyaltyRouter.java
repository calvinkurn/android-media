package com.tokopedia.core.router.loyaltytokopoint;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.tokopedia.core.drawer2.data.viewmodel.PopUpNotif;

/**
 * @author anggaprasetiyo on 08/12/17.
 */

public interface ILoyaltyRouter {
    String LOYALTY_TOKOPOINT_NOTIFICATION_DIALOG_FRAGMENT_TAG
            = "LOYALTY_TOKOPOINT_NOTIFICATION_DIALOG_FRAGMENT_TAG";

    DialogFragment getLoyaltyTokoPointNotificationDialogFragment(PopUpNotif popUpNotif);

    BroadcastReceiver getTokoPointBroadcastReceiver();

    void openTokoPoint(Context context, String url);
}
