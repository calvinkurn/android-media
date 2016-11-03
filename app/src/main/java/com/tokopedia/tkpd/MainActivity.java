package com.tokopedia.tkpd;


import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

import com.tokopedia.tkpd.myproduct.ProductActivity;
import com.tokopedia.tkpd.myproduct.fragment.AddProductFragment;
import com.tokopedia.tkpd.util.TokenHandler;




public class MainActivity extends Activity {
	TokenHandler token = new TokenHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		token.ATGenerator(this);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.add_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				});
	}

}
