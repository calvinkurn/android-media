package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;

import com.tokopedia.design.R;

/**
 * Created by meyta on 2/12/18.
 *
 * important! please use app:tabTextAppearance="@style/Tabs.Main"
 * or @style/Tabs.Secondary
 */

public class Tabs extends TabLayout {

    public final static int MAIN = 1;
    public final static int SECONDARY = 2;

    private int tabType;

    public Tabs(Context context) {
        super(context);
        this.init();
    }

    public Tabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public Tabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Tabs, 0, 0);
        try {
            tabType = a.getInteger(R.styleable.Tabs_tabType, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        if (tabType == MAIN) {
            mainStyle();
        } else if (tabType == SECONDARY) {
            secondaryStyle();
        }

        this.setTabGravity(GRAVITY_FILL);
        this.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
        init();
    }

    private void mainStyle() {
        int white = ContextCompat.getColor(getContext(), R.color.Unify_N0);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G500));
        this.setSelectedTabIndicatorColor(white);
        this.setTabTextColors(white, white);
        this.setTabMode(MODE_FIXED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(10);
            this.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_G500);
        } else {
            this.setBackgroundResource(R.drawable.bg_green_toolbar_drop_shadow);
        }
    }

    private void secondaryStyle() {
        int gray = ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_32);
        int green = ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G500);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.Unify_N0));
        this.setSelectedTabIndicatorColor(green);
        this.setTabTextColors(gray, green);
        this.setTabMode(MODE_SCROLLABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(10);
            this.setBackgroundResource(R.color.Unify_N0);
        } else {
            this.setBackgroundResource(com.tokopedia.resources.common.R.drawable.bg_white_toolbar_drop_shadow);
        }
    }

}