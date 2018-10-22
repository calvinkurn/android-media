/*
 * Created By Kulomady on 11/26/16 12:47 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 12:47 AM
 */

package com.tokopedia.core.discovery.old;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.MethodChecker;

@Deprecated
public class HeaderHotAdapter extends PagerAdapter {
	private Context context;
	private int dp;
	private String descText;
	private Boolean isAllowScroll;
	private float startX;
	private float startY;
	private float currX;
	private float currY;
	private float deltaX;
	private float deltaY;

	
	public HeaderHotAdapter(Context context, String desc) {
		this.context=context;
		this.descText = desc;
	}
	

	@Override
	public int getCount() {
		return 2;
	//return GalImages.length;
	}
	 
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View layout = inflater.inflate(R.layout.vp_hot_prod, null);
	    TextView desc = (TextView) layout.findViewById(R.id.desc_text);
		ScrollView scrollViewDesc = (ScrollView) layout.findViewById(R.id.scroll_desc);

		scrollViewDesc.setOnTouchListener(ChooseDirectionScroll());

	    if (position == 0) {
	    	desc.setBackgroundColor(0x00000000);
	    	desc.setText(null);
	    } else if (position == 1) {
	    	if(descText!=null) {
				desc.setBackgroundColor(0xB3000000);
				desc.setText(MethodChecker.fromHtml(descText));
				int dp = (int) context.getResources().getDimension(R.dimen.padding_small);
				desc.setPadding(dp, dp, dp, dp);
			}
	    }
	    ((ViewPager) container).addView(layout,0);
		return layout;
	}

	/**
	 * to choose direction of scroll, whether scroll to right/left or down/up
	 * and to enable scroll description if the description have long text.
	 * @return
     */
	@NonNull
	private View.OnTouchListener ChooseDirectionScroll() {
		return new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						isAllowScroll = false;
						startX = event.getRawX();
						startY = event.getRawY();
						break;
					case MotionEvent.ACTION_CANCEL:
						break;
					case MotionEvent.ACTION_UP:
						currX = event.getRawX();
						currY = event.getRawY();
						deltaX = currX - startX;
						deltaY = currY - startY;
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					case MotionEvent.ACTION_MOVE:
						currX = event.getRawX();
						currY = event.getRawY();
						deltaX = currX - startX;
						deltaY = currY - startY;
						v.getParent().requestDisallowInterceptTouchEvent(true);
						if (deltaX > 20) {
							v.getParent().requestDisallowInterceptTouchEvent(false);
						}
						break;
					default:
						break;
				}
				return false;
			}
		};
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}


	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == (arg1);
	}
}
