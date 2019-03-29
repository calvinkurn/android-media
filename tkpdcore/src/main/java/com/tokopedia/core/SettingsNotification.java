package com.tokopedia.core;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.fragment.SettingsFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.tokopedia.core2.R;

public class SettingsNotification extends TActivity {

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_CONFIG_P_NOTIF;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_settings_notification);
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
