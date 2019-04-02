package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;

import com.tokopedia.design.R;

/**
 * Created by meta on 19/03/18.
 */

public class RoundFloatingButton extends ButtonCompat {

    public RoundFloatingButton(Context context) {
        super(context);
    }

    public RoundFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initUnifyType(TypedArray attributeArray) {
        Drawable drawable = AppCompatResources.getDrawable(getContext(), R.drawable.bg_button_rounded_white);
        this.setBackground(drawable);

        setMinimumHeight(40);
        setMinHeight(40);
        setCompoundDrawablePadding(8);
    }
}