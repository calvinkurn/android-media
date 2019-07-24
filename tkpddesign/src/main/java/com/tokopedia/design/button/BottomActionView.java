package com.tokopedia.design.button;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nathan on 10/3/17.
 */

public class BottomActionView extends BaseCustomView {

    @DrawableRes
    public static final int DEFAULT_ICON = R.drawable.ic_search_icon;
    public static final int ANIMATION_DURATION = 400;

    private View linearLayoutButton1;
    private View linearLayoutButton2;

    private String label1;
    @DrawableRes
    private int icon1Res;
    private String label2;
    @DrawableRes
    private int icon2Res;
    private View vMark1;
    private View vMark2;
    private boolean isBav1Display, isBav2Display, isBav2IconDisplay;
    private ImageView icon2ImageView;
    private ImageView icon1ImageView;
    private boolean isShow = true;
    private ObjectAnimator hideAnimator;
    private ObjectAnimator showAnimator;

    public BottomActionView(Context context) {
        super(context);
        init();
    }

    public BottomActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.BottomActionView);
        try {
            icon1Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_1, DEFAULT_ICON);
            isBav1Display = styledAttributes.getBoolean(R.styleable.BottomActionView_bav_1_display, false);
            label1 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_1);
            icon2Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_2, DEFAULT_ICON);
            label2 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_2);
            isBav2Display = styledAttributes.getBoolean(R.styleable.BottomActionView_bav_2_display, false);
            isBav2IconDisplay = styledAttributes.getBoolean(R.styleable.BottomActionView_bav_2_icon_display, true);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    public Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return context.getResources().getDrawable(resId, context.getApplicationContext().getTheme());
        else
            return AppCompatResources.getDrawable(context, resId);
    }

    private void init() {
        View view = inflate(getContext(), getLayout(), this);
        linearLayoutButton1 = view.findViewById(R.id.linear_layout_button_1);
        icon1ImageView = (ImageView) linearLayoutButton1.findViewById(R.id.image_view_icon_1);
        TextView label1textView = (TextView) linearLayoutButton1.findViewById(R.id.text_view_label_1);
        vMark1 = linearLayoutButton1.findViewById(R.id.v_mark_1);

        View separatorView = (View) view.findViewById(R.id.view_separator);

        linearLayoutButton2 = view.findViewById(R.id.linear_layout_button_2);
        icon2ImageView = (ImageView) linearLayoutButton2.findViewById(R.id.image_view_icon_2);
        TextView label2textView = (TextView) linearLayoutButton2.findViewById(R.id.text_view_label_2);
        vMark2 = linearLayoutButton2.findViewById(R.id.v_mark_2);

        setFirstImageDrawable(icon1Res);
        if (!TextUtils.isEmpty(label1)) {
            label1textView.setText(label1);
        }
        setSecondImageDrawable(icon2Res);
        if (!TextUtils.isEmpty(label2)) {
            label2textView.setText(label2);
        }
        if (!TextUtils.isEmpty(label2)) {
            separatorView.setVisibility(View.VISIBLE);
            linearLayoutButton2.setVisibility(View.VISIBLE);
        } else {
            separatorView.setVisibility(View.GONE);
            linearLayoutButton2.setVisibility(View.VISIBLE);
        }
        if(isBav2Display){
            linearLayoutButton2.setVisibility(VISIBLE);
        }else{
            linearLayoutButton2.setVisibility(GONE);
        }

        if(isBav1Display){
            linearLayoutButton1.setVisibility(VISIBLE);
        }else{
            linearLayoutButton1.setVisibility(GONE);
        }
        if((isBav1Display ? 1 : 0) + (isBav2Display? 1 : 0) == 1){
            separatorView.setVisibility(View.GONE);
        }

        if(!isBav2IconDisplay) {
            icon2ImageView.setVisibility(View.GONE);
        }

        invalidate();
        requestLayout();
    }

    protected int getLayout() {
        return R.layout.widget_bottom_action_view;
    }

    public void setSecondImageDrawable(@DrawableRes int secondImageDrawable) {
        icon2ImageView.setImageDrawable(getDrawable(icon2ImageView.getContext(),secondImageDrawable));
    }

    public void setFirstImageDrawable(@DrawableRes int secondImageDrawable) {
        icon1ImageView.setImageDrawable(getDrawable(icon1ImageView.getContext(),secondImageDrawable));
    }

    public void setMarkLeft(boolean isVisible) {
        vMark1.setVisibility(isVisible? View.VISIBLE: View.INVISIBLE);
    }

    public void setMarkRight(boolean isVisible) {
        vMark2.setVisibility(isVisible? View.VISIBLE: View.INVISIBLE);
    }

    public void setButton1OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton1.setOnClickListener(onClickListener);
    }

    public void setButton2OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton2.setOnClickListener(onClickListener);
    }

    public void hide(){
        hide(true);
    }

    public void hide(boolean isAnimate) {
        if (isShow) {
            if (isAnimate) {
                if (this.getHeight() > 0) {
                    startHideAnimation();
                }
            } else {
                this.setTranslationY(this.getHeight());
            }
        }
        isShow = false;
    }

    public void show(){
        show(true);
    }

    public void show(boolean isAnimate) {
        if (!isShow) {
            if (isAnimate) {
                if (this.getHeight() > 0) {
                    startShowAnimation();
                }
            } else {
                this.setTranslationY(0);
            }
        }
        isShow = true;
    }

    public void startHideAnimation() {
        if (showAnimator!= null && showAnimator.isRunning()) {
            showAnimator.cancel();
            show(false);
            return;
        }
        if (hideAnimator == null) {
            hideAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, getHeight());
            hideAnimator.setDuration(ANIMATION_DURATION);
        }
        hideAnimator.start();
    }

    public void startShowAnimation() {
        if (hideAnimator!= null && hideAnimator.isRunning()) {
            hideAnimator.cancel();
            hide(false);
            return;
        }
        if (showAnimator == null) {
            showAnimator = ObjectAnimator.ofFloat(this, "translationY", getHeight(), 0);
            showAnimator.setDuration(ANIMATION_DURATION);
        }
        showAnimator.start();
    }

    public void hideBav2(){
        linearLayoutButton2.setVisibility(GONE);
        findViewById(R.id.view_separator).setVisibility(GONE);
    }

    public void showBav2(){
        linearLayoutButton2.setVisibility(VISIBLE);
        findViewById(R.id.view_separator).setVisibility(VISIBLE);
    }
}
