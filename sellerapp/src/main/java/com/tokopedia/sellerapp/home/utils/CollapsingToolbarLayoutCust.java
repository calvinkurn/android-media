package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

/**
 * Created by zulfikarrahman on 10/27/16.
 */

public class CollapsingToolbarLayoutCust extends CollapsingToolbarLayout {
    public boolean isScrimsShown = false;

    OnScrimChangeListener onScrimChangeListener;
    public interface OnScrimChangeListener{
        void onScrimChange(boolean isScrimShown);
    }

    public void setOnScrimChangeListener(OnScrimChangeListener onScrimChangeListener) {
        this.onScrimChangeListener = onScrimChangeListener;
    }
    
    public void notifyScrimChange(){
        if (onScrimChangeListener!= null) {
            onScrimChangeListener.onScrimChange(isScrimsShown);
        }
    }

    public CollapsingToolbarLayoutCust(Context context) {
        super(context);
    }

    public CollapsingToolbarLayoutCust(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsingToolbarLayoutCust(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setScrimsShown(boolean shown) {
        if (isScrimsShown != shown) {
            isScrimsShown = shown;
            if (onScrimChangeListener!= null) {
                onScrimChangeListener.onScrimChange(isScrimsShown);
            }
        }
        super.setScrimsShown(shown);
    }
}
