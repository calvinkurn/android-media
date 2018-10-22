package com.tokopedia.core.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ViewHelper;
import com.tokopedia.core2.R;

public class SlideOffViewHandler {
	private float startY;
	private float currY;
	private float deltaY;
	private boolean isDown = false;
	
	public interface SlideOffMotionEventListener{
		void OnMoveDown();
		void OnMoveUp();
	}
	
	public void AddFakeHeader(Activity context, ListView lv,final View header){
		View LVHeader = View.inflate(context, R.layout.fake_footer_shop_info, null);
		header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		final View Blank = LVHeader.findViewById(R.id.blank);
		if (lv.getHeaderViewsCount() == 0) {
			lv.addHeaderView(LVHeader, "FakeHeader", false);
		}
		final ViewTreeObserver obs = header.getViewTreeObserver();
		obs.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				ViewHelper.setViewSize(Blank, header.getHeight());
				CommonUtils.dumper("header height: " + header.getHeight());
				if(header.getViewTreeObserver().isAlive())
					try {
						header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					} catch (NoSuchMethodError e) {
						header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						e.printStackTrace();
					}
				
			}
		});
	}
	
	public void AddFakeHeader(Activity context, ListView lv,final View header, Boolean ignore){
		View LVHeader = View.inflate(context, R.layout.fake_footer_shop_info, null);
		header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		final View Blank = LVHeader.findViewById(R.id.blank);
		if (ignore) {
			lv.addHeaderView(LVHeader, "FakeHeader", false);
		} else {
			if (lv.getHeaderViewsCount() == 0) {
				lv.addHeaderView(LVHeader, "FakeHeader", false);
			}
		}
		final ViewTreeObserver obs = header.getViewTreeObserver();
		obs.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				ViewHelper.setViewSize(Blank, header.getHeight());
				CommonUtils.dumper("header height: "+header.getHeight());
				if(header.getViewTreeObserver().isAlive())
					try {
						header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					} catch (Exception e) {
						header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
			}
		});
	}
	
	public void MotionEventListener(MotionEvent event, SlideOffMotionEventListener listener){
		switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	startY = event.getRawY();
	        	break;
	        case MotionEvent.ACTION_CANCEL:
	        	break;
	        case MotionEvent.ACTION_UP:
	        	isDown = false;
	        	currY = event.getRawY();
	        	deltaY = currY - startY;
	        	if(deltaY > 5)
	        		listener.OnMoveUp();
	        	else if(deltaY < -5)
	        		listener.OnMoveDown();
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	currY = event.getRawY();
	        	deltaY = currY - startY;
	        	if(startY!=currY)
	        		startY = currY;
	        	if(isDown){
		        	if(deltaY > 5)
		        		listener.OnMoveUp();
		        	else if(deltaY < -5)
		        		listener.OnMoveDown();
	        	}
	        	isDown = true;	        	
	            break;
	        default:
	            break;
		}
	}
	
	public void ToggleSlideOffScreen(View v, boolean isAtTop, boolean setVisible){
		if(v!=null){
			boolean valid = true;
			if(setVisible){
				if(v.getVisibility() == View.VISIBLE)
					valid = false;
			}
			else{
				if(v.getVisibility() == View.GONE)
					valid = false;
			}

			if(valid){
				Animation slideoff;
				int Direction;
				if(isAtTop)
					Direction = -1;
				else
					Direction = 1;
				if(!setVisible)
					slideoff = new TranslateAnimation(0, 0, 0, Direction * v.getHeight());
				else
					slideoff = new TranslateAnimation(0, 0, Direction * v.getHeight(), 0);
				slideoff.setDuration(250);
				v.startAnimation(slideoff);
				if(!setVisible)
					v.setVisibility(View.GONE);
				else
					v.setVisibility(View.VISIBLE);
			}
		}
	}
}
