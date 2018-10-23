package com.tokopedia.core.util;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;

public class ClipboardHandler {
	private Activity context;
	
	public ClipboardHandler(Activity context){
		this.context = context;
	}
	
	public static void CreateMenuPopUp(final Activity context,final View v){
		final PopupMenu popup = new PopupMenu(context, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(com.tokopedia.core2.R.menu.clipboard_copy, popup.getMenu());
	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				if (item.getItemId() == R.id.action_copy) {
					CopyToClipboard(context, ((TextView) v).getText().toString());
					return false;
				}
				return false;
			}
		});
		popup.show();
	}
	
	public static void CopyToClipboard(Activity context, String Text){
		ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE); 
    	ClipData clip = ClipData.newPlainText("Tokopedia", Text);
    	clipboard.setPrimaryClip(clip);
	}

}
