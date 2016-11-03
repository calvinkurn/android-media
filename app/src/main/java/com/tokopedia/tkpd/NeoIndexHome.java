package com.tokopedia.tkpd;

import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.home.ParentIndexHome;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class NeoIndexHome extends ParentIndexHome {
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (!MainApplication.isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

}
