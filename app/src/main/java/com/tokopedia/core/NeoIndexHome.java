package com.tokopedia.core;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.home.ParentIndexHome;

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
