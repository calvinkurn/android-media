package com.tokopedia.core.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;

/**
 * Created by Erry on 7/19/2016.
 */
public class FilterView extends LinearLayout implements View.OnClickListener {

    private LinearLayout filter_btn;
    private FrameLayout contentLayout;
    private View layout;
    private Drawable ic_expand;
    private Drawable ic_collapse;
    private TextView filterBtnText;
    private TranslateAnimation animOpen;
    private TranslateAnimation animClose;
    private boolean isVisible = false;
    private boolean isAnimating = false;
    private static final String TAG = FilterView.class.getSimpleName();

    public FilterView(Context context) {
        super(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        setOrientation(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FilterView, defStyle, 0);
        ic_expand = a.getDrawable(R.styleable.FilterView_ic_expand);
        ic_collapse = a.getDrawable(R.styleable.FilterView_ic_collapse);
        inflate(getContext(), R.layout.filter_view, this);
        filter_btn = (LinearLayout) findViewById(R.id.filter_btn);
        filterBtnText = (TextView) findViewById(R.id.filter_btn_text);
        filterBtnText.setText(a.getString(R.styleable.FilterView_filter_text));
        contentLayout = (FrameLayout) findViewById(R.id.content);
        filter_btn.setOnClickListener(this);
        contentLayout.setVisibility(GONE);
    }

    public View getLayout() {
        return layout;
    }

    public void setLayout(int resource) {
        layout = LayoutInflater.from(getContext()).inflate(resource, null);
        contentLayout.addView(layout);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if(layout.getHeight()>0){
                    try {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } catch (NoSuchMethodError e) {
                        layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
                animOpen = new TranslateAnimation(0, 0, layout.getHeight(), 0);
                animClose = new TranslateAnimation(0, 0, 0, layout.getHeight());
                Animation.AnimationListener listen = new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        contentLayout.clearAnimation();
                        if(!isVisible)
                            contentLayout.setVisibility(View.GONE);
                        isAnimating = false;
                    }
                };
                animOpen.setAnimationListener(listen);
                animClose.setAnimationListener(listen);
                animOpen.setDuration(100);
                animClose.setDuration(100);
            }
        });
    }

    public void toggle() {
        if(isVisible && !isAnimating){
            isVisible = false;
            isAnimating = true;
            startAnimation(animClose);
            filterBtnText.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_expand, null);

        }else if(!isVisible && !isAnimating){
            isVisible = true;
            isAnimating = true;
            contentLayout.setVisibility(View.VISIBLE);
            startAnimation(animOpen);
            filterBtnText.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_collapse, null);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.filter_btn) {
            toggle();

        }
    }
}
