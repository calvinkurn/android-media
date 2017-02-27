package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

/**
 * Created by zulfikarrahman on 10/27/16.
 */

public class CollapsingToolbarLayoutCust extends CollapsingToolbarLayout {
    public boolean isScrimsShown = false;

    public CollapsingToolbarLayoutCust(Context context) {
        super(context);
    }

    public CollapsingToolbarLayoutCust(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsingToolbarLayoutCust(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    
    public void setScrimsShown(boolean shown) {
        isScrimsShown = shown;
        super.setScrimsShown(shown);
    }
}
