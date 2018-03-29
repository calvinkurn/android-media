package com.tokopedia.gamification.floating.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.EggGamificationActivity;
import com.tokopedia.gamification.floating.listener.OnDragTouchListener;
import com.tokopedia.gamification.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hendry on 28/03/18.
 */

public class FloatingEggButtonFragment extends BaseDaggerFragment {

    private static final String COORD_X = "x";
    private static final String COORD_Y = "y";
    public static final String COORD_EGG_PREF = "_egg.pref";

    private View vgRoot;
    private View vgFloatingEgg;
    private ImageView ivFloatingEgg;
    private TextView tvFloatingCounter;
    private TextView tvFloatingTimer;

    private float eggMarginRight;
    private float eggMarginBottom;
    private boolean isDraggable;
    private int xEgg;
    private int yEgg;

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
        eggMarginRight = a.getDimensionPixelOffset(R.styleable.FloatingEggButtonFragment_margin_right, 0);
        eggMarginBottom = a.getDimensionPixelOffset(R.styleable.FloatingEggButtonFragment_margin_bottom, 0);
        a.recycle();
    }

    public void setFloatingEggVisibility(boolean isVisible, int delayInMs) {
        //TODO for animation and delay, set here
        vgFloatingEgg.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEggCoordinate();
    }

    private void initEggCoordinate(){
        if (eggMarginRight != 0 && eggMarginBottom != 0) {
            int bottomPx = 0;
            int rightPx = 0;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vgFloatingEgg.getLayoutParams();
            if (isDraggable && hasCoordPreference()) {
                layoutParams.setMargins(xEgg, yEgg, 0, 0);
            } else {
                layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                Resources r = getResources();
                bottomPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, eggMarginBottom, r.getDisplayMetrics());
                rightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, eggMarginRight, r.getDisplayMetrics());

                layoutParams.setMargins(0, 0, rightPx, bottomPx);
            }
        }
    }

    private boolean hasCoordPreference() {
        SharedPreferences sharedPreferences = getSharedPref();
        xEgg = sharedPreferences.getInt(COORD_X, -1);
        yEgg = sharedPreferences.getInt(COORD_Y, -1);
        return xEgg != -1 && yEgg != -1;
    }

    private void saveCoordPreference() {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putInt(COORD_X, (int) vgFloatingEgg.getX());
        editor.putInt(COORD_Y, (int) vgFloatingEgg.getY());
        editor.apply();
    }

    private SharedPreferences getSharedPref(){
        return getContext().getSharedPreferences(
                getActivity().getClass().getSimpleName() + COORD_EGG_PREF
                , MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEggData();
    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO reset the timer if any
        // TODO cancel the load data of presenter
    }

    // TODO load data to get egg/token data from server or cache here
    private void loadEggData() {
        onSuccessLoadTokopointsToken();
    }

    public void onSuccessLoadTokopointsToken() {
        //TODO will set the variable from the model here
        boolean offFlag = false;
        int sumToken = 99;
        String sumTokenString = "99+";
        String tokenUnit = "buah";
        //String pageUrl = "http://tokopedia.com";
        //String appLink = "tokopedia://";
        long timeRemainingSeconds = 61;
        String timeRemainingSecondsString = "00:00:01";
        boolean isShowTime = true;
        String imageUrl = "https://user-images.githubusercontent.com/13778932/38075015-049430aa-335b-11e8-822e-ca662dc45b7f.png";

        setFloatingEggVisibility(!offFlag, 0);

        vgFloatingEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO will point to which activity or page based on applink/url
                Intent intent = new Intent(getContext(), EggGamificationActivity.class);
                startActivity(intent);
            }
        });

        if (isDraggable) {
            vgFloatingEgg.setOnTouchListener(new OnDragTouchListener(vgFloatingEgg, vgRoot,
                    new OnDragTouchListener.OnDragActionListener() {
                        @Override
                        public void onDragStart(View view) {

                        }

                        @Override
                        public void onDragEnd(View view) {
                            saveCoordPreference();
                        }
                    }));
        } else {
            vgFloatingEgg.setOnTouchListener(null);
        }

        ImageHandler.LoadImage(ivFloatingEgg, imageUrl);
        tvFloatingCounter.setText(sumTokenString);
        if (isShowTime) {
            tvFloatingTimer.setText(timeRemainingSecondsString);
            tvFloatingTimer.setVisibility(View.VISIBLE);
        } else {
            tvFloatingTimer.setVisibility(View.GONE);
        }

        if (timeRemainingSeconds > 0) {
            // TODO run the timer here
        } else {
            // TODO stop the countdown timer if any
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
