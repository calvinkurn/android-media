package com.tokopedia.core.onboarding.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;

/**
 * Created by stevenfredian on 7/26/17.
 */

public class OnboardingAnimation {

    public static final long DEFAULT_ANIMATION_DURATION = 1250L;
    public static final int UP_DIRECTION = -1;
    public static final int DOWN_DIRECTION = 1;

    public static ValueAnimator expandTextView(final TextView v, float mScreenWidth) {
        if (v != null) {
            ValueAnimator anim = ValueAnimator.ofInt(0, (int) mScreenWidth);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    layoutParams.width = val;
                    v.setLayoutParams(layoutParams);
                    v.requestLayout();
                }
            });
            v.setVisibility(View.VISIBLE);
            return anim;
        }
        return null;
    }

    public static ObjectAnimator fadeText(final TextView v, Context context, int colorFrom, int colorTo) {
        if (v != null) {
            v.setTextColor(MethodChecker.getColor(context, colorFrom));
            ObjectAnimator anim = ObjectAnimator.ofObject(v, "textColor",
                    new ArgbEvaluator(),
                    MethodChecker.getColor(context, colorFrom),
                    MethodChecker.getColor(context, colorTo));
            v.setVisibility(View.VISIBLE);
            return anim;
        }
        return null;
    }

    public static ObjectAnimator appearText(final TextView v) {
        if (v != null) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 0, 1);
            return anim;
        }
        return null;
    }

    public static ObjectAnimator slideToY(final View view, int direction) {
        if (view != null) {
            final int height;
            if(view.getHeight() == 0){
                view.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                height = view.getMeasuredHeight();
            }else {
                height = view.getHeight();
            }

            ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0, height * direction);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            return anim;
        }
        return null;
    }

    public static ValueAnimator slideToXFromCurrentHeight(final View view, int direction, int delta) {
        if (view != null) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationX",
                    view.getLayoutParams().height, (direction * delta) + view.getWidth() / 2);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            return anim;
        }
        return null;
    }

    public static ValueAnimator slideToX(final View view, int direction, int from, int delta) {
        if (view != null) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationX",
                    view.getLayoutParams().height, (direction * delta) + view.getWidth()/2);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            return anim;
        }
        return null;
    }

    public static ValueAnimator slideReverseX(final View view) {
        if (view != null) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(-(int)(view.getWidth()*1.5), 0);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float val = (Integer) valueAnimator.getAnimatedValue();
                    view.setTranslationX(val);
                }
            });
            return valueAnimator;
        }
        return null;
    }

    public static ObjectAnimator setVisibilityGone(final View view) {
        if (view != null) {
            ObjectAnimator anim = ObjectAnimator.ofInt(view, "visibility", View.VISIBLE, View.GONE);
            return anim;
        }
        return null;
    }


}
