package com.tokopedia.gamification.floating.view.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.data.entity.TokenFloatingEntity;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.di.GamificationComponentInstance;
import com.tokopedia.gamification.floating.listener.OnDragTouchListener;
import com.tokopedia.gamification.floating.view.contract.FloatingEggContract;
import com.tokopedia.gamification.floating.view.presenter.FloatingEggPresenter;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hendry on 28/03/18.
 */

public class FloatingEggButtonFragment extends BaseDaggerFragment implements FloatingEggContract.View {

    private static final String COORD_X = "x";
    private static final String COORD_Y = "y";
    public static final String COORD_EGG_PREF = "_egg.pref";
    public static final float SCALE_ON_DOWN = 0.95f;
    public static final float SCALE_NORMAL = 1f;
    public static final int SHORT_ANIMATION_DURATION = 300;
    public static final int LONG_ANIMATION_DURATION = 600;

    private View vgRoot;
    private View vgFloatingEgg;
    private ImageView ivFloatingEgg;
    private TextView tvFloatingCounter;
    private TextView tvFloatingTimer;

    private float initialEggMarginRight;
    private float initialEggMarginBottom;
    private int rootWidth;
    private boolean isDraggable;

    private CountDownTimer countDownTimer;
    private Handler visibilityHandler;
    private Runnable visibilityRunnableToShow;

    @Inject
    public FloatingEggPresenter floatingEggPresenter;
    private boolean isHideAnimating;
    private boolean needHideFloatingToken = true;
    private OnDragListener onDragListener;

    public static FloatingEggButtonFragment newInstance() {
        return new FloatingEggButtonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_floating_egg, container, false);
        vgRoot = view.findViewById(R.id.vg_root);
        vgFloatingEgg = view.findViewById(R.id.vg_floating_egg);
        ivFloatingEgg = view.findViewById(R.id.iv_floating_egg);
        tvFloatingCounter = view.findViewById(R.id.tv_floating_counter);
        tvFloatingTimer = view.findViewById(R.id.tv_floating_timer);
        vgFloatingEgg.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void initInjector() {
        GamificationComponent gamificationComponent =
                GamificationComponentInstance.getComponent(getActivity().getApplication());
        gamificationComponent.inject(this);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        applyAttrs(context, attrs, savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        applyAttrs(activity, attrs, savedInstanceState);
    }

    void applyAttrs(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingEggButtonFragment);

        isDraggable = a.getBoolean(R.styleable.FloatingEggButtonFragment_draggable, false);
        initialEggMarginRight = a.getDimensionPixelOffset(R.styleable.FloatingEggButtonFragment_margin_right, 0);
        initialEggMarginBottom = a.getDimensionPixelOffset(R.styleable.FloatingEggButtonFragment_margin_bottom, 0);
        a.recycle();
    }

    public void hideOnScrolling() {
        hideFloatingEggAnimate();
        showFloatingEggAnimate(true);
    }

    public void hideFloatingEggAnimate() {
        if (vgFloatingEgg.getVisibility() == View.GONE || isHideAnimating) {
            return;
        }
        isHideAnimating = true;
        vgFloatingEgg.setVisibility(View.VISIBLE);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_NORMAL, 0);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_NORMAL, 0);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(vgFloatingEgg, pvhScaleX, pvhScaleY);
        objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
        objectAnimator.setDuration(SHORT_ANIMATION_DURATION);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                vgFloatingEgg.setVisibility(View.GONE);
                isHideAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    public void showFloatingEggAnimate(boolean hasDelay) {
        if (needHideFloatingToken) {
            return;
        }
        if (vgFloatingEgg.getVisibility() == View.VISIBLE && !isHideAnimating) {
            return;
        }
        if (hasDelay) {
            if (visibilityHandler == null) {
                visibilityHandler = new Handler();
            }
            if (visibilityRunnableToShow == null) {
                visibilityRunnableToShow = new Runnable() {
                    @Override
                    public void run() {
                        showFloatingEggAnimate(false);
                    }
                };
            }
            visibilityHandler.removeCallbacks(visibilityRunnableToShow);
            visibilityHandler.postDelayed(visibilityRunnableToShow, LONG_ANIMATION_DURATION);
        } else {
            vgFloatingEgg.setVisibility(View.VISIBLE);
            PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0, SCALE_NORMAL);
            PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0, SCALE_NORMAL);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(vgFloatingEgg, pvhScaleX, pvhScaleY);
            objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
            objectAnimator.setDuration(SHORT_ANIMATION_DURATION);
            objectAnimator.start();
        }
    }

    private void showFloatingEgg() {
        vgFloatingEgg.setScaleX(SCALE_NORMAL);
        vgFloatingEgg.setScaleY(SCALE_NORMAL);
        vgFloatingEgg.setVisibility(View.VISIBLE);
    }

    private void hideFLoatingEgg() {
        vgFloatingEgg.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (floatingEggPresenter.isUserLogin()) {
            vgRoot.setVisibility(View.VISIBLE);
            initDragBound();
            initEggCoordinate();
        } else {
            vgRoot.setVisibility(View.GONE);
        }
    }

    private void initDragBound() {
        vgRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onInflateRoot();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    vgRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    vgRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void onInflateRoot() {
        rootWidth = vgRoot.getWidth();
        if (isDraggable) {
            int xEgg = (int) vgFloatingEgg.getX();
            int yEgg = (int) vgFloatingEgg.getY();
            if (xEgg == 0 && yEgg == 0) {
                return;
            }
            if (((xEgg + (vgFloatingEgg.getWidth() / 2))) >= (rootWidth / 2)) {
                int targetX = rootWidth - vgFloatingEgg.getWidth();
                setCoordFloatingEgg(targetX, yEgg);
                saveCoordPreference(targetX, yEgg);
            } else {
                setCoordFloatingEgg(0, yEgg);
                saveCoordPreference(0, yEgg);
            }
        }
    }

    private void initEggCoordinate() {
        if (isDraggable && hasCoordPreference()) {
            int coordPref[] = getCoordPreference();
            setCoordFloatingEgg(coordPref[0], coordPref[1]);
        } else {
            if (initialEggMarginRight != 0 || initialEggMarginBottom != 0) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vgFloatingEgg.getLayoutParams();
                layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                Resources r = getResources();
                int bottomPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        initialEggMarginBottom, r.getDisplayMetrics());
                int rightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        initialEggMarginRight, r.getDisplayMetrics());
                layoutParams.setMargins(0, 0, rightPx, bottomPx);
            } else {
                setCoordFloatingEgg(0, 0);
            }
        }
    }

    private void setCoordFloatingEgg(int x, int y) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vgFloatingEgg.getLayoutParams();
        layoutParams.setMargins(x, y, 0, 0);
    }

    private boolean hasCoordPreference() {
        int coord[] = getCoordPreference();
        return coord[0] != -1 && coord[1] != -1;
    }

    private int[] getCoordPreference() {
        SharedPreferences sharedPreferences = getSharedPref();
        int xEgg = sharedPreferences.getInt(COORD_X, -1);
        int yEgg = sharedPreferences.getInt(COORD_Y, -1);
        return new int[]{xEgg, yEgg};
    }

    private void saveCoordPreference(int x, int y) {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putInt(COORD_X, x);
        editor.putInt(COORD_Y, y);
        editor.apply();
    }

    private SharedPreferences getSharedPref() {
        return getContext().getSharedPreferences(
                getActivity().getClass().getSimpleName() + COORD_EGG_PREF
                , MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (floatingEggPresenter.isUserLogin()) {
            loadEggData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCountdownTimer();
        if (floatingEggPresenter != null) {
            floatingEggPresenter.detachView();
        }
        removeShowAnimationCallback();
    }

    private void removeShowAnimationCallback() {
        if (visibilityRunnableToShow != null) {
            visibilityHandler.removeCallbacks(visibilityRunnableToShow);
            visibilityRunnableToShow = null;
        }
    }

    public void loadEggData() {
        if (floatingEggPresenter.isUserLogin()) {
            removeShowAnimationCallback();
            floatingEggPresenter.attachView(this);
            floatingEggPresenter.getGetTokenTokopoints();
        }
    }

    @Override
    public void onSuccessGetToken(final TokenDataEntity tokenData) {
        final String sumTokenString = tokenData.getSumTokenStr();

        TokenFloatingEntity tokenFloating = tokenData.getFloating();
        final String pageUrl = tokenFloating.getPageUrl();
        final String appLink = tokenFloating.getApplink();

        final long timeRemainingSeconds = tokenFloating.getTimeRemainingSeconds();
        final boolean isShowTime = tokenFloating.getShowTime();
        String imageUrl = tokenFloating.getTokenAsset().getFloatingImgUrl();

        needHideFloatingToken = tokenData.getOffFlag() || tokenData.getSumToken() == 0 ||
                TextUtils.isEmpty(imageUrl);

        if (needHideFloatingToken) {
            hideFLoatingEgg();
        } else {
            showFloatingEgg();
            trackingEggImpression(String.valueOf(tokenData.getFloating().getTokenId()));
        }

        vgFloatingEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplinkUtil.navigateToAssociatedPage(getActivity(), "tokopedia://gamification2", pageUrl, CrackTokenActivity.class);
                trackingEggClick(String.valueOf(tokenData.getFloating().getTokenId()));
            }
        });

        if (isDraggable) {
            vgFloatingEgg.setOnTouchListener(new OnDragTouchListener(vgFloatingEgg, vgRoot,
                    new OnDragTouchListener.OnDragActionListener() {
                        @Override
                        public void onDragStart(View view) {
                            if (onDragListener != null) {
                                onDragListener.onDragStart();
                            }
                            vgFloatingEgg.setScaleX(SCALE_ON_DOWN);
                            vgFloatingEgg.setScaleY(SCALE_ON_DOWN);
                        }

                        @Override
                        public void onDragEnd(View view) {
                            if (onDragListener != null) {
                                onDragListener.onDragEnd();
                            }
                            animateToLeftOrRightBound();
                        }
                    }));
        } else {
            vgFloatingEgg.setOnTouchListener(null);
        }

        tvFloatingCounter.setVisibility(View.GONE);
        tvFloatingTimer.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl.endsWith(".gif")) {
                Glide.with(getContext())
                        .load(imageUrl)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .signature(new StringSignature(String.valueOf(tokenFloating.getTokenAsset().getVersion())))
                        .into(new ImageViewTarget<GifDrawable>(ivFloatingEgg) {
                            @Override
                            protected void setResource(GifDrawable resource) {
                                ivFloatingEgg.setImageDrawable(resource);
                                resource.start();
                                onFloatingEggLoaded(sumTokenString, isShowTime, timeRemainingSeconds);

                            }
                        });
            } else {
                Glide.with(getContext())
                        .load(imageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .signature(new StringSignature(String.valueOf(tokenFloating.getTokenAsset().getVersion())))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                ivFloatingEgg.setImageBitmap(resource);
                                onFloatingEggLoaded(sumTokenString, isShowTime, timeRemainingSeconds);
                            }
                        });

            }
        }
    }

    private void onFloatingEggLoaded(String sumTokenString, boolean isShowTime, long timeRemainingSeconds) {
        if (TextUtils.isEmpty(sumTokenString)) {
            tvFloatingCounter.setVisibility(View.GONE);
        } else {
            tvFloatingCounter.setText(sumTokenString);
            tvFloatingCounter.setVisibility(View.VISIBLE);
        }

        if (isShowTime && timeRemainingSeconds > 0) {
            setUIFloatingTimer(timeRemainingSeconds);
            startCountdownTimer(timeRemainingSeconds);
            tvFloatingTimer.setVisibility(View.VISIBLE);
        } else {
            stopCountdownTimer();
            tvFloatingTimer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorGetToken(Throwable throwable) {
        stopCountdownTimer();
        vgFloatingEgg.setVisibility(View.GONE);
        needHideFloatingToken = true;
    }

    private void startCountdownTimer(long timeRemainingSeconds) {
        stopCountdownTimer();
        countDownTimer = new CountDownTimer(timeRemainingSeconds * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isAdded()) {
                    return;
                }
                long ms = millisUntilFinished / 1000L;
                ms--;
                if (ms <= 0) {
                    vgFloatingEgg.setVisibility(View.GONE);
                    loadEggData();
                } else {
                    setUIFloatingTimer(ms);
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void stopCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void setUIFloatingTimer(long timeRemainingSeconds) {
        int seconds = (int) timeRemainingSeconds;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        minutes = minutes % 60;
        seconds = seconds % 60;
        tvFloatingTimer.setText(String.format(getString(R.string.countdown_format), hours, minutes, seconds));
    }

    private void animateToLeftOrRightBound() {
        int xEgg = (int) vgFloatingEgg.getX();
        int yEgg = (int) vgFloatingEgg.getY();
        if (rootWidth <= 0) {
            saveCoordPreference(xEgg, yEgg);
            return;
        }
        //if the egg tends to the right, animate to the right
        if (((xEgg + (vgFloatingEgg.getWidth() / 2))) >= (rootWidth / 2)) {
            int targetX = rootWidth - vgFloatingEgg.getWidth();
            PropertyValuesHolder pvhX =
                    PropertyValuesHolder.ofFloat(View.X, xEgg, targetX);
            PropertyValuesHolder pvhScaleX =
                    PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_ON_DOWN, SCALE_NORMAL);
            PropertyValuesHolder pvhScaleY =
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_ON_DOWN, SCALE_NORMAL);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(vgFloatingEgg, pvhX, pvhScaleX, pvhScaleY);
            objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
            objectAnimator.setDuration(SHORT_ANIMATION_DURATION);
            objectAnimator.start();

            saveCoordPreference(targetX, yEgg);
        } else {
            PropertyValuesHolder pvhX =
                    PropertyValuesHolder.ofFloat(View.X, xEgg, 0);
            PropertyValuesHolder pvhScaleX =
                    PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_ON_DOWN, SCALE_NORMAL);
            PropertyValuesHolder pvhScaleY =
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_ON_DOWN, SCALE_NORMAL);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(vgFloatingEgg, pvhX, pvhScaleX, pvhScaleY);
            objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
            objectAnimator.setDuration(SHORT_ANIMATION_DURATION);
            objectAnimator.start();

            saveCoordPreference(0, yEgg);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void trackingEggImpression(String idToken) {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            ((AbstractionRouter) getActivity().getApplication())
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                            GamificationEventTracking.Category.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Action.IMPRESSION_LUCKY_EGG,
                            idToken
                    );
        }
    }

    private void trackingEggClick(String idToken) {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            ((AbstractionRouter) getActivity().getApplication())
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Category.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Action.CLICK_LUCKY_EGG,
                            idToken
                    );
        }
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    public interface OnDragListener {
        void onDragStart();
        void onDragEnd();
    }
}