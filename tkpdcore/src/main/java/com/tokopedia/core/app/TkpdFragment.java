package com.tokopedia.core.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;

/**
 * Extends TkpdBaseV4Fragment from tkpd abstraction
 */
@Deprecated
public abstract class TkpdFragment extends Fragment {
	
	public Context context;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
        CommonUtils.dumper(getClass().toString());
	}

	protected abstract String getScreenName();

	public static boolean isNetworkStatusAvailable (Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null)
		{
			NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
			if(netInfos != null)
				if(netInfos.isConnected())
					return true;
		}
		return false;
	}

	protected void showNoConnectionDialog(Context context){
		final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.error_network_dialog, null);
		TextView msg = (TextView) promptsView.findViewById(R.id.msg);
		msg.setText(R.string.msg_no_connection);
		myAlertDialog.setView(promptsView);
		myAlertDialog.setPositiveButton(context.getString(R.string.title_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		myAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		myAlertDialog.show();
	}
}
