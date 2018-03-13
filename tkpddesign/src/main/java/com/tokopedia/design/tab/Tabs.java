package com.tokopedia.design.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.tokopedia.design.R;

/**
 * Created by meyta on 2/12/18.
 *
 * how to use?
 * xml : tabType=main (main, secondary)
 *
 * java : setTabType(Tabs.MAIN)
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
        int white = ContextCompat.getColor(getContext(), R.color.white);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        this.setSelectedTabIndicatorColor(white);
        this.setTabTextColors(white, white);
        this.setTabMode(MODE_FIXED);
    }

    private void secondaryStyle() {
        int gray = ContextCompat.getColor(getContext(), R.color.font_black_disabled_38);
        int green = ContextCompat.getColor(getContext(), R.color.tkpd_main_green);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        this.setSelectedTabIndicatorColor(green);
        this.setTabTextColors(gray, green);
        this.setTabMode(MODE_SCROLLABLE);
    }

}