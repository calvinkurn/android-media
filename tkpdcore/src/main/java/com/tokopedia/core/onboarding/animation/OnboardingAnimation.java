package com.tokopedia.core.onboarding.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.util.MethodChecker;

/**
 * Created by stevenfredian on 7/26/17.
 */

public class OnboardingAnimation {

    public static final long DEFAULT_ANIMATION_DURATION = 750L;
    public static final int UP_DIRECTION = -1;
    public static final int DOWN_DIRECTION = 1;

    public static ValueAnimator expandTextView(final TextView v, float mScreenWidth){
        if(v != null){
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
            return anim;
        }
        return null;
    }

    public static ObjectAnimator fadeText(final TextView v, Context context, int colorFrom, int colorTo){
        if(v != null){
            v.setTextColor(MethodChecker.getColor(context,colorFrom));
            ObjectAnimator anim = ObjectAnimator.ofObject(v, "textColor",
                    new ArgbEvaluator(),
                    MethodChecker.getColor(context, colorFrom),
                    MethodChecker.getColor(context, colorTo));
            return anim;
        }
        return null;
    }

    public static ValueAnimator slideTo(final View view, int direction) {
        if(view!=null){
            final float yDelta = view.getHeight();
            int from = (int)view.getY();
            int to = (int)view.getY() + (int)(yDelta * direction);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
                    params.gravity = Gravity.CENTER;
                    view.setY(val);
                    view.setLayoutParams(params);
                    view.requestLayout();
                }
            });
            return valueAnimator;
        }
        return null;
    }

}
