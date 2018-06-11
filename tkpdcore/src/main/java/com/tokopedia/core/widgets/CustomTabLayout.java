package com.tokopedia.core.widgets;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.tokopedia.core.util.UtilsDevice;

import java.lang.reflect.Field;

/**
 * Created by Erry Suprayogi on 09/12/16.
 */

public class CustomTabLayout extends TabLayout {
    private static final int WIDTH_INDEX = 0;
    private static final int DIVIDER_FACTOR = 5;
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";

    public CustomTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();
    }

    private void initTabMinWidth() {
        int[] wh = UtilsDevice.getScreenSize(getContext());
        int tabMinWidth = wh[WIDTH_INDEX] / DIVIDER_FACTOR;

        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        initShadow();
    }

    private void initShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(10);
            this.setBackgroundResource(com.tokopedia.design.R.color.tkpd_main_green);
        } else {
            this.setBackgroundResource(com.tokopedia.design.R.drawable.bg_green_toolbar_drop_shadow);
        }
    }
}
