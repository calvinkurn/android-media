package com.tokopedia.tkpd;

import com.tokopedia.tkpd.app.TActivity;

import android.os.Bundle;
import android.view.Menu;

public class ProfileEditAddress extends TActivity {

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
