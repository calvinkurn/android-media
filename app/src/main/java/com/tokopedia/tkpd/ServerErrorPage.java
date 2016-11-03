package com.tokopedia.tkpd;

import com.tokopedia.tkpd.app.TActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ServerErrorPage extends TActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_error_page);
		findViewById(R.id.retry_but).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	
	}


}
