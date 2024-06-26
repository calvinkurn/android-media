package com.tokopedia.loyalty.common;

/**
 * @author anggaprasetiyo on 12/12/17.
 */

public interface DrawerActivityBroadcastReceiverConstant {

    String INTENT_ACTION_MAIN_APP =
            "com.tokopedia.core.app.DrawerPresenterActivity.DrawerActivityBroadcastReceiver.ACTION_MAIN_APP";
    String EXTRA_TOKOPOINT_DRAWER_DATA = "EXTRA_TOKOPOINT_DRAWER_DATA";
    String EXTRA_TOKOCASH_DRAWER_DATA = "EXTRA_TOKOCASH_DRAWER_DATA";
    String EXTRA_ACTION_RECEIVER = "EXTRA_ACTION_RECEIVER";

    int ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA = 1;
    int ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA = 5;
    int ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA = 6;
    int ACTION_RECEIVER_GET_TOKOCASH_DATA = 2;
    int ACTION_RECEIVER_GET_TOKOPOINT_DATA = 3;
    int ACTION_RECEIVER_GET_TOKOCASH_PENDING_DATA = 4;

}
