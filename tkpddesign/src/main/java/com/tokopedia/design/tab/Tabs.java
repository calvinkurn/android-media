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
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
        init();
    }

    private void mainStyle() {
        int white = ContextCompat.getColor(getContext(), R.color.white);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        this.setTabGravity(GRAVITY_FILL);
        this.setSelectedTabIndicatorColor(white);
        this.setSelectedTabIndicatorHeight(4);
        this.setTabMode(MODE_SCROLLABLE);
        this.setTabTextColors(white, white);
    }

    private void secondaryStyle() {
        int green = ContextCompat.getColor(getContext(), R.color.tkpd_main_green);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        this.setTabGravity(GRAVITY_FILL);
        this.setSelectedTabIndicatorColor(green);
        this.setSelectedTabIndicatorHeight(4);
        this.setTabMode(MODE_SCROLLABLE);
        this.setTabTextColors(green, green);
    }

}
