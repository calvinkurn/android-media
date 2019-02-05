package com.tokopedia.core;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core2.R;

import android.os.Bundle;
import android.view.Menu;

public class ProfileEditAddress extends TActivity {

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_CONFIG_P_ADDRESS;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_profile_edit_address);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_edit_address, menu);
		return true;
	}

}
