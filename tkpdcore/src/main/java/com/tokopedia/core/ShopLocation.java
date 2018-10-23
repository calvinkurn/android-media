package com.tokopedia.core;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.ListViewShopLocation;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core2.R;

import org.json.JSONArray;

public class ShopLocation extends TActivity {

	private boolean IsOwner;
	private JSONArray ShopLocationList;
	private ListViewShopLocation ShopLocationAdapter;
	private ListView ShopLocationListView;
	private ShopModel model;

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_SHOP_LOCATION;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_shop_location);
		ShopLocationListView = (ListView) findViewById(R.id.location_list);

		IsOwner = getIntent().getExtras().getBoolean("is_owner");
		String data = getIntent().getStringExtra("shop_info");
		model = new Gson().fromJson(data, ShopModel.class);
//		try {
//			ShopLocationList = new JSONArray(getIntent().getExtras().getString("address_list"));
//			JSONObject ShopData = new JSONObject();
//			for(int i=0;i<ShopLocationList.length();i++){
//				//JSONObject AddrData = new JSONObject(AddrList.getString(AddressID.get(i)));
//				ShopData = new JSONObject(ShopLocationList.getString(i));
//				shop_name.add(ShopData.getString("addr_name"));
//				shop_detail.add(ShopData.getString("address") + "\n" + ShopData.getString("area"));
//				shop_phone.add(ShopData.getString("phone"));
//				shop_fax.add(ShopData.getString("fax"));
//				shop_mail.add(ShopData.getString("email"));
//			}
//
			ShopLocationAdapter = new ListViewShopLocation(ShopLocation.this, model);
			ShopLocationListView.setAdapter(ShopLocationAdapter);
			ShopLocationAdapter.notifyDataSetChanged();
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
