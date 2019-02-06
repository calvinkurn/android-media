package com.tokopedia.core.util;

import com.tokopedia.core2.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class TransactionHideFilter {
	private boolean isVisible = false;
	private boolean isAnimating = true;
	private TranslateAnimation animOpen;
	private TranslateAnimation animClose;

    private View FilterButton;

    public static TransactionHideFilter createSmartWay(View view, Context context){
        TransactionHideFilter filter = new TransactionHideFilter(view.findViewById(R.id.btn_filter_hide), view.findViewById(R.id.read_wrapper), view.findViewById(R.id.filter_layout), (TextView) view.findViewById(R.id.sort_but), context);
        return filter;
    }
	
	public TransactionHideFilter(final View FilterButton,final View FilterContent, final View FilterLayout, final TextView chev, final Context context){
		FilterContent.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				if(FilterContent.getHeight()>0){
					try {
						FilterContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					} catch (NoSuchMethodError e) {
						FilterContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				}
				animOpen = new TranslateAnimation(0, 0, FilterContent.getHeight(), 0);
				animClose = new TranslateAnimation(0, 0, 0, FilterContent.getHeight());
				AnimationListener listen = new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						FilterContent.clearAnimation();
						if(!isVisible)
							FilterContent.setVisibility(View.GONE);
						isAnimating = false;
						}
				};
				animOpen.setAnimationListener(listen);
				animClose.setAnimationListener(listen);
				animOpen.setDuration(100);
				animClose.setDuration(100);
//				animOpen.setFillAfter(true);
//				animClose.setFillAfter(true);
				FilterLayout.startAnimation(animClose);
			}
		});
		FilterButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isVisible && !isAnimating){
						isVisible = false;
						isAnimating = true;
						FilterLayout.startAnimation(animClose);
						chev.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_find_previous_holo_light), null);
						
					}else if(!isVisible && !isAnimating){
						isVisible = true;
						isAnimating = true;
						FilterContent.setVisibility(View.VISIBLE);
						FilterLayout.startAnimation(animOpen);
						chev.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_find_next_holo_light), null);
					}
				}
			});
        this.FilterButton = FilterButton;
		}

    public void triggerFilterTab(){
       FilterButton.callOnClick();
    }

	public void closeTab(){
		if(isVisible && !isAnimating){
			FilterButton.callOnClick();
		}
	}
}
