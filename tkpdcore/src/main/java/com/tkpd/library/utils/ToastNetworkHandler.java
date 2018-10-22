package com.tkpd.library.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core2.R;

public class ToastNetworkHandler {

	private static Toast toast;

	public static void showToast (Context context) {
		if (!isToastShown()) {
			toastMsg(context, null);
		}
	}

	public static void showToast (Context context, String toastMsg) {
		if (!isToastShown()) {
			toastMsg(context, toastMsg);
		}
	}

	private static void toastMsg(Context context, String toastMsg) {
		if(toastMsg == null) {
			toastMsg = context.getResources().getString(R.string.msg_server_error);
		}

		toast = Toast.makeText(context, toastMsg, Toast.LENGTH_LONG);
		toast.show();
	}

	public static Boolean isToastShown() {
		if (toast == null) {
			return false;
		} else {
			return toast.getView().getWindowVisibility() == View.VISIBLE;
		}
	}

}
