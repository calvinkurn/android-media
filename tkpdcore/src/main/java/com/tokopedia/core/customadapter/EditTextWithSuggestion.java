package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author EkaCipta
 * EditText custom untuk memunculkan suggestion link product ketika menggunakan symbol '@'
 * @Important Gunakan setLoadingListener
 */
public class EditTextWithSuggestion extends EditText{
	
	public interface onSuggestionLoadingListener{
		void onLoadingStart();
		void onLoadingFinish();
	}
	
	private ArrayList<String> ProdNameList = new ArrayList<String>();
	private Map<String, String> ProdUrlMap = new HashMap<String, String>();
	Context context;
	private Timer idle;
	private boolean timerStart = false;
	private int LastIndex;
	private int LastLength;
	private PopupWindow suggestionList;
	private ListViewSimpleTxtOnly adapter;
	private ListView list;
	private onSuggestionLoadingListener listener; 

	public EditTextWithSuggestion(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if(SessionHandler.isUserHasShop(context)) Initiate();
	}
	
	public EditTextWithSuggestion(Context context) {
		super(context);
		this.context = context;
		if(SessionHandler.isUserHasShop(context)) Initiate();
	}
	
	public EditTextWithSuggestion(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		if(SessionHandler.isUserHasShop(context)) Initiate();
	}
	
	public void setOnSuggestionLoadingListener(onSuggestionLoadingListener listener){
		try {
			this.listener = listener;
		}
		catch(NullPointerException e){
			listener = new onSuggestionLoadingListener() {
				@Override
				public void onLoadingStart() {
				}
				
				@Override
				public void onLoadingFinish() {
				}
			};
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Initiate(){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		suggestionList = new PopupWindow(inflater.inflate(R.layout.popup_window_suggestion, null), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		suggestionList.setFocusable(true);
		list = (ListView)suggestionList.getContentView().findViewById(R.id.list);
		adapter = new ListViewSimpleTxtOnly((Activity)context, ProdNameList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setText(getText().toString().substring(0, LastIndex) + ProdUrlMap.get(ProdNameList.get(position)) + getText().toString().substring((LastIndex + 1 + (length() - LastLength)), length()));
				setSelection(length());
				suggestionList.dismiss();
			}
		});
		suggestionList.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				timerStart = false;
			}
		});
		
		idle = new Timer();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.TextView#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	protected void onTextChanged(final CharSequence text,final int start,
			final int lengthBefore, final int lengthAfter) {
		if(context == null) return;
		if(SessionHandler.isUserHasShop(context)){
			if(timerStart){
				idle.cancel();
				idle.purge();
				idle = new Timer();
				idle.schedule(new TimerTask() {
					
					@Override
					public void run() {
						EditTextWithSuggestion.this.post(new Runnable() {
							
							@Override
							public void run() {
								timerStart = false;
	//							ShowSuggestion();
								idle.cancel();
							}
						});
					}
				}, 2000);
			}
			if(text.length() > 0 && !timerStart){
				super.onTextChanged(text, start, lengthBefore, lengthAfter);
				if(start == 0 && text.charAt(start) == '@'){
					timerStart = true;
					LastIndex = start;
					LastLength = length();
				}
				else if(length() > 0 && start<length()){
					if(text.charAt(start) == '@' && text.charAt(start-1) == ' '){
						timerStart = true;
						LastIndex = start;
						LastLength = length();
					}
				}
			}
			else if(start == length() && timerStart){
				if(!text.toString().contains("@")){
					idle.cancel();
					idle.purge();
					timerStart = false;
				}
			}
		}
	}
	

	private void ShowSuggestion(){
//		suggestionMenu.getMenu().clear();
//		for (String string : ProdNameList) {
//			suggestionMenu.getMenu().add(string);
//		}
//		suggestionMenu.show();
//		suggestionList.showAsDropDown(this, 0, 0);
		suggestionList.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);
	}

}
