package com.tokopedia.tkpd;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.tkpd.network.SnackbarRetry;
import com.tokopedia.tkpd.prototype.ShopSettingCache;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageNotification extends TActivity {

	private CheckBox CBNewsletter;
	private CheckBox CBReviews;
	private CheckBox CBTalk;
	private CheckBox CBPrivate;
	private CheckBox CBAdmin;
	private TextView SaveBut;
	private String SettingId;
	private View MainView;
	private TkpdProgressDialog mProgressDialog;
	private TextView SetRing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_manage_notification);
		SetRing = (TextView) findViewById(R.id.set_ring);
		SetRing.setVisibility(View.GONE);
		CBNewsletter = (CheckBox) findViewById(R.id.newsletter);
		MainView = findViewById(R.id.main_view);
		MainView.setVisibility(View.GONE);
		CBReviews = (CheckBox) findViewById(R.id.reviews);
		CBTalk = (CheckBox) findViewById(R.id.talkabout);
		CBPrivate = (CheckBox) findViewById(R.id.message);
		CBAdmin = (CheckBox) findViewById(R.id.admin_message);
		SaveBut = (TextView) findViewById(R.id.save_but);
		mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
		mProgressDialog.setLoadingViewId(R.id.include_loading);
		mProgressDialog.showDialog();
		CheckCache();

		SaveBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mProgressDialog = new TkpdProgressDialog(ManageNotification.this, TkpdProgressDialog.NORMAL_PROGRESS);
				mProgressDialog.showDialog();
				SetNotif();
			}
		});

		SetRing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ManageNotification.this, SettingsNotification.class));

			}
		});
	}


	private void CheckCache(){
		if(ShopSettingCache.getSetting(ShopSettingCache.CODE_NOTIFICATION, this)!=null){
			MainView.setVisibility(View.VISIBLE);
			mProgressDialog.dismiss();
			SetToUI(ShopSettingCache.getSetting(ShopSettingCache.CODE_NOTIFICATION, this));
		}
		else
			GetNotif();
	}

	private void SetToUI(JSONObject Result){
		try {
			JSONObject Notification = new JSONObject(Result.getString("notification"));
			if(Notification.getString("review").equals("1"))
			CBReviews.setChecked(true);
			if(Notification.getString("pm").equals("1"))
				CBPrivate.setChecked(true);
			if(Notification.getString("pm_from_admin").equals("1"))
				CBAdmin.setChecked(true);
			if(Notification.getString("news_letter").equals("1"))
				CBNewsletter.setChecked(true);
			if(Notification.getString("talk_product").equals("1"))
				CBTalk.setChecked(true);
			SettingId = Notification.getString("setting_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GetNotif(){
		NetworkHandler network = new NetworkHandler(ManageNotification.this, TkpdUrl.GET_PEOPLE);
		network.AddParam("act", "get_notification");
		network.Commit(new NetworkHandlerListener() {

			@Override
			public void onSuccess(Boolean status) {
				MainView.setVisibility(View.VISIBLE);
				mProgressDialog.dismiss();
			}

			@Override
			public void getResponse(JSONObject Result) {
				SetToUI(Result);
					ShopSettingCache.SaveCache(ShopSettingCache.CODE_NOTIFICATION, Result.toString(), ManageNotification.this);
			}

			@Override
			public void getMessageError(ArrayList<String> MessageError) {
				mProgressDialog.dismiss();
				String errorMessage = "";
				for(int i=0; i<MessageError.size(); i++){
					errorMessage += MessageError.get(i);
					if((i+1)<MessageError.size())
						errorMessage += "\n";
				}
				NetworkErrorHelper.showEmptyState(
						ManageNotification.this,
						getWindow().getDecorView().getRootView(),
						errorMessage,
						new NetworkErrorHelper.RetryClickedListener() {
					@Override
					public void onRetryClicked() {
						mProgressDialog.showDialog();
						GetNotif();
					}
				});
			}
		});
	}

	private void SetNotif(){
		NetworkHandler network = new NetworkHandler(ManageNotification.this, TkpdUrl.GET_PEOPLE);
		network.AddParam("act", "edit_notification");
		if(CBAdmin.isChecked())
			network.AddParam("f_notice_pm_from_admin","on");
		if(CBNewsletter.isChecked())
			network.AddParam("f_notice_news_letter","on");
		if(CBPrivate.isChecked())
			network.AddParam("f_notice_pm","on");
		if(CBReviews.isChecked())
			network.AddParam("f_notice_review","on");
		if(CBTalk.isChecked())
			network.AddParam("f_notice_talk_product","on");
		network.AddParam("setting_id", SettingId);
		network.Commit(new NetworkHandlerListener() {

			@Override
			public void onSuccess(Boolean status) {
				// TODO Auto-generated method stub
				mProgressDialog.dismiss();
			}

			@Override
			public void getResponse(JSONObject Result) {
				// TODO Auto-generated method stub
				System.out.println("Updated notificatioin");
				try {
					ShopSettingCache.DeleteCache(ShopSettingCache.CODE_NOTIFICATION, ManageNotification.this);
					if(Result.getInt("success")!=0)
					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void getMessageError(ArrayList<String> MessageError) {
				String errorMessage = "";
				for(int i=0; i<MessageError.size(); i++){
					errorMessage += MessageError.get(i);
					if((i+1)<MessageError.size())
						errorMessage += "\n";
				}
				mProgressDialog.dismiss();
				SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(ManageNotification.this,
						errorMessage,
						new NetworkErrorHelper.RetryClickedListener() {
					@Override
					public void onRetryClicked() {
						mProgressDialog.showDialog();
						SetNotif();
					}
				});
				snackbarRetry.showRetrySnackbar();
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
