package com.tokopedia.tkpd.beranda.presentation.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SectionContainer extends LinearLayout {
    private Animation inAnimation;
    private Animation outAnimation;

    public SectionContainer(Context context) {
        super(context);
    }

    public SectionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            if (visibility == VISIBLE) {
                if (inAnimation != null) startAnimation(inAnimation);
            } else if ((visibility == INVISIBLE) || (visibility == GONE)) {
                if (outAnimation != null) startAnimation(outAnimation);
            }
        }
        super.setVisibility(visibility);
    }
}