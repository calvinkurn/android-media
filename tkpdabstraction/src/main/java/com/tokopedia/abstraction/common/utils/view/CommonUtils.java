package com.tokopedia.abstraction.common.utils.view;

import android.util.Log;

import com.tokopedia.abstraction.common.utils.GlobalConfig;

public class CommonUtils {

	public static void dumper (Object o) {
		if(GlobalConfig.isAllowDebuggingTools()) {
			Log.i("Dumper", o.toString());
		}
	}

	public static void dumper (String str) {
		if(GlobalConfig.isAllowDebuggingTools()) {
			Log.i("Dumper", str);
		}
	}

}
