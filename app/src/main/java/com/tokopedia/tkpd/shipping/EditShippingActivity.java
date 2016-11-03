package com.tokopedia.tkpd.shipping;

import android.os.Bundle;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.shipping.fragment.FragmentEditShipping;

public class EditShippingActivity extends TActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_shipping_shop_editor);

		if(savedInstanceState == null){
			getFragmentManager().beginTransaction().add(R.id.main_view, FragmentEditShipping.createInstance()).commit();
		}
	}
}
