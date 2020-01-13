package com.tokopedia.tokopoints.view.customview;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokopoints.R;

public class TokoPointToolbar extends Toolbar implements View.OnClickListener {

    private ToolbarState currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT;

    TextView tvCouponCount, tvToolbarTitle;

    ImageView ivMyCoupon;
    Context mContext;


    Drawable backArrowWhite, couponWhiteDrawable, couponGreyDrawable;

    private TransitionDrawable couponCrossfader;
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
        ivMyCoupon = findViewById(R.id.iv_tpToolbar_coupon);

        ivMyCoupon.setOnClickListener(this);

        initDrawableResources();

        setCouponCount(0, "");
        setNavIcon(context);
    }

    private void setNavIcon(Context context) {
        post(() -> {
            int NAV_ICON_POSITION = 1;
            View v = getChildAt(NAV_ICON_POSITION);
            if (v != null && v.getLayoutParams() instanceof Toolbar.LayoutParams && v instanceof AppCompatImageButton) {
                Toolbar.LayoutParams lp = (Toolbar.LayoutParams) v.getLayoutParams();
                lp.width = context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_48);
                v.setLayoutParams(lp);
                v.invalidate();
                v.requestLayout();
            }
        });
    }

    private void initDrawableResources() {

        backArrowWhite = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_new_action_back_tokopoints);

        couponWhiteDrawable = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_my_coupon_white);
        couponGreyDrawable = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_my_coupon_grey);
        couponCrossfader = new TransitionDrawable(new Drawable[]{couponGreyDrawable, couponWhiteDrawable});

        setNavigationIcon(backArrowWhite);
        ivMyCoupon.setImageDrawable(couponCrossfader);
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
        if (viewId == R.id.iv_tpToolbar_coupon) {
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

        int whiteColor = MethodChecker.getColor(getContext(), R.color.tp_toolbar_navigation_white_color);
        int greyColor = MethodChecker.getColor(getContext(), R.color.tp_toolbar_navigation_grey_color);

        couponCrossfader.reverseTransition(200);
        toggleNavigationIconColor(whiteColor, greyColor);

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor",
                whiteColor, greyColor);
        colorAnim.setDuration(200);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();

    }

    private void toggleNavigationIconColor(int startColor, int endColor) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && getNavigationIcon() != null) {
            ValueAnimator valueAnimator = ValueAnimator.ofArgb(startColor, endColor);
            valueAnimator.setDuration(200L);
            valueAnimator.addUpdateListener(animation -> {
                getNavigationIcon().setColorFilter((Integer) animation.getAnimatedValue(), PorterDuff.Mode.SRC_ATOP);
            });
            valueAnimator.start();
        }
    }

    public void switchToTransparentMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_TRANSPARENT)
            return;
        currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT;

        int whiteColor = MethodChecker.getColor(getContext(), R.color.tp_toolbar_navigation_white_color);
        int greyColor = MethodChecker.getColor(getContext(), R.color.tp_toolbar_navigation_grey_color);

        couponCrossfader.reverseTransition(200);
        toggleNavigationIconColor(greyColor, whiteColor);

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor", greyColor, whiteColor);
        colorAnim.setDuration(200);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }

    public void applyAlphaToToolbarBackground(float alpha) {
        setBackgroundColor(
                adjustAlpha(mContext.getResources().getColor(com.tokopedia.design.R.color.white), alpha));
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }


    private Drawable getBitmapDrawableFromVectorDrawable(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return ContextCompat.getDrawable(context, drawableId);
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

        void onToolbarMyCouponClick();
    }

}
