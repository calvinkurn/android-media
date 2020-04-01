package com.tokopedia.core.util;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

public class ClipboardHandler {
	
	public ClipboardHandler(Activity context){

	}
	
	public static void CopyToClipboard(Activity context, String Text){
		ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE); 
    	ClipData clip = ClipData.newPlainText("Tokopedia", Text);
    	clipboard.setPrimaryClip(clip);
	}
}