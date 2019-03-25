package com.tokopedia.tokopoints.view.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tokopoints.R;

public class TokoPointToolbar extends Toolbar {

    private ToolbarState currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT;

    TextView tvCouponCount;

    ImageView ivLeaderBoard, ivMyCoupon;
    Context mContext;


    public TokoPointToolbar(Context context) {
        super(context);
        inflateResource(context);
    }

    public TokoPointToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateResource(context);
    }

    public TokoPointToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateResource(context);
    }

    private void inflateResource(Context context) {
        mContext = context;
        View.inflate(context, R.layout.tp_toolbar, this);
        tvCouponCount = findViewById(R.id.tv_tpToolbar_couponCount);
        ivLeaderBoard = findViewById(R.id.tv_tpToolbar_leaderboard);
        ivMyCoupon = findViewById(R.id.iv_tpToolbar_coupon);
        switchToTransparentMode();
        setCouponCount(0);
    }

    enum ToolbarState {
        TOOLBAR_DARK, TOOLBAR_TRANSPARENT
    }

    public void setCouponCount(int couponCount) {
        if (couponCount == 0) {
            tvCouponCount.setVisibility(INVISIBLE);
        } else {
            tvCouponCount.setVisibility(VISIBLE);
            if (couponCount > 99) {
                tvCouponCount.setText(mContext.getString(R.string.moreCouponCount));
            } else {
                tvCouponCount.setText(String.format("%s", tvCouponCount));
            }
        }
    }

    public void switchToDarkMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_DARK)
            return;
        currentToolbarState = ToolbarState.TOOLBAR_DARK;
        ivLeaderBoard.setImageResource(R.drawable.ic_tp_leaderboard_grey);
        ivMyCoupon.setImageResource(R.drawable.ic_my_coupon_grey);
        this.setNavigationIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_back_grey));
    }

    public void switchToTransparentMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_TRANSPARENT)
            return;
        currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT;
        ivLeaderBoard.setImageResource(R.drawable.ic_leaderboard);
        ivMyCoupon.setImageResource(R.drawable.ic_my_coupon_white);
        this.setNavigationIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_back));
    }

    public void applyAlphaToToolbarBackground(float alpha) {
        setBackgroundColor(
                adjustAlpha(mContext.getResources().getColor(R.color.white), alpha));
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }


}
