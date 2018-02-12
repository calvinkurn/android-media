package com.tokopedia.design.tab;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.tokopedia.design.R;

/**
 * Created by meyta on 2/12/18.
 */

public class Tabs extends TabLayout {

    public Tabs(Context context) {
        super(context);
        this.defaultStyle();
    }

    public Tabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.defaultStyle();
    }

    public Tabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.defaultStyle();
    }

    private void defaultStyle() {
        int white = ContextCompat.getColor(getContext(), R.color.white);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        this.setTabGravity(GRAVITY_FILL);
        this.setSelectedTabIndicatorColor(white);
        this.setSelectedTabIndicatorHeight(4);
        this.setTabMode(MODE_FIXED);
        this.setTabTextColors(white, white);

    }

}
