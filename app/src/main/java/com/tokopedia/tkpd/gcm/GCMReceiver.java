package com.tokopedia.tkpd.gcm;



import android.content.Context;

import com.tokopedia.tkpd.R;
import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMReceiver extends GCMBroadcastReceiver {

@Override
protected String getGCMIntentServiceClassName(Context context) {
	return "com.tokopedia.tkpd.gcm.GCMIntentService";
	}
}