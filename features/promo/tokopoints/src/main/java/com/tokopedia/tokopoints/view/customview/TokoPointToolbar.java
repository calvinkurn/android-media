package com.tokopedia.tokopoints.view.customview;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tokopoints.R;

public class TokoPointToolbar extends Toolbar implements View.OnClickListener {

    private ToolbarState currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT;

    TextView tvCouponCount, tvToolbarTitle;

    ImageView ivLeaderBoard, ivMyCoupon;
    Context mContext;


    BitmapDrawable backArrowWhite, backArrowGrey, leaderboardWhiteDrawable,
            couponWhiteDrawable, leaderboardGreyDrawable, couponGreyDrawable;

    private TransitionDrawable arrowCrossfader, leaderboardCrossfader, couponCrossfader;
    private OnTokoPointToolbarClickListener clickListener;


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
        tvToolbarTitle = findViewById(R.id.tv_tpToolbar_title);
        tvCouponCount = findViewById(R.id.tv_tpToolbar_couponCount);
        ivLeaderBoard = findViewById(R.id.iv_tpToolbar_leaderboard);
        ivMyCoupon = findViewById(R.id.iv_tpToolbar_coupon);

        ivLeaderBoard.setOnClickListener(this);
        ivMyCoupon.setOnClickListener(this);

        initDrawableResources();

        setCouponCount(0, "");
    }

    private void initDrawableResources() {

        backArrowWhite = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_action_back);
        backArrowGrey = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_action_back_grey);

        leaderboardWhiteDrawable = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_leaderboard);
        leaderboardGreyDrawable = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_tp_leaderboard_grey);

        couponWhiteDrawable = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_my_coupon_white);
        couponGreyDrawable = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_my_coupon_grey);

        arrowCrossfader = new TransitionDrawable(new Drawable[]{backArrowGrey, backArrowWhite});

        leaderboardCrossfader = new TransitionDrawable(new Drawable[]{leaderboardGreyDrawable, leaderboardWhiteDrawable});

        couponCrossfader = new TransitionDrawable(new Drawable[]{couponGreyDrawable, couponWhiteDrawable});

        this.setNavigationIcon(arrowCrossfader);
        ivMyCoupon.setImageDrawable(couponCrossfader);
        ivLeaderBoard.setImageDrawable(leaderboardCrossfader);

        arrowCrossfader.startTransition(0);
        leaderboardCrossfader.startTransition(0);
        couponCrossfader.startTransition(0);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title.equals("Tokopedia"))
            return;
        super.setTitle(title);
        tvToolbarTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        if (null == clickListener)
            return;
        int viewId = v.getId();
        if (viewId == R.id.iv_tpToolbar_leaderboard) {
            clickListener.onToolbarLeaderboardClick();
        } else if (viewId == R.id.iv_tpToolbar_coupon) {
            clickListener.onToolbarMyCouponClick();
        }
    }

    public void setCouponCount(int couponCount, String countStr) {
        if (couponCount == 0) {
            tvCouponCount.setVisibility(INVISIBLE);
        } else {
            tvCouponCount.setVisibility(VISIBLE);
            tvCouponCount.setText(countStr);
        }
    }

    public void switchToDarkMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_DARK)
            return;

        currentToolbarState = ToolbarState.TOOLBAR_DARK;

        arrowCrossfader.reverseTransition(200);
        couponCrossfader.reverseTransition(200);
        leaderboardCrossfader.reverseTransition(200);

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor",
                Color.parseColor("#FFFFFF"), Color.parseColor("#666666"));
        colorAnim.setDuration(200);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();

    }

    public void switchToTransparentMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_TRANSPARENT)
            return;
        currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT;

        arrowCrossfader.reverseTransition(200);
        couponCrossfader.reverseTransition(200);
        leaderboardCrossfader.reverseTransition(200);


        ObjectAnimator colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor",
                Color.parseColor("#666666"), Color.parseColor("#FFFFFF"));
        colorAnim.setDuration(200);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
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


    private BitmapDrawable getBitmapDrawableFromVectorDrawable(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return (BitmapDrawable) ContextCompat.getDrawable(context, drawableId);
        } else
            return new BitmapDrawable(context.getResources(), getBitmapFromVectorDrawable(context, drawableId));
    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setOnTokoPointToolbarClickListener(OnTokoPointToolbarClickListener clickListener) {
        this.clickListener = clickListener;
    }

    enum ToolbarState {
        TOOLBAR_DARK, TOOLBAR_TRANSPARENT
    }

    public interface OnTokoPointToolbarClickListener {
        void onToolbarLeaderboardClick();

        void onToolbarMyCouponClick();
    }

}
