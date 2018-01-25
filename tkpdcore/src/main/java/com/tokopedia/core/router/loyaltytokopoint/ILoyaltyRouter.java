package com.tokopedia.core.router.loyaltytokopoint;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;

import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;

/**
 * @author anggaprasetiyo on 08/12/17.
 */

public interface ILoyaltyRouter {
    String LOYALTY_TOKOPOINT_NOTIFICATION_DIALOG_FRAGMENT_TAG
            = "LOYALTY_TOKOPOINT_NOTIFICATION_DIALOG_FRAGMENT_TAG";
    String INTENT_ACTION_BROADCAST_RECEIVER_TOKOPOINT =
            "com.tokopedia.loyalty.broadcastreceiver.TokoPointDrawerBroadcastReceiver.ACTION_GET_DRAWER_TOKOPOINT";

    DialogFragment getLoyaltyTokoPointNotificationDialogFragment(TokoPointDrawerData.PopUpNotif popUpNotif);

    BroadcastReceiver getTokoPointBroadcastReceiver();
}
