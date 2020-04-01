package com.tokopedia.core.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class SlideOffViewHandler {
	private float startY;
	private float currY;
	private float deltaY;
	private boolean isDown = false;
	
	public interface SlideOffMotionEventListener{
		void OnMoveDown();
		void OnMoveUp();
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
