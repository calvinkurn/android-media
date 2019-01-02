package com.tokopedia.core;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.SimpleListViewAdapter;
import com.tokopedia.core.util.JSONHandler;
import com.tokopedia.core.util.TokenHandler;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import com.tokopedia.core2.R;

public class UserAddress extends TActivity {
	private JSONHandler JsonSender;
	private TokenHandler Token = new TokenHandler();
	private ListView AddressList;
	private SimpleListViewAdapter AddressAdapter;
	private ArrayList<String> AddressName = new ArrayList<String>();
	private View footerLV;
	private TextView AddNewAddressBtn;

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_PEOPLE_ADDRESS;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_user_address);

		AddNewAddressBtn = (TextView) findViewById(R.id.add_new_address);
		AddressList = (ListView) findViewById(R.id.address_name);
		footerLV = View.inflate(this, R.layout.footer_list_view, null);
		AddressAdapter = new SimpleListViewAdapter(UserAddress.this, AddressName, null);
		AddressList.setAdapter(AddressAdapter);
		AddressList.addFooterView(footerLV);
		new getUserAddress().execute();
		
		
		//----------------------------------------------------------------------------------
		//Untuk edit setelah di hold cukup lama
		//----------------------------------------------------------------------------------
		
		AddressList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//Intent intent = new Intent(UserAddress.this, ProfileEditAddress.class);
				Bundle bundle = new Bundle();
				bundle.putInt("page", 1);
				//	startActivityForResult(intent, 2);
				return true;
			}
		});
		
		//----------------------------------------------------------------------------------
		//Untuk delete ketika di klik
		//----------------------------------------------------------------------------------
		
		AddressList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public class getUserAddress extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... Void) {
			try {
				int TokenStatus = Token.checkToken(getBaseContext());     
	            if (TokenStatus==1 || TokenStatus==2) {
	            	JsonSender = new JSONHandler(TkpdUrl.GET_PEOPLE);
					JsonSender.AddJSON("app_id", Token.getAppId(getBaseContext()));
	 				JsonSender.AddJSON("token", Token.getToken(getBaseContext()));
	 				JsonSender.AddJSON("user_id", Token.getLoginID(getBaseContext()));
	 				JsonSender.AddJSON("act", "get_address");
	 				
	 				JsonSender.AddJSON("page", "1");
	 				JsonSender.CompileJSON();
	            }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String response = null;
			for (int i=0;i<5 && response==null;i++) {
				response = JsonSender.getResponse();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return response;
		} 
		
		@Override
		protected void onPostExecute(String response) {
			try {
				JSONObject json = new JSONObject(response);
				if (!json.isNull("result")) {
					JSONObject Result = new JSONObject(json.getString("result"));
					
					JSONArray DataArray = new JSONArray(Result.getString("data"));
					
					for (int i = 0; i< DataArray.length(); i++) {
						JSONObject DataDetail = new JSONObject(DataArray.getString(i));
						AddressName.add(DataDetail.getString("address_name"));
					
					}
					AddressAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
	}
}
