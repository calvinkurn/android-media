package com.tokopedia.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.prototype.ProfileCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManagePrivacy extends TActivity {

	private CheckBox ManageBirth;
	private CheckBox ManageEmail;
	private CheckBox ManageMessenger;
	private CheckBox ManageMobile;
	private TextView ErrorMessage;
	private TextView SaveBut;
	private View MainView;
	
	private TkpdProgressDialog mProgressDialog;

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_CONFIG_P_PRIVACY;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_manage_privacy);


		MainView = findViewById(R.id.main_view);
		MainView.setVisibility(View.GONE);
		ErrorMessage = (TextView) findViewById(R.id.msg_error);
		ManageBirth = (CheckBox) findViewById(R.id.birth_date);
		ManageEmail = (CheckBox) findViewById(R.id.email);
		ManageMessenger = (CheckBox) findViewById(R.id.messenger);
		ManageMobile = (CheckBox) findViewById(R.id.mobile);
		SaveBut = (TextView) findViewById(R.id.save_but);
		mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
		mProgressDialog.setLoadingViewId(R.id.include_loading);
		mProgressDialog.showDialog();
		CheckCache();
		
		SaveBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mProgressDialog = new TkpdProgressDialog(ManagePrivacy.this, TkpdProgressDialog.NORMAL_PROGRESS);
				mProgressDialog.showDialog();
				ErrorMessage.setVisibility(View.GONE);
				EditPrivacy();
			}
		});
	}
	
	private void CheckCache(){
		if(ShopSettingCache.getSetting(ShopSettingCache.CODE_PRIVACY, ManagePrivacy.this)!=null){
			mProgressDialog.dismiss();
			MainView.setVisibility(View.VISIBLE);
			SetToUI(ShopSettingCache.getSetting(ShopSettingCache.CODE_PRIVACY, ManagePrivacy.this));
		}
		else
			GetPrivacy();
	}
	
	private void SetToUI(JSONObject Result){
		try {
			JSONObject Privacy = new JSONObject(Result.getString("privacy"));
			if(Privacy.getString("flag_messenger").equals("2"))
				ManageMessenger.setChecked(true);
			if(Privacy.getString("flag_hp").equals("2"))
				ManageMobile.setChecked(true);
			if(Privacy.getString("flag_email").equals("2"))
				ManageEmail.setChecked(true);
			if(Privacy.getString("flag_birthdate").equals("2"))
				ManageBirth.setChecked(true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void GetPrivacy(){
		NetworkHandler network = new NetworkHandler(ManagePrivacy.this, TkpdUrl.GET_PEOPLE);
		network.AddParam("act", "get_privacy");

		network.Commit(new NetworkHandlerListener() {
			
			@Override
			public void onSuccess(Boolean status) {
				mProgressDialog.dismiss();
				MainView.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void getResponse(JSONObject Result) {
				SetToUI(Result);
				ShopSettingCache.SaveCache(ShopSettingCache.CODE_PRIVACY, Result.toString(), ManagePrivacy.this);
			}
			
			@Override
			public void getMessageError(ArrayList<String> MessageError) {
				
			}
		});
	}
	
	private void EditPrivacy(){
		NetworkHandler network = new NetworkHandler(ManagePrivacy.this, TkpdUrl.GET_PEOPLE);
		network.AddParam("act", "edit_privacy");
		if(ManageBirth.isChecked())
			network.AddParam("flag_birthdate", "2");
		else
			network.AddParam("flag_birthdate", "0");
		if(ManageEmail.isChecked())
			network.AddParam("flag_email", "2");
		else
			network.AddParam("flag_email", "0");
		if(ManageMessenger.isChecked())
			network.AddParam("flag_messenger", "2");
		else
			network.AddParam("flag_messenger", "0");
		if(ManageMobile.isChecked())
			network.AddParam("flag_hp", "2");
		else
			network.AddParam("flag_hp", "0");
		network.Commit(new NetworkHandlerListener() {
			
			@Override
			public void onSuccess(Boolean status) {
				mProgressDialog.dismiss();
			}
			
			@Override
			public void getResponse(JSONObject Result) {
				System.out.println("Updated privacy settings");
				
				try {
					if(Result.getInt("success")!=0){
					ShopSettingCache.DeleteCache(ShopSettingCache.CODE_PRIVACY, ManagePrivacy.this);
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					if(ManageBirth.isChecked())
						bundle.putInt("birth", View.VISIBLE);
					else
						bundle.putInt("birth", View.GONE);
					if(ManageEmail.isChecked())
						bundle.putInt("email", View.VISIBLE);
					else
						bundle.putInt("email", View.GONE);
					if(ManageMessenger.isChecked())
						bundle.putInt("messenger", View.VISIBLE);
					else
						bundle.putInt("messenger", View.GONE);
					if(ManageMobile.isChecked())
						bundle.putInt("phone", View.VISIBLE);
					else
						bundle.putInt("phone", View.GONE);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					ProfileCache.DeleteCache(SessionHandler.getLoginID(ManagePrivacy.this));
					finish();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void getMessageError(ArrayList<String> MessageError) {
				// TODO Auto-generated method stub
				ErrorMessage.setText("");
				ErrorMessage.setVisibility(View.VISIBLE);
				for(int i=0; i<MessageError.size(); i++){
					ErrorMessage.setText(ErrorMessage.getText() + MessageError.get(i));
					if((i+1)<MessageError.size())
						ErrorMessage.setText(ErrorMessage.getText() + "\n");
				}
			}
		});
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
