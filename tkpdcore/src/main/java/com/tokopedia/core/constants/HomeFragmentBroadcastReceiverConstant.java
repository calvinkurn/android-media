package com.tokopedia.core.constants;

/**
 * @author anggaprasetiyo on 12/12/17.
 */

public interface HomeFragmentBroadcastReceiverConstant {
    String INTENT_ACTION =
            "com.tokopedia.tkpd.beranda.presentation.view.fragment.HomeFragment.HomeFragmentBroadcastReceiver.ACTION";
    String EXTRA_TOKOPOINT_DRAWER_DATA = "EXTRA_TOKOPOINT_DRAWER_DATA";
    String EXTRA_TOKOCASH_DRAWER_DATA = "EXTRA_TOKOCASH_DRAWER_DATA";
    String EXTRA_TOKOCASH_PENDING_DATA = "EXTRA_TOKOCASH_PENDING_DATA";
    String EXTRA_ACTION_RECEIVER = "EXTRA_ACTION_RECEIVER";

    int ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA = 1;
    int ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA = 2;
    int ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA_ERROR = 4;
    int ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA_ERROR = 5;
    int ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA = 3;
}
