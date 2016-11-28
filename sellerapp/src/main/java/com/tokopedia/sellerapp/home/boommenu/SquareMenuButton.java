package com.tokopedia.sellerapp.home.boommenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.boommenu.Eases.EaseType;
import com.tokopedia.tkpd.home.boommenu.Types.BoomType;
import com.tokopedia.tkpd.home.boommenu.Types.ButtonType;
import com.tokopedia.tkpd.home.boommenu.Types.ClickEffectType;
import com.tokopedia.tkpd.home.boommenu.Types.DimType;
import com.tokopedia.tkpd.home.boommenu.Types.OrderType;
import com.tokopedia.tkpd.home.boommenu.Types.PlaceType;
import com.tokopedia.tkpd.home.boommenu.Types.StateType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by normansyahputa on 9/9/16.
 */

public class SquareMenuButton
    extends FrameLayout
    implements
    CircleButton.OnCircleButtonClickListener,
    HamButton.OnHamButtonClickListener {

        // This param is used to optimizize the memory used.
        // When this param is set to true,
        // all the sub buttons will be created when needed
        // and will not be stored.
        public static final boolean MEMORY_OPTIMIZATION = true;

        public static final int MIN_CIRCLE_BUTTON_NUMBER = 1;
        public static final int MAX_CIRCLE_BUTTON_NUMBER = 9;
        public static final int MIN_HAM_BUTTON_NUMBER = 1;
        public static final int MAX_HAM_BUTTON_NUMBER = 4;
        public static final int MENU_SLIDE_MOVEMENT = 100;

        private ViewGroup animationLayout = null;
        private ViewGroup buttonLayout = null;

        private ShadowLayout shadowLayout;
        private FrameLayout frameLayout;
        private View ripple;

        private int[][] originalLocations = new int[MAX_CIRCLE_BUTTON_NUMBER][2];
        private int[][] startLocations = new int[MAX_CIRCLE_BUTTON_NUMBER][2];
        private int[][] endLocations = new int[MAX_CIRCLE_BUTTON_NUMBER][2];

        private boolean animationPlaying = false;
        private StateType state = StateType.CLOSED;

        // Params about buttons
        private int buttonNum = 0;
        private SquareButton[] circleButtons = new SquareButton[MAX_CIRCLE_BUTTON_NUMBER];  // this Array reference cannot never be null as initialized
        private HamButton[] hamButtons = new HamButton[MAX_HAM_BUTTON_NUMBER];   // this Array reference cannot never be null as initialized
        private Dot[] dots = new Dot[MAX_CIRCLE_BUTTON_NUMBER];
        private Bar[] bars = new Bar[MAX_HAM_BUTTON_NUMBER];
        private ShareLines shareLines = null;

        // Store the drawables of buttons
        private Drawable[] drawables = null;
        // Store the colors of buttons
        private int[][] colors = null;
        // Store the strings of buttons
        private String[] strings = null;

        // Is in action bar
        private boolean isInActionBar = false;
        // Is in list item
        private boolean isInList = false;
        // Boom button color
        private int boomButtonColor = 0;
        // Boom button pressed color
        private int boomButtonPressedColor = 0;

        // Frames of animations
        private int frames = 80;
        // Duration of animations
        private int duration = 8000;
        // Delay
        private int delay = 100;
        // Show order type
        private OrderType showOrderType = OrderType.DEFAULT;
        // Hide order type
        private OrderType hideOrderType = OrderType.DEFAULT;
        // Button type
        private ButtonType buttonType = ButtonType.CIRCLE;
        // Boom type
        private BoomType boomType = BoomType.HORIZONTAL_THROW;
        // Place type
        private PlaceType placeType = null;
        // Default dot width
        private int dotWidth = (int) Util.getInstance().dp2px(8);
        // Default dot width
        private int dotHeight = (int) Util.getInstance().dp2px(8);
        // Default close icon width
        private int closeWidth = (int) Util.getInstance().dp2px(15);
        // Default circle button width
        private int buttonWidth = (int) Util.getInstance().dp2px(88);
        // Default bar width
        private int barWidth = (int) Util.getInstance().dp2px(36);
        // Default bar height
        private int barHeight = (int) Util.getInstance().dp2px(6);
        // Default ham button width
        private int hamButtonWidth = 0;
        // Default ham button height
        private int hamButtonHeight = (int) Util.getInstance().dp2px(80);
        // Boom button radius
        private int boomButtonRadius = (int) Util.getInstance().dp2px(56);
        // Movement ease
        private EaseType showMoveEaseType = EaseType.EaseOutBack;
        private EaseType hideMoveEaseType = EaseType.EaseOutCirc;
        // Scale ease
        private EaseType showScaleEaseType = EaseType.EaseOutBack;
        private EaseType hideScaleEaseType = EaseType.EaseOutCirc;
        // Whether rotate
        private int rotateDegree = 720;
        // Rotate ease
        private EaseType showRotateEaseType = EaseType.EaseOutBack;
        private EaseType hideRotateEaseType = EaseType.Linear;
        // Auto dismiss
        private boolean autoDismiss = true;
        // Cancelable
        private boolean cancelable = true;
        // Dim value
        private DimType dimType = DimType.DIM_10;
        // Click effect
        private ClickEffectType clickEffectType = ClickEffectType.RIPPLE;
        // Sub buttons offsets of shadow
        private float subButtonsXOffsetOfShadow = 0;
        private float subButtonsYOffsetOfShadow = 0;
        private int subButtonTextColor = Color.WHITE;
        private ImageView.ScaleType subButtonImageScaleType = ImageView.ScaleType.CENTER;

        private BoomMenuButton.OnClickListener onClickListener = null;
        private BoomMenuButton.AnimatorListener animatorListener = null;
        private BoomMenuButton.OnSubButtonClickListener onSubButtonClickListener = null;

        private Context mContext;

        BoomMenuButton.GetAnimationRootLayout getAnimationRootLayout;
        private Close close;

    public SquareMenuButton(Context context) {
            this(context, null);
        }

        public SquareMenuButton(Context context, AttributeSet attrs) {
            super(context, attrs);

            mContext = context;

            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.BoomMenuButton, 0, 0);
            if (attr != null) {
                try {
                    isInActionBar = attr.getBoolean(R.styleable.BoomMenuButton_boom_inActionBar, false);
                    isInList = attr.getBoolean(R.styleable.BoomMenuButton_boom_inList, false);
                    boomButtonColor = attr.getColor(R.styleable.BoomMenuButton_boom_button_color,
                            ContextCompat.getColor(mContext, R.color.default_boom_button_color));
                    boomButtonPressedColor = attr.getColor(R.styleable.BoomMenuButton_boom_button_pressed_color,
                            ContextCompat.getColor(mContext, R.color.default_boom_button_color_pressed));
                } finally {
                    attr.recycle();
                }
            }

            if (isInActionBar || isInList) {
                LayoutInflater.from(context).inflate(R.layout.boom_menu_button_in_action_bar, this, true);
                frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoot();
                    }
                });
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    LayoutInflater.from(context).inflate(R.layout.boom_menu_button, this, true);
                } else {
                    LayoutInflater.from(context).inflate(R.layout.boom_menu_button_below_lollipop, this, true);
                }
                shadowLayout = (ShadowLayout) findViewById(R.id.shadow_layout);
                frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
                ripple = findViewById(R.id.ripple);

                setRipple(clickEffectType);
                setBoomButtonBackgroundColor(boomButtonPressedColor, boomButtonColor);
            }

            hamButtonWidth = (int) (Util.getInstance().getScreenWidth(getContext()) * 8 / 9
                    + Util.getInstance().dp2px(4));

            setWillNotDraw(false);
        }

        /**
         * Init the boom menu button.
         * Notice that you should call this NOT in your onCreate method.
         * Because the width and height of boom menu button is 0.
         * Call this in:
         * <p/>
         * (This method needs to be overrided in activity)
         * public void onWindowFocusChanged(boolean hasFocus) {
         * super.onWindowFocusChanged(hasFocus);
         * init(...);
         * }
         *
         * @param drawables          The drawables of images of sub buttons. Can not be null.
         * @param strings            The texts of sub buttons, ok to be null.
         * @param colors             The colors of sub buttons, including pressed-state and normal-state.
         * @param buttonType         The button type.
         * @param boomType           The boom type.
         * @param placeType          The place type.
         * @param showMoveEaseType   Ease type to move the sub buttons when showing.
         * @param showScaleEaseType  Ease type to scale the sub buttons when showing.
         * @param showRotateEaseType Ease type to rotate the sub buttons when showing.
         * @param hideMoveEaseType   Ease type to move the sub buttons when dismissing.
         * @param hideScaleEaseType  Ease type to scale the sub buttons when dismissing.
         * @param hideRotateEaseType Ease type to rotate the sub buttons when dismissing.
         * @param rotateDegree       Rotation degree.
         */
    public void init(
            Drawable[] drawables,
            String[] strings,
            int[][] colors,
            ButtonType buttonType,
            BoomType boomType,
            PlaceType placeType,
            EaseType showMoveEaseType,
            EaseType showScaleEaseType,
            EaseType showRotateEaseType,
            EaseType hideMoveEaseType,
            EaseType hideScaleEaseType,
            EaseType hideRotateEaseType,
            Integer rotateDegree) {

        // judge
        errorJudge(drawables, strings, colors, buttonType);

        this.buttonType = buttonType;
        this.boomType = boomType;
        if (placeType == null) throw new RuntimeException("Place type is null!");
        else this.placeType = placeType;
        if (showMoveEaseType != null) this.showMoveEaseType = showMoveEaseType;
        if (showScaleEaseType != null) this.showScaleEaseType = showScaleEaseType;
        if (showRotateEaseType != null) this.showRotateEaseType = showRotateEaseType;
        if (hideMoveEaseType != null) this.hideMoveEaseType = hideMoveEaseType;
        if (hideScaleEaseType != null) this.hideScaleEaseType = hideScaleEaseType;
        if (hideRotateEaseType != null) this.hideRotateEaseType = hideRotateEaseType;
        if (rotateDegree != null) this.rotateDegree = rotateDegree;

        if (buttonType.equals(ButtonType.CIRCLE)) {
            // circle buttons
            // create buttons
            buttonNum = drawables.length;

            if (isInList || MEMORY_OPTIMIZATION) {
                // store the drawables, THEN we will build the buttons when create them
                this.drawables = drawables;
                this.colors = colors;
                this.strings = strings;
            } else {
                for (int i = 0; i < buttonNum; i++) {
                    circleButtons[i] = new SquareButton(mContext);
                    circleButtons[i].setOnCircleButtonClickListener(this, i);
                    circleButtons[i].setDrawable(drawables[i]);
                    if (strings != null) circleButtons[i].setText(strings[i]);
                    circleButtons[i].setColor(colors[i][0], colors[i][1]);
                }
            }

            // create dots
            for (int i = 0; i < buttonNum; i++) {
                dots[i] = new Dot(mContext);
                dots[i].setColor(colors[i][1]);
            }

            // place dots according to the number of them and the place type
            placeDots();
        } else if (buttonType.equals(ButtonType.HAM)) {
            // hamburger button
            hamButtonWidth = Util.getInstance().getScreenWidth(getContext()) * 8 / 9;
            // create buttons
            buttonNum = drawables.length;

            if (isInList || MEMORY_OPTIMIZATION) {
                // store the drawables, THEN we will build the buttons when create them
                this.drawables = drawables;
                this.colors = colors;
                this.strings = strings;
            } else {
                for (int i = 0; i < buttonNum; i++) {
                    hamButtons[i] = new HamButton(mContext);
                    hamButtons[i].setOnHamButtonClickListener(this, i);
                    hamButtons[i].setDrawable(drawables[i]);
                    if (strings != null) hamButtons[i].setText(strings[i]);
                    hamButtons[i].setColor(colors[i][0], colors[i][1]);
                }
            }

            // create bars
            for (int i = 0; i < buttonNum; i++) {
                bars[i] = new Bar(mContext);
                bars[i].setColor(colors[i][1]);
            }

            // place bars according to the number of them and the place type
            placeBars();
        }
    }

    /**
     * Judge whether the input params to init boom menu button is incorrect.
     *
     * @param drawables  The drawables of the sub buttons.
     * @param strings    The texts of the sub buttons.
     * @param colors     The colors(including the pressed-state and normal-state) of the sub buttons.
     * @param buttonType The button type of the sub buttons.
     */
    private void errorJudge(
            Drawable[] drawables,
            String[] strings,
            int[][] colors,
            ButtonType buttonType) {
        if (drawables == null) {
            throw new RuntimeException("The button's drawables are null!");
        }
        if (colors == null) {
            throw new RuntimeException("The button's colors are null!");
        }
        if (buttonType.equals(ButtonType.CIRCLE)) {
            if (!(
                    MIN_CIRCLE_BUTTON_NUMBER <= drawables.length
                            && drawables.length <= MAX_CIRCLE_BUTTON_NUMBER)
                    || (strings != null
                    && !(
                    MIN_CIRCLE_BUTTON_NUMBER <= strings.length
                            && strings.length <= MAX_CIRCLE_BUTTON_NUMBER))
                    || !(
                    MIN_CIRCLE_BUTTON_NUMBER <= colors.length
                            && colors.length <= MAX_CIRCLE_BUTTON_NUMBER)) {
                throw new RuntimeException("The circle type button's length must be in [" +
                        MIN_CIRCLE_BUTTON_NUMBER + ", " +
                        MAX_CIRCLE_BUTTON_NUMBER + "]!");
            }
        } else if (buttonType.equals(ButtonType.HAM)) {
            if ((!(
                    MIN_HAM_BUTTON_NUMBER <= drawables.length
                            && drawables.length <= MAX_HAM_BUTTON_NUMBER))
                    || (strings != null
                    && !(
                    MIN_HAM_BUTTON_NUMBER <= strings.length
                            && strings.length <= MAX_HAM_BUTTON_NUMBER))
                    || !(
                    MIN_HAM_BUTTON_NUMBER <= colors.length
                            && colors.length <= MAX_HAM_BUTTON_NUMBER)) {
                throw new RuntimeException("The ham type button's length must be in [" +
                        MIN_HAM_BUTTON_NUMBER + ", " +
                        MAX_HAM_BUTTON_NUMBER + "]!");
            }
        }
    }

    /**
     * Place all dots to the boom menu botton.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void placeDots() {
        frameLayout.removeAllViews();
        LayoutParams[] ps = PlaceParamsFactory.getDotParams(
                placeType,
                frameLayout.getWidth(),
                frameLayout.getHeight(),
                dotWidth,
                dotHeight
        );

        if (placeType.SHARE_3_1.v <= placeType.v && placeType.v <= PlaceType.SHARE_9_2.v) {
            shareLines = new ShareLines(mContext);
            float[][] locations = new float[3][2];
            locations[0][0] = ps[0].leftMargin + dotWidth / 2;
            locations[0][1] = ps[0].topMargin + dotHeight / 2;
            locations[1][0] = ps[1].leftMargin + dotWidth / 2;
            locations[1][1] = ps[1].topMargin + dotHeight / 2;
            locations[2][0] = ps[2].leftMargin + dotWidth / 2;
            locations[2][1] = ps[2].topMargin + dotHeight / 2;
            shareLines.setLocations(locations);
            shareLines.setOffset(1);

            LayoutParams p
                    = new LayoutParams(frameLayout.getWidth(), frameLayout.getHeight());
            frameLayout.addView(shareLines, p);
        }

        for (int i = 0; i < buttonNum; i++) {
            frameLayout.addView(dots[i], ps[i]);
        }

        close = new Close(mContext);
        close.setAlpha(0f);
        LayoutParams params = new LayoutParams(closeWidth, closeWidth);
        params.gravity = Gravity.CENTER;
        frameLayout.addView(close, params);
    }

    /**
     * Place all bars to the boom menu botton.
     */
    private void placeBars() {
        frameLayout.removeAllViews();
        LayoutParams[] ps = PlaceParamsFactory.getBarParams(
                placeType,
                frameLayout.getWidth(),
                frameLayout.getHeight(),
                barWidth,
                barHeight
        );
        for (int i = 0; i < ps.length; i++) frameLayout.addView(bars[i], ps[i]);
    }

    /**
     * When the boom menu button is clicked.
     */
    private void shoot() {
        // create the buttons
        if (isInList || MEMORY_OPTIMIZATION) {
            if (buttonType.equals(ButtonType.CIRCLE)) {
                for (int i = 0; i < buttonNum; i++) {
                    circleButtons[i] = new SquareButton(mContext);
                    circleButtons[i].setOnCircleButtonClickListener(this, i);
                    circleButtons[i].setDrawable(drawables[i]);
                    if (strings != null) circleButtons[i].setText(strings[i]);
                    circleButtons[i].setColor(colors[i][0], colors[i][1]);
                }
            } else if (buttonType.equals(ButtonType.HAM)) {
                for (int i = 0; i < buttonNum; i++) {
                    hamButtons[i] = new HamButton(mContext);
                    hamButtons[i].setOnHamButtonClickListener(this, i);
                    hamButtons[i].setDrawable(this.drawables[i]);
                    if (this.strings != null) hamButtons[i].setText(this.strings[i]);
                    hamButtons[i].setColor(this.colors[i][0], this.colors[i][1]);
                    hamButtons[i].setShadowDx(subButtonsXOffsetOfShadow);
                    hamButtons[i].setShadowDy(subButtonsYOffsetOfShadow);
                    hamButtons[i].getTextView().setTextColor(subButtonTextColor);
                    // TODO to find a way to apply multiple colors if set on setTextViewColor(int[] colors)
                    hamButtons[i].getImageView().setScaleType(subButtonImageScaleType);
                    hamButtons[i].setRipple(clickEffectType);
                }
            }
//            setRipple(clickEffectType);
        }

        // listener
        if (onClickListener != null) onClickListener.onClick();
        // wait for the before animations finished
        if (animationPlaying) return;
        animationPlaying = true;
        // start all animations
        if (animationLayout == null) {
            animationLayout = createAnimationLayout();
        }
        if (buttonLayout == null){
            buttonLayout = createButtonLayout();
        }
        animationLayout.removeAllViews();
        buttonLayout.removeAllViews();
        placeButtonToView();
        // dim the animation layout
        dimAnimationLayout();
        animateMenuButton(true);
    }

    private ViewGroup createButtonLayout() {
        // this rootview is needed for adding boom menu to the root layout
        ViewGroup rootView = null;
        if(getAnimationRootLayout != null){
            rootView = getAnimationRootLayout.getRootView();
        }else {
            rootView = (ViewGroup) scanForActivity(mContext).getWindow().getDecorView();
        }
        FrameLayout buttonLayout = new FrameLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        buttonLayout.setLayoutParams(layoutParams);
        buttonLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(buttonLayout);
        return buttonLayout;
    }

    /**
     * Dim the background layout.
     */
    private void dimAnimationLayout() {
        animationLayout.setVisibility(VISIBLE);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), DimType.DIM_0.value, dimType.value);
        colorAnimation.setDuration(duration + delay); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                animationLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(animationLayout, "backgroundColor",
//                DimType.DIM_0.value,
//                dimType.value)
//                .setDuration(duration + delay);
//        objectAnimator.setEvaluator(new ArgbEvaluator());
//
//        objectAnimator.start();

        // share lines animation
        if (placeType.SHARE_3_1.v <= placeType.v && placeType.v <= PlaceType.SHARE_9_2.v) {
            ObjectAnimator shareLinesAnimator = ObjectAnimator.ofFloat(shareLines, "offset",
                    1f,
                    0f).setDuration(duration + delay);
            shareLinesAnimator.setStartDelay(0);
            shareLinesAnimator.start();
        }

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(buttonLayout, View.ALPHA,
                0f,
                1f).setDuration(duration + delay);
        alphaAnimator.setStartDelay(0);
        alphaAnimator.start();

        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(buttonLayout,
                View.Y, 100f, 0f).setDuration(duration + delay);
        yAnimator.setStartDelay(0);
        yAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animatorListener != null) animatorListener.toShow();
                state = StateType.OPENING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (animatorListener != null) animatorListener.showed();
                state = StateType.OPEN;
                animationPlaying = false;
            }
        });
        yAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animatorListener != null)
                    animatorListener.showing(animation.getAnimatedFraction());
            }
        });
        yAnimator.start();
    }

    private void placeButtonToView() {
        getEndLocations();
        for (int i = 0; i < buttonNum; i++) {
            dots[i].getLocationOnScreen(startLocations[i]);
            originalLocations[i] = startLocations[i];

            startLocations[i][0] -= (buttonWidth - dots[i].getWidth()) / 2;
            startLocations[i][1] -= (buttonWidth - dots[i].getHeight()) / 2;

            setShowAnimation(circleButtons[i], endLocations[i]);
        }
    }

    /**
     * Get end location of all sub buttons.
     */
    private void getEndLocations() {
        int width = Util.getInstance().getScreenWidth(mContext);
        int height = Util.getInstance().getScreenHeight(mContext);
        if (buttonType.equals(ButtonType.CIRCLE)) {
            endLocations = EndLocationsFactory.getEndLocations(
                    placeType, width, height, buttonWidth, buttonWidth);
        } else if (buttonType.equals(ButtonType.HAM)) {
            endLocations = EndLocationsFactory.getEndLocations(
                    placeType, width, height, hamButtonWidth, hamButtonHeight);
        }
    }

    /**
     * Create the background layout as a "canvas" of all animations and sub buttons.
     * Notice that we don't need to call this every time, we can set the visibility
     * of the background layout to hide it.
     *
     * @return The background layout.
     */
    private ViewGroup createAnimationLayout() {
        // this rootview is needed for adding boom menu to the root layout
        ViewGroup rootView = null;
        if(getAnimationRootLayout != null){
            rootView = getAnimationRootLayout.getRootView();
        }else {
            rootView = (ViewGroup) scanForActivity(mContext).getWindow().getDecorView();
        }
        FrameLayout animLayout = new FrameLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(layoutParams);
        animLayout.setBackgroundResource(android.R.color.background_dark);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * Set show animation of each sub button.
     *
     * @param button        The sub button.
     * @param endLocation   End location of the animation.
     */
    public void setShowAnimation(
            final View button,
            int[] endLocation) {
        button.bringToFront();

        float[] sl = new float[2];
        float[] el = new float[2];
        sl[0] = endLocation[0];
        el[0] = endLocation[0];
        sl[1] = endLocation[1] * 1.0f - MENU_SLIDE_MOVEMENT;
        el[1] = endLocation[1] * 1.0f;

        final View view = button;
        LayoutParams lp = new LayoutParams(buttonWidth, buttonWidth);
        lp.topMargin = endLocation[1];
        lp.leftMargin = endLocation[0];
        view.setVisibility(View.VISIBLE);
        buttonLayout.addView(view, lp);
    }

    /**
     * Start all animations about dismissing.
     */
    private void startHideAnimations() {
        animationPlaying = true;
        lightAnimationLayout();
        animateMenuButton(false);
    }

    private void animateMenuButton(boolean isShow) {
        this.bringToFront();
        float length = (isShow? -1: 1) * (getX() + getWidth()/2 - (getRootView().getWidth() / 2));
        for(int i = 0; i < buttonNum; i ++){
            float sButton = isShow ? 1f : 0f;
            float eButton = isShow ? 0f : 1f;
            ObjectAnimator animator = ObjectAnimator.ofFloat(dots[i], View.ALPHA, sButton, eButton);
            animator.setDuration(delay + duration);
            animator.start();
//            dots[i].setVisibility(isShow ? View.GONE : View.VISIBLE);
        }
//        close.setVisibility(isShow ? View.VISIBLE : View.GONE);
        float sClose = isShow ? 0f : 1f;
        float eClose = isShow ? 1f : 0f;
        ObjectAnimator animator = ObjectAnimator.ofFloat(close, View.ALPHA, sClose, eClose);
        animator.setDuration(delay + duration);
        animator.start();


        final View.OnClickListener listener = (isShow? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationPlaying) return;
                if (cancelable) startHideAnimations();
            }
        } : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationPlaying) return;
                shoot();
            }
        });
        setRipple(clickEffectType, listener);
        animate().setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(500)
                .setStartDelay(200)
                .translationX(length)
                .start();
        float start = isShow ? 360f : 0f;
        float end = isShow ? 0f : 360f;
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(this, View.ROTATION, start, end);
        rotateAnimator.setDuration(delay + duration);
        rotateAnimator.start();
    }

    /**
     * Light the background, used when the boom menu button is to dismiss.
     */
    public void lightAnimationLayout() {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), dimType.value,DimType.DIM_0.value);
        colorAnimation.setDuration(duration + delay); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                animationLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(animationLayout, "backgroundColor",
//                dimType.value,
//                DimType.DIM_0.value)
//                .setDuration(duration + delay * (buttonNum - 1));
//        objectAnimator.setEvaluator(new ArgbEvaluator());
//
//        objectAnimator.start();

        // share lines animation
        if (placeType.SHARE_3_1.v <= placeType.v && placeType.v <= PlaceType.SHARE_9_2.v) {
            ObjectAnimator shareLinesAnimator = ObjectAnimator.ofFloat(shareLines, "offset",
                    0f,
                    1f).setDuration(duration + delay * (buttonNum - 1));
            shareLinesAnimator.setStartDelay(0);
            shareLinesAnimator.start();
        }

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(buttonLayout, View.ALPHA,
                1f,
                0f).setDuration(duration + delay);
        alphaAnimator.setStartDelay(0);
        alphaAnimator.start();

        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(buttonLayout,
                View.Y, 0f, 100f).setDuration(duration + delay);
        yAnimator.setStartDelay(0);
        yAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animatorListener != null) animatorListener.toHide();
                state = StateType.CLOSING;
                animationPlaying = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animationLayout.removeAllViews();
                animationLayout.setVisibility(GONE);
                animationPlaying = false;
                if (animatorListener != null) animatorListener.hided();
                state = StateType.CLOSED;
            }
        });

        yAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animatorListener != null)
                    animatorListener.hiding(animation.getAnimatedFraction());
            }
        });
        yAnimator.start();


    }

    /**
     * Set auto dismiss. If the boom menu button is auto dismiss, user can click one
     * of the sub buttons to dismiss the boom menu botton.
     *
     * @param autoDismiss
     */
    public void setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }

    /**
     * Set cancelable. If the boom menu button is cancelable, user can click
     * the background to dismiss it.
     *
     * @param cancelable
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * Set frames for animaitons.
     *
     * @param frames
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * Set animation duration.
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Set start delay.
     *
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Set rotate degrees.
     *
     * @param rotateDegree
     */
    public void setRotateDegree(int rotateDegree) {
        this.rotateDegree = rotateDegree;
    }

    /**
     * Set show order type.
     *
     * @param showOrderType
     */
    public void setShowOrderType(OrderType showOrderType) {
        this.showOrderType = showOrderType;
    }

    /**
     * Set hide order type.
     *
     * @param hideOrderType
     */
    public void setHideOrderType(OrderType hideOrderType) {
        this.hideOrderType = hideOrderType;
    }

    /**
     * Set OnClickListener.
     *
     * @param onClickListener
     */
    public void setOnClickListener(BoomMenuButton.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Set AnimatorListener.
     *
     * @param animatorListener
     */
    public void setAnimatorListener(BoomMenuButton.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    /**
     * Set OnSubButtonClickListener.
     *
     * @param onSubButtonClickListener
     */
    public void setOnSubButtonClickListener(BoomMenuButton.OnSubButtonClickListener onSubButtonClickListener) {
        this.onSubButtonClickListener = onSubButtonClickListener;
    }

    /**
     * Set the color of pressed-state or normal-state of boom menu button.
     *
     * @param pressedColor
     * @param normalColor
     */
    public void setBoomButtonBackgroundColor(int pressedColor, int normalColor) {
        Util.getInstance().setCircleButtonStateListDrawable(
                frameLayout, boomButtonRadius, pressedColor, normalColor);
    }

    /**
     * Set the offset of the shadow layouts of boom menu button.
     * If xOffset is 0 and yOffset is 0, then the shadow layout is at the center.
     *
     * @param xOffset In pixels.
     * @param yOffset In pixels.
     */
    public void setBoomButtonShadowOffset(float xOffset, float yOffset) {
        if (shadowLayout != null) {
            shadowLayout.setmDx(xOffset);
            shadowLayout.setmDy(yOffset);
        }
    }

    /**
     * Set the dim type.
     * Dim_0 for no dim.
     * Max is Dim_9.
     *
     * @param dimType
     */
    public void setDimType(DimType dimType) {
        this.dimType = dimType;
    }

    /**
     * Set the click effect of the boom button.
     *
     * @param clickEffectType
     */
    private void setRipple(ClickEffectType clickEffectType) {
        this.clickEffectType = clickEffectType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && clickEffectType.equals(ClickEffectType.RIPPLE)
                && ripple != null) {
            ripple.setVisibility(VISIBLE);
            ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shoot();
                }
            });
        } else {
            if (ripple != null) ripple.setVisibility(GONE);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shoot();
                }
            });
        }
    }

    private void setRipple(ClickEffectType clickEffectType, View.OnClickListener listener) {
        this.clickEffectType = clickEffectType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && clickEffectType.equals(ClickEffectType.RIPPLE)
                && ripple != null) {
            ripple.setVisibility(VISIBLE);
            ripple.setOnClickListener(listener);
        } else {
            if (ripple != null) ripple.setVisibility(GONE);
            frameLayout.setOnClickListener(listener);
        }
    }


    /**
     * Set the color of all textviews in the sub buttons.
     *
     * @param color
     */
    public void setTextViewColor(int color) {
        for (int i = 0; i < buttonNum; i++) {
            if (buttonType.equals(ButtonType.CIRCLE)) {
                if (circleButtons[i] != null) {
                    circleButtons[i].getTextView().setTextColor(color);
                } else {
                    // delaying apply on the fly in the shoot() function
                    subButtonTextColor = color;
                }
            } else if (buttonType.equals(ButtonType.HAM)) {
                if (hamButtons[i] != null) {
                    hamButtons[i].getTextView().setTextColor(color);
                } else {
                    // delaying apply on the fly in the shoot() function
                    subButtonTextColor = color;
                }
            }
        }
    }

    /**
     * Set the color of all textviews in the sub buttons corresponding to the array.
     *
     * @param colors
     */
    // TODO for now doesnt work if MEMORY OPTIMIZATION is true
    public void setTextViewColor(int[] colors) {
        int length = Math.min(buttonNum, colors.length);
        if (buttonType.equals(ButtonType.CIRCLE)) {
            for (int i = 0; i < length; i++) {
                if (circleButtons[i] != null)
                    circleButtons[i].getTextView().setTextColor(colors[i]);
            }
        } else if (buttonType.equals(ButtonType.HAM)) {
            for (int i = 0; i < length; i++) {
                if (hamButtons[i] != null) hamButtons[i].getTextView().setTextColor(colors[i]);
            }
        }
    }

    /**
     * Set the scaleType of all imageviews in the sub buttons.
     *
     * @param scaleType
     */
    public void setImageViewScaleType(ImageView.ScaleType scaleType) {
        for (int i = 0; i < buttonNum; i++) {
            if (buttonType.equals(ButtonType.CIRCLE)) {
                if (circleButtons[i] != null) {
                    circleButtons[i].getImageView().setScaleType(scaleType);
                } else {
                    // delaying apply on the fly in the shoot() function
                    subButtonImageScaleType = scaleType;
                }
            } else if (buttonType.equals(ButtonType.HAM)) {
                if (hamButtons[i] != null) {
                    hamButtons[i].getImageView().setScaleType(scaleType);
                } else {
                    // delaying apply on the fly in the shoot() function
                    subButtonImageScaleType = scaleType;
                }
            }
        }
    }

    /**
     * Set the boom type of boom button.
     *
     * @param boomType
     */
    public void setBoomType(BoomType boomType) {
        this.boomType = boomType;
    }

    /**
     * @return
     */
    public boolean isClosed() {
        return state.equals(StateType.CLOSED);
    }

    /**
     * @return
     */
    public boolean isClosing() {
        return state.equals(StateType.CLOSING);
    }

    /**
     * @return
     */
    public boolean isOpen() {
        return state.equals(StateType.OPEN);
    }

    /**
     * @return
     */
    public boolean isOpening() {
        return state.equals(StateType.OPENING);
    }

    public void setShowMoveEaseType(EaseType showMoveEaseType) {
        this.showMoveEaseType = showMoveEaseType;
    }

    public void setShowScaleEaseType(EaseType showScaleEaseType) {
        this.showScaleEaseType = showScaleEaseType;
    }

    public void setShowRotateEaseType(EaseType showRotateEaseType) {
        this.showRotateEaseType = showRotateEaseType;
    }

    public void setHideMoveEaseType(EaseType hideMoveEaseType) {
        this.hideMoveEaseType = hideMoveEaseType;
    }

    public void setHideScaleEaseType(EaseType hideScaleEaseType) {
        this.hideScaleEaseType = hideScaleEaseType;
    }

    public void setHideRotateEaseType(EaseType hideRotateEaseType) {
        this.hideRotateEaseType = hideRotateEaseType;
    }

    /**
     * Get the click event from CircleButton or HamButton
     *
     * @param index
     */
    @Override
    public void onClick(int index) {
        if (!state.equals(StateType.OPEN)) return;
        if (onSubButtonClickListener != null) onSubButtonClickListener.onClick(index);
        if (autoDismiss && !animationPlaying) startHideAnimations();
    }

    /**
     * This interface tells when the boom menu button is clicked.
     */
    public interface OnClickListener {
        void onClick();
    }

    /**
     * Animation listener.
     */
    public interface AnimatorListener {
        void toShow();

        void showing(float fraction);

        void showed();

        void toHide();

        void hiding(float fraction);

        void hided();
    }

    /**
     * This interface return rootview
     */
    public interface GetAnimationRootLayout{
        ViewGroup getRootView();
    }

    /**
     * This interface return which button is clicked.
     */
    public interface OnSubButtonClickListener {
        void onClick(int buttonIndex);
    }

    /**
     * If the boom menu button is open, dismiss it.
     *
     * @return True if dismiss, false if can not dismiss.
     */
    public boolean dismiss() {
        if (state == StateType.OPEN) {
            startHideAnimations();
            return true;
        }
        return false;
    }

    /**
     * If the boom menu button is closed, open it.
     *
     * @return True if open, false if can not open.
     */
    public boolean boom() {
        if (state == StateType.CLOSED) {
            shoot();
            return true;
        }
        return false;
    }

    /**
     * Set the width of the share icon lines
     *
     * @param width
     */
    public void setShareLineWidth(float width) {
        if (shareLines != null) shareLines.setLineWidth(width);
    }

    /**
     * Set the color of the share icon line 1
     *
     * @param color
     */
    public void setShareLine1Color(int color) {
        if (shareLines != null) shareLines.setLine1Color(color);
    }

    /**
     * Set the color of the share icon line 2
     *
     * @param color
     */
    public void setShareLine2Color(int color) {
        if (shareLines != null) shareLines.setLine2Color(color);
    }

    public void setGetAnimationRootLayout(BoomMenuButton.GetAnimationRootLayout getAnimationRootLayout){
        this.getAnimationRootLayout = getAnimationRootLayout;
    }

    /**
     * Builder
     * Fuck this... That's so long...
     */
    public static class Builder {

        // required parameters
        private ArrayList<Drawable> drawables = null;
        private ArrayList<int[]> colors = null;
        private ArrayList<String> strings = null;

        private int frames = 80;

        private int duration = 800;

        private int delay = 100;

        private OrderType showOrderType = OrderType.DEFAULT;

        private OrderType hideOrderType = OrderType.DEFAULT;

        private ButtonType buttonType = ButtonType.CIRCLE;

        private BoomType boomType = BoomType.HORIZONTAL_THROW;

        private PlaceType placeType = null;

        private EaseType showMoveEaseType = EaseType.EaseOutBack;
        private EaseType hideMoveEaseType = EaseType.EaseOutCirc;

        private EaseType showScaleEaseType = EaseType.EaseOutBack;
        private EaseType hideScaleEaseType = EaseType.EaseOutCirc;

        private int rotateDegree = 720;
        private EaseType showRotateEaseType = EaseType.EaseOutBack;
        private EaseType hideRotateEaseType = EaseType.Linear;

        private boolean autoDismiss = true;

        private boolean cancelable = true;

        private DimType dimType = DimType.DIM_10;

        private ClickEffectType clickEffectType = ClickEffectType.RIPPLE;

        private float boomButtonXOffsetOfShadow = 0;
        private float boomButtonYOffsetOfShadow = 0;

        private float subButtonsXOffsetOfShadow = 0;
        private float subButtonsYOffsetOfShadow = 0;
        private int subButtonTextColor = Color.WHITE;
        private ImageView.ScaleType subButtonImageScaleType = ImageView.ScaleType.CENTER;

        private BoomMenuButton.OnClickListener onClickListener = null;
        private BoomMenuButton.AnimatorListener animatorListener = null;
        private BoomMenuButton.OnSubButtonClickListener onSubButtonClickListener = null;
        private BoomMenuButton.GetAnimationRootLayout getAnimationRootLayout = null;

        private float shareLineWidth = 3f;
        private int shareLine1Color = Color.WHITE;
        private int shareLine2Color = Color.WHITE;

        public SquareMenuButton.Builder subButtons(ArrayList<Drawable> drawables, ArrayList<int[]> colors, ArrayList<String> strings) {
            this.drawables = drawables;
            this.colors = colors;
            this.strings = strings;
            return this;
        }

        public SquareMenuButton.Builder subButtons(Drawable[] drawables, int[][] colors, String[] strings) {
            this.drawables = new ArrayList<>(Arrays.asList(drawables));
            this.colors = new ArrayList<>(Arrays.asList(colors));
            this.strings = new ArrayList<>(Arrays.asList(strings));
            return this;
        }

        public SquareMenuButton.Builder frames(int frames) {
            this.frames = frames;
            return this;
        }

        public SquareMenuButton.Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public SquareMenuButton.Builder delay(int delay) {
            this.delay = delay;
            return this;
        }

        public SquareMenuButton.Builder showOrder(OrderType showOrderType) {
            this.showOrderType = showOrderType;
            return this;
        }

        public SquareMenuButton.Builder hideOrder(OrderType hideOrderType) {
            this.hideOrderType = hideOrderType;
            return this;
        }

        public SquareMenuButton.Builder button(ButtonType buttonType) {
            this.buttonType = buttonType;
            return this;
        }

        public SquareMenuButton.Builder boom(BoomType boomType) {
            this.boomType = boomType;
            return this;
        }

        public SquareMenuButton.Builder place(PlaceType placeType) {
            this.placeType = placeType;
            return this;
        }

        public SquareMenuButton.Builder showMoveEase(EaseType showMoveEaseType) {
            this.showMoveEaseType = showMoveEaseType;
            return this;
        }

        public SquareMenuButton.Builder hideMoveEase(EaseType hideMoveEaseType) {
            this.hideMoveEaseType = hideMoveEaseType;
            return this;
        }

        public SquareMenuButton.Builder showScaleEase(EaseType showScaleEaseType) {
            this.showScaleEaseType = showScaleEaseType;
            return this;
        }

        public SquareMenuButton.Builder hideScaleType(EaseType hideScaleEaseType) {
            this.hideScaleEaseType = hideScaleEaseType;
            return this;
        }

        public SquareMenuButton.Builder rotateDegree(int rotateDegree) {
            this.rotateDegree = rotateDegree;
            return this;
        }

        public SquareMenuButton.Builder showRotateEase(EaseType showRotateEaseType) {
            this.showRotateEaseType = showRotateEaseType;
            return this;
        }

        public SquareMenuButton.Builder hideRotateType(EaseType hideRotateEaseType) {
            this.hideRotateEaseType = hideRotateEaseType;
            return this;
        }

        public SquareMenuButton.Builder autoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        public SquareMenuButton.Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public SquareMenuButton.Builder dim(DimType dimType) {
            this.dimType = dimType;
            return this;
        }

        public SquareMenuButton.Builder clickEffect(ClickEffectType clickEffectType) {
            this.clickEffectType = clickEffectType;
            return this;
        }

        public SquareMenuButton.Builder boomButtonShadow(float boomButtonXOffsetOfShadow, float boomButtonYOffsetOfShadow) {
            this.boomButtonXOffsetOfShadow = boomButtonXOffsetOfShadow;
            this.boomButtonYOffsetOfShadow = boomButtonYOffsetOfShadow;
            return this;
        }

        public SquareMenuButton.Builder subButtonsShadow(float subButtonsXOffsetOfShadow, float subButtonsYOffsetOfShadow) {
            this.subButtonsXOffsetOfShadow = subButtonsXOffsetOfShadow;
            this.subButtonsYOffsetOfShadow = subButtonsYOffsetOfShadow;
            return this;
        }

        public SquareMenuButton.Builder subButtonTextColor(int subButtonTextColor) {
            this.subButtonTextColor = subButtonTextColor;
            return this;
        }

        public SquareMenuButton.Builder subButtonImageScaleType(ImageView.ScaleType subButtonImageScaleType) {
            this.subButtonImageScaleType = subButtonImageScaleType;
            return this;
        }

        public SquareMenuButton.Builder onBoomButtonBlick(BoomMenuButton.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public SquareMenuButton.Builder animator(BoomMenuButton.AnimatorListener animatorListener) {
            this.animatorListener = animatorListener;
            return this;
        }

        public SquareMenuButton.Builder onSubButtonClick(BoomMenuButton.OnSubButtonClickListener onSubButtonClickListener) {
            this.onSubButtonClickListener = onSubButtonClickListener;
            return this;
        }

        public SquareMenuButton.Builder onGetAnimationRootLayout(BoomMenuButton.GetAnimationRootLayout getAnimationRootLayout){
            this.getAnimationRootLayout = getAnimationRootLayout;
            return this;
        }

        public SquareMenuButton.Builder shareStyle(float shareLineWidth, int shareLine1Color, int shareLine2Color) {
            this.shareLineWidth = shareLineWidth;
            this.shareLine1Color = shareLine1Color;
            this.shareLine2Color = shareLine2Color;
            return this;
        }

        /**
         * Add a sub button with 3 params.
         *
         * @param drawable
         * @param twoColors
         * @param string
         * @return
         */
        public SquareMenuButton.Builder addSubButton(Drawable drawable, int[] twoColors, String string) {
            if (drawables == null) drawables = new ArrayList<>();
            drawables.add(drawable);
            if (colors == null) colors = new ArrayList<>();
            colors.add(twoColors);
            if (strings == null) strings = new ArrayList<>();
            strings.add(string);
            return this;
        }

        /**
         * Add a sub button with 2 params.
         *
         * @param drawable
         * @param twoColors
         * @return
         */
        public SquareMenuButton.Builder addSubButton(Drawable drawable, int[] twoColors) {
            if (drawables == null) drawables = new ArrayList<>();
            drawables.add(drawable);
            if (colors == null) colors = new ArrayList<>();
            colors.add(twoColors);
            return this;
        }

        /**
         * Add a sub button with 4 params.
         *
         * @param context
         * @param drawable
         * @param twoColors
         * @param string
         * @return
         */
        public SquareMenuButton.Builder addSubButton(Context context, int drawable, int[] twoColors, String string) {
            if (drawables == null) drawables = new ArrayList<>();
            drawables.add(ContextCompat.getDrawable(context, drawable));
            if (colors == null) colors = new ArrayList<>();
            colors.add(twoColors);
            if (strings == null) strings = new ArrayList<>();
            strings.add(string);
            return this;
        }

        /**
         * Add a sub button with 3 params.
         *
         * @param context
         * @param drawable
         * @param twoColors
         * @return
         */
        public SquareMenuButton.Builder addSubButton(Context context, int drawable, int[] twoColors) {
            if (drawables == null) drawables = new ArrayList<>();
            drawables.add(ContextCompat.getDrawable(context, drawable));
            if (colors == null) colors = new ArrayList<>();
            colors.add(twoColors);
            return this;
        }

        public SquareMenuButton init(SquareMenuButton boomMenuButton) {
            if (boomMenuButton == null) throw new RuntimeException("BMB is null!");
            Drawable[] drawablesInArray = new Drawable[drawables.size()];
            for (int i = 0; i < drawables.size(); i++) drawablesInArray[i] = drawables.get(i);
            String[] stringsInArray = new String[strings.size()];
            for (int i = 0; i < strings.size(); i++) stringsInArray[i] = strings.get(i);
            int[][] colorsInArray = new int[colors.size()][2];
            for (int i = 0; i < colors.size(); i++) colorsInArray[i] = colors.get(i);
            boomMenuButton.init(
                    drawablesInArray,
                    stringsInArray,
                    colorsInArray,
                    buttonType,
                    boomType,
                    placeType,
                    showMoveEaseType,
                    showScaleEaseType,
                    showRotateEaseType,
                    hideMoveEaseType,
                    hideScaleEaseType,
                    hideRotateEaseType,
                    rotateDegree);
            boomMenuButton.setFrames(frames);
            boomMenuButton.setDuration(duration);
            boomMenuButton.setDelay(delay);
            boomMenuButton.setShowOrderType(showOrderType);
            boomMenuButton.setHideOrderType(hideOrderType);
            boomMenuButton.setAutoDismiss(autoDismiss);
            boomMenuButton.setCancelable(cancelable);
            boomMenuButton.setDimType(dimType);
//            boomMenuButton.setClickEffectType(clickEffectType);
            boomMenuButton.setBoomButtonShadowOffset(boomButtonXOffsetOfShadow, boomButtonYOffsetOfShadow);
//            boomMenuButton.setSubButtonShadowOffset(subButtonsXOffsetOfShadow, subButtonsYOffsetOfShadow);
            boomMenuButton.setTextViewColor(subButtonTextColor);
            boomMenuButton.setImageViewScaleType(subButtonImageScaleType);
            boomMenuButton.setOnClickListener(onClickListener);
            boomMenuButton.setAnimatorListener(animatorListener);
            boomMenuButton.setOnSubButtonClickListener(onSubButtonClickListener);
            boomMenuButton.setShareLineWidth(shareLineWidth);
            boomMenuButton.setShareLine1Color(shareLine1Color);
            boomMenuButton.setShareLine2Color(shareLine2Color);
            boomMenuButton.setGetAnimationRootLayout(getAnimationRootLayout);
            return boomMenuButton;
        }
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)cont).getBaseContext());

        return null;
    }
}
