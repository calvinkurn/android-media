package com.tokopedia.design.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;

import com.tokopedia.design.R;

/**
 * Created by meta on 19/03/18.
 */

public class RoundedFloatingButton extends ButtonCompat {

    public RoundedFloatingButton(Context context) {
        super(context);
        init();
    }

    public RoundedFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable drawable = AppCompatResources.getDrawable(getContext(), R.drawable.bg_button_rounded_white);
        this.setBackground(drawable);
    }
}