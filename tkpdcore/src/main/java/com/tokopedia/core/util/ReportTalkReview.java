package com.tokopedia.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONObject;

import java.util.ArrayList;

public class ReportTalkReview {
	private JSONHandler JsonSender;
	private TokenHandler Token = new TokenHandler();
	private Activity context;
	private Integer state;
	private String ID;
	private String Message;
	private String URLid;
	private String Action;
	private String ShopId;
	private TkpdProgressDialog progressDialog;
	
	public ReportTalkReview(Activity context, int state, String ID, String Action, String ShopId) {
		this.context = context;
		this.state = state;
		this.ShopId = ShopId;
		this.ID = ID;
		this.Action = Action;
		if (state == 1) {
			URLid = TkpdUrl.GET_REVIEW;
		} else {
			URLid = TkpdUrl.GET_TALK;
		}
	}
	
	public void ShowDialogReport() {
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.prompt_dialog_report, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.reason);

		alertDialogBuilder
			.setCancelable(true)
			.setPositiveButton(context.getString(R.string.action_report),
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    }
			  })
			.setNegativeButton(context.getString(R.string.title_cancel),
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	if(userInput.length()>0){
					    	Message = userInput.getText().toString();
					    	PostReport(Message);
					    	alertDialog.dismiss();
					    	}
					    	else
					    		userInput.setError(context.getString(R.string.error_field_required));
		            }
		        });
			}
		});
		// show it
		alertDialog.show();
	}
	
	private void PostReport(String... params)
	{
		progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
		progressDialog.showDialog();
		NetworkHandler network = new NetworkHandler(context, URLid);
		network.AddParam("id", ID);
		network.AddParam("act", Action);
		network.AddParam("r_message", params[0]);
		network.AddParam("shop_id", ShopId);
		network.Commit(new NetworkHandlerListener() {

			@Override
			public void onSuccess(Boolean status) {
				progressDialog.dismiss();
			}

			@Override
			public void getResponse(JSONObject Result) {
				SnackbarManager.make(context,context.getString(R.string.toast_success_report), Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void getMessageError(ArrayList<String> MessageError) {
				progressDialog.dismiss();
				if(MessageError.get(0).contains("java.net.UnknownHostException")){
					SnackbarManager.make(context, context.getResources().getString(R.string.msg_no_connection), Snackbar.LENGTH_LONG).show();
				}else {
					SnackbarManager.make(context, MessageError.toString(), Snackbar.LENGTH_LONG).show();
				}
			}
		});
	}
	
//	public class PostReport extends AsyncTask<String, Void, String> {
//		@Override
//		protected String doInBackground(String... params) {
//			try {
//				progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
//				progressDialog.showDialog();
//	            int TokenStatus = Token.checkToken(context);     
//	            if (TokenStatus==1 || TokenStatus==2) {
//	            	JsonSender = new JSONHandler(context.getString(URLid));
//					JsonSender.AddJSON("app_id", Token.getAppId(context));
//	 				JsonSender.AddJSON("token", Token.getToken(context));
//	 				JsonSender.AddJSON("user_id", Token.getLoginID(context));
//	 				JsonSender.AddJSON("shopId", ID);
//	 				JsonSender.AddJSON("act", Action);
//	 				JsonSender.AddJSON("r_message", params[0]);
//	 				JsonSender.CompileJSON();
//	            }
//			} catch (JSONException e) {
//						// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String response = null;
//			for (int i=0;i<5 && response==null;i++) {
//				response = JsonSender.getResponse();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			return response;
//			
//		}
//
//		@Override
//		protected void onPostExecute(String response) {
//			System.out.println(response);
//			progressDialog.dismiss();
//		}
//		
//	}

}
