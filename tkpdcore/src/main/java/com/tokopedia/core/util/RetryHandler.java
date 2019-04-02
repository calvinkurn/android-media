package com.tokopedia.core.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;

public class RetryHandler {

	private static final int RECYCLER_VIEW_RETRY_LAYOUT = 5;
	private BaseRecyclerViewAdapter rvAdapter;
	private ListView mainLV[];
	private View retryView;
	private View loadingView;
	private Context context;
	private OnRetryListener listener;
	private OnConnectionTimeout TimeoutListener;
	private View mainView;
	private int State;
	public boolean ShowRetry = false;

	private static int ACTION_RETRY = 2;
	private static int FULL_SCREEN_RETRY = 1;
	public static int LIST_VIEW_RETRY_FOOTER = 3;
	public static int LIST_VIEW_RETRY_HEADER = 4;

	private int loadingViewId;

	public interface OnRetryListener {
		public void onRetryCliked();
	}

	public interface OnConnectionTimeout{
		void onConnectionTimeOut();
		void onRetry();
	}

	public RetryHandler(Context context) {
		this.context = context;
		State = ACTION_RETRY;
	}


	public RetryHandler(Context context, View view) {
		this.context = context;
		this.mainView = view;
		State = FULL_SCREEN_RETRY;
	}

	public RetryHandler(Context context, BaseRecyclerViewAdapter rvAdapter) {
		this.context = context;
		this.rvAdapter = rvAdapter;
		State = RECYCLER_VIEW_RETRY_LAYOUT;
	}

	public int getState(){
		return State;
	}

	public void setOnTimeoutListener(OnConnectionTimeout TimeoutListener){
		this.TimeoutListener = TimeoutListener;
	}

	public RetryHandler(Context context, View footerLV, ListView... lv) {
		this.context = context;
		this.mainLV = lv;
		State = LIST_VIEW_RETRY_FOOTER;
	}

	public void setLoadingViewId(int loadingViewId) {
		this.loadingViewId = loadingViewId;
	}

	public void setRetryView() {
		if (State == LIST_VIEW_RETRY_FOOTER || State == LIST_VIEW_RETRY_HEADER) {
			retryView = View.inflate(context, R.layout.design_retry_footer, null);
			for (int i = 0; i < mainLV.length; i++) {
				mainLV[i].addFooterView(retryView);
			}
			retryView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRetryCliked();
					if(TimeoutListener != null)
						TimeoutListener.onRetry();
				}
			});
			retryView.findViewById(R.id.main_retry).setVisibility(View.GONE);
		} else if (State == FULL_SCREEN_RETRY) {
			LayoutInflater inflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View retryLoad = inflater.inflate(R.layout.design_retry, (ViewGroup) mainView);
			loadingView = mainView.findViewById(R.id.include_loading);
			retryView = retryLoad.findViewById(R.id.main_retry);
			retryView.setVisibility(View.GONE);
			retryView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRetryCliked();
					if(TimeoutListener != null)
						TimeoutListener.onRetry();
				}
			});

		}
	}
	/**
	 *
	 * @param state Ambil dari retry handler
	 */
	public void setState(int state){
		State = state;
	}

	public void setOnRetryListener(OnRetryListener listener) {
		this.listener = listener;
	}

	public Boolean enableRetryView() {
		ShowRetry = true;
		if (State == ACTION_RETRY) {
			showDialogConnection();
		} else if (State == FULL_SCREEN_RETRY) {
			loadingView.setVisibility(View.GONE);
			retryView.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
		} else if (getState() == RECYCLER_VIEW_RETRY_LAYOUT){
			CommonUtils.dumper("NISIETAGCONNECTION : RECYCLER VIEW TIMEOUT");
//			rvAdapter.setIsLoading(false);
//			rvAdapter.setIsRetry(true);
//			rvAdapter.notifyDataSetChanged();
//			CommonUtils.UniversalToast(context, context.getString(R.string.msg_connection_timeout_toast));
			if (TimeoutListener != null)
				TimeoutListener.onConnectionTimeOut();
		}
		else {
			for (int i = 0; i < mainLV.length; i++) {
				mainLV[i].removeFooterView(retryView);
			}

			for (int i = 0; i < mainLV.length; i++) {
				mainLV[i].addFooterView(retryView);
			}

			for (int i = 0; i < mainLV.length; i++){
				if(State == LIST_VIEW_RETRY_HEADER){
					mainLV[i].removeFooterView(retryView);
				} else {
					retryView.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
				}
			}

			if (State == LIST_VIEW_RETRY_HEADER) {
				Toast.makeText(context,
						MethodChecker.fromHtml(context.getString(R.string.msg_connection_timeout_toast)),
						Toast.LENGTH_LONG).show();
			}

			if(TimeoutListener != null) {
				TimeoutListener.onConnectionTimeOut();
			}
		}

		return true;
	}

	public Boolean disableRetryView() {
		ShowRetry = false;
		if (State == FULL_SCREEN_RETRY) {
			loadingView.setVisibility(View.VISIBLE);
			retryView.findViewById(R.id.main_retry).setVisibility(View.GONE);
		} else if (State == LIST_VIEW_RETRY_FOOTER || State == LIST_VIEW_RETRY_HEADER) {
			for (int i = 0; i < mainLV.length; i++) {
				mainLV[i].removeFooterView(retryView);
			}

			for (int i = 0; i < mainLV.length; i++) {
				mainLV[i].removeFooterView(retryView);
			}

			retryView.findViewById(R.id.main_retry).setVisibility(View.GONE);
		}else if (getState() == RECYCLER_VIEW_RETRY_LAYOUT){
			rvAdapter.setIsRetry(false);
			rvAdapter.notifyDataSetChanged();
		}

		return true;
	}

	private void showDialogConnection() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.error_network_dialog, null);
		TextView msg = (TextView) promptsView.findViewById(R.id.msg);
		msg.setText(R.string.msg_connection_timeout);
		myAlertDialog.setView(promptsView);
		myAlertDialog.setPositiveButton(context.getString(R.string.title_try_again), new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				listener.onRetryCliked();

			}
		});
		myAlertDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		myAlertDialog.show();
	}

}